package stayAnalyse

import java.text.SimpleDateFormat
import java.util.Properties

import Utils.DistanceUtil.DistanceUtil
import Utils.TimeUtil.TimeUtil
import routeChain.Analyse.{Data, Source}
import pojo.CustomWaterMark
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import service.GetLocateMap
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import config.Config
import scala.collection.mutable.ArrayBuffer

object StayAnalyse {
  //距离阈值
  val distance_threshold = 300
  //时间阈值
  val time_threshold = 30

  //驻留分析
  def stay(sources: ArrayBuffer[Data]): Unit = {
    val array = sources
    var index = 0
    while (index < array.size - 1) {
      //起点
      val from = array(index)
      //终点
      val to = array(index + 1)
      val distance = DistanceUtil.getDistance(from.longitude, from.latitude, to.longitude, to.latitude)
      val time = TimeUtil.getMinute(from.timestamp, to.timestamp)
      if ((distance < distance_threshold && time < time_threshold) || (from.longitude.equals(to.longitude) && from.latitude.equals(to.latitude))) {
        index = index + 1
      }
      else {
        array.remove(index)
      }
    }
  }

  def main(args: Array[String]): Unit = {

    val map = GetLocateMap.getLocateMap()
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val properties = new Properties
    val kafkaPort = Config.kafkaPort
    properties.setProperty("bootstrap.servers", kafkaPort)
    properties.setProperty("group.id", "stayAnalyse.StayAnalyse")
    val consumer = new FlinkKafkaConsumer(Config.timeStream, new SimpleStringSchema(), properties)
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
      //三十分钟为一个时间窗口
      .window(TumblingEventTimeWindows.of(Time.minutes(30)))
      .apply(new WindowFunction[Data, ArrayBuffer[Data], String, TimeWindow] {
        override def apply(key: String, window: TimeWindow, input: Iterable[Data], out: Collector[ArrayBuffer[Data]]): Unit = {
          val array = new ArrayBuffer[Data]()

          input.foreach(c => {
            array.append(c)
          })
          //驻留数据提取
          stay(array)
          out.collect(array)
        }
      })
      .filter(c => c.length > 1)
      .map(data => {
        val array = data
        val output = new StringBuffer()
        output.append(array(0).imsi)
        output.append(":")
        for (i <- 0 to (array.length - 1)) {
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
        output.toString
      })
    //处理结果写入kafka
    data.addSink(new FlinkKafkaProducer[String](Config.stayData, new SimpleStringSchema(), properties))
    //kafka配置参数
    env.execute()
  }

}
