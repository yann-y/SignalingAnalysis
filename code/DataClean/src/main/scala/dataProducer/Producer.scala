package dataProducer

import java.text.SimpleDateFormat
import java.util.Properties

import config.Config
import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.flink.streaming.api.scala._

object Producer {
  case class Source(timstamp: String, imsi: String, lac_id: String, cell_id: String)
  //数据按时间排序,形成时间序列流
  def dataProcess(): Seq[Source] = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val inputStream = env.readTextFile(Config.dataSource)
    val data = inputStream.map(data => {
      val dataArray = data.split(",")
      //抽取timestamp,imsi,lac_id,cell_id 四个字段
      Source(dataArray(0), dataArray(1), dataArray(2), dataArray(3))
    })
      .map(s => {
        val simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss")
        val date = simpleDateFormat.format(s.timstamp.toLong)
        Source(date, s.imsi, s.lac_id, s.cell_id)
      })
      //去除干扰数据条目(不是2018.10.03当天的数据)
      .filter(_.timstamp.substring(0, 8).equals("20181003"))
      .map(c => {
        val simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss")
        val date = simpleDateFormat.parse(c.timstamp).getTime + ""
        Source(date.substring(0, date.length - 3), c.imsi, c.lac_id, c.cell_id)
      })
      .sortPartition(_.timstamp, Order.ASCENDING)

    data.collect()
  }

  def main(args: Array[String]): Unit = {
    val data = dataProcess()
    val prop = new Properties
    //读取配置文件
    val in = getClass.getClassLoader.getResourceAsStream("producer.properties")
    prop.load(in)
    //创建Producer
    val producer = new KafkaProducer[String, String](prop)
    // 模拟一些数据并发送给kafka
    var index = 0
    var time = 1538496000
    //每5秒发一条
    while (index < data.length) {
      val msg = data(index)
      if (msg.timstamp.toLong < time) {
        //发送数据
        producer.send(new ProducerRecord[String, String](Config.timeStream, msg.timstamp + "," + msg.imsi + "," + msg.lac_id + "," + msg.cell_id))
      }
      else {
        time = time + 5
        //每5秒发一条
        Thread.sleep(1 * 1000)
      }
      index = index + 1
      //println(index)
    }
    producer.flush()
  }
}
