package heatMap

import java.text.SimpleDateFormat
import java.util.Properties

import dataProducer.Producer.Source
import pojo.CustomWaterMark
import config.Config
import routeChain.Analyse.Data
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import service.GetLocateMap
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.{AllWindowFunction, WindowFunction}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

import scala.collection.mutable.ArrayBuffer

object Process {
  def main(args: Array[String]): Unit = {
    //获取关联数据表(为了去除lac_id和cell_id为空的数据)
    val map = GetLocateMap.getLocateMap()
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val properties = new Properties
    //接收来自kafka的数据

    properties.setProperty("bootstrap.servers", Config.kafkaPort)
    properties.setProperty("group.id", "heatMap.process")
    val consumer = new FlinkKafkaConsumer(Config.timeStream, new SimpleStringSchema(), properties)
    consumer.setStartFromEarliest()
    //添加水印

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
      //数据格式转换
      .map(s => {
        val array = map.get(s.lac_id + "-" + s.cell_id).split("-")
        val longitude = array(0)
        val latitude = array(1)
        Data(s.imsi, s.timstamp, longitude, latitude)
      })
      //5s一个窗口
      .timeWindowAll(Time.seconds(5))
      .apply(new AllWindowFunction[Data, ArrayBuffer[Data], TimeWindow] {
        override def apply(window: TimeWindow, input: Iterable[Data], out: Collector[ArrayBuffer[Data]]): Unit = {
          val array = new ArrayBuffer[Data]()
          input.foreach(c => array.append(c))
          out.collect(array)
        }
      })
      //将数据转换为输出格式
      .map(c => {
        val output = new StringBuffer()
        for (i <- 0 to c.length - 1) {
          output.append("(")
          output.append(c(i).timestamp)
          output.append(",")
          output.append(c(i).imsi)
          output.append(",")
          output.append(c(i).longitude)
          output.append(",")
          output.append(c(i).latitude)
          if (i != c.length - 1)
            output.append(")_")
          else
            output.append(")")
        }
        output.toString
      })
    //处理结果写入kafka
    print("yes")
    data.addSink(new FlinkKafkaProducer[String](Config.heatMapData, new SimpleStringSchema(), properties))
    //kafka配置参数
    env.execute()
  }
}
