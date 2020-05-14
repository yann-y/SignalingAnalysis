package modeSplit

import java.text.SimpleDateFormat
import java.util.Properties

import Utils.DistanceUtil.DistanceUtil
import Utils.SpeedUtil.SpeedUtil
import Utils.TimeUtil.TimeUtil
import config.Config
import pojo.CustomWaterMark
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import org.apache.flink.util.Collector
import service.GetLocateMap
import org.apache.flink.streaming.api.scala._

import scala.collection.mutable.ArrayBuffer

object Analyse {
  //距离阈值
  val distance_threshold = 300
  //时间阈值
  val time_threshold = 30
  //速度阈值
  val speed_threshold = 80
  //漂移阈值
  val drift_threshold = 2000

  case class Source(timstamp: String, imsi: String, lac_id: String, cell_id: String)

  case class Data(imsi: String, timestamp: String, longitude: String, latitude: String)

  //驻留清洗
  def stayClean(sources: ArrayBuffer[Data]): Unit = {
    val array = sources
    var index = 0
    while (index < array.size - 1) {
      //起点
      val from = array(index)
      //终点
      val to = array(index + 1)
      val distance = DistanceUtil.getDistance(from.longitude, from.latitude, to.longitude, to.latitude)
      val time = TimeUtil.getMinute(from.timestamp, to.timestamp)
      if (distance < distance_threshold && time < time_threshold || (from.longitude.equals(to.longitude) && from.latitude.equals(to.latitude))) {
        array.remove(index)
      }
      else {
        index = index + 1
      }
    }
  }

  def pingpongClean(source: ArrayBuffer[Data]): Unit = {
    val array = source
    var index = 0
    while (index < array.size - 2) {
      //第一个点
      val first = array(index)
      //第二个点
      val third = array(index + 2)
      //如果第一个点和第三个点相同,且第一个点到第三个点的时间小于阈值,则删除第第二个点和第三个点
      if (first.longitude.equals(third.longitude) && first.latitude.equals(third.latitude)) {
        val stayTime = TimeUtil.getMinute(first.timestamp, third.timestamp)
        if (stayTime < time_threshold) {
          array.remove(index + 1)
          array.remove(index + 1)
        }
        else {
          index = index + 1
        }
      }
      else {
        index = index + 1
      }
    }
  }

  def driftClean(source: ArrayBuffer[Data]): Unit = {
    val array = source
    var index = 0
    while (index < array.size - 1) {
      val first = array(index)
      val second = array(index + 1)
      //时间
      val time = TimeUtil.getSecond(first.timestamp, second.timestamp)
      //距离
      val distance = DistanceUtil.getDistance(first.longitude, first.latitude, second.longitude, second.latitude)
      //速度
      val speed = SpeedUtil.getSpeedByHour(time, distance)
      if (index < array.size - 2) {
        val third = array(index + 2)
        val d2 = DistanceUtil.getDistance(second.longitude, second.latitude, third.longitude, third.latitude)
        val d3 = DistanceUtil.getDistance(first.longitude, first.latitude, third.longitude, third.latitude)
        //速度大于阈值则剔除
        if (speed > speed_threshold || (d2 > drift_threshold && distance > drift_threshold && d3 < 2 * drift_threshold)) {
          array.remove(index + 1)
        }
        else {
          index = index + 1
        }
      }
      else {
        if (speed > speed_threshold) {
          array.remove(index + 1)
        }
        else {
          index = index + 1
        }
      }
    }
  }


  def main(args: Array[String]): Unit = {
    val map = GetLocateMap.getLocateMap()
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val properties = new Properties
    val kafkaPort = Config.kafkaPort
    properties.setProperty("bootstrap.servers", kafkaPort);
    properties.setProperty("group.id", "flink-group02")

    val consumer = new FlinkKafkaConsumer("timeStream", new SimpleStringSchema(), properties);
    //从最早开始消费
    consumer.setStartFromEarliest()
    consumer.assignTimestampsAndWatermarks(new CustomWaterMark)
    val stream = env.addSource(consumer)
    val data = stream.map(c => {
      val array = new Array[String](4)
      for (i <- 0 to array.length - 1) {
        array(i) = ""
      }
      val tmp = c.split(",")
      for (i <- 0 to tmp.length - 1) {
        array(i) = tmp(i)
      }
      Source(array(0), array(1), array(2), array(3))
    })
      //去除imsi中，包含特殊字符的数据条目(‘#’,’*’,’^’)
      .filter(s => (!s.imsi.contains("*")) && !s.imsi.contains("#") && !s.imsi.contains("^"))
      //去除空间信息残缺的记录条目(imsi、lac_id、cell_id中为空)
      .filter(s => (s.imsi.length() > 1) && s.lac_id.length() > 1 && s.cell_id.length() > 1)
      //timestamp时间戳转换格式 ‘20190603000000’--年月日时分秒
      .map(s => {
      val simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss")
      val date = simpleDateFormat.format(s.timstamp.toLong * 1000)
      Source(date, s.imsi, s.lac_id, s.cell_id)
    })
      //去除干扰数据条目(不是2018.10.03当天的数据)
      .filter(_.timstamp.substring(0, 8).equals("20181003"))
      //去除两数据源关联后经纬度为空的数据条目
      .filter(s => {
      map.containsKey(s.lac_id + "-" + s.cell_id)
    })
      .map(s => {
        val array = map.get(s.lac_id + "-" + s.cell_id).split("-")
        val longitude = array(0)
        val latitude = array(1)
        Data(s.imsi, s.timstamp, longitude, latitude)
      })
      .keyBy(_.imsi)
      .window(TumblingEventTimeWindows.of(Time.minutes(30)))
      .apply(new WindowFunction[Data, ArrayBuffer[Data], String, TimeWindow] {
        override def apply(key: String, window: TimeWindow, input: Iterable[Data], out: Collector[ArrayBuffer[Data]]): Unit = {
          val array = new ArrayBuffer[Data]()

          input.foreach(c => {
            array.append(c)
          })
          stayClean(array)
          pingpongClean(array)
          driftClean(array)
          out.collect(array)
        }
      }).filter(c => {
      c.length >= 2
    }).map(data => {
      val array = data
      val output = new StringBuilder()
      output.append(array(0).imsi)
      output.append(":")
      for (i <- 0 to array.length - 1) {
        val tmp = array(i)
        output.append("(")
        output.append(tmp.longitude)
        output.append(",")
        output.append(tmp.latitude)
        output.append(",")
        output.append(tmp.timestamp)
        if (i != array.length - 1) {
          output.append(")_")
        }
        else {
          output.append(")")
        }
      }
      output.toString()
    })



    val KAFKA_PROP: Properties = new Properties() {
      setProperty("bootstrap.servers", kafkaPort) //broker地址
      setProperty("group.id", "test") //组id
    }
    //处理结果写入kafka
    data.addSink(new FlinkKafkaProducer[String]("routeChain", new SimpleStringSchema(), KAFKA_PROP)).setParallelism(1)
    //kafka配置参数
    env.execute()
  }


}
