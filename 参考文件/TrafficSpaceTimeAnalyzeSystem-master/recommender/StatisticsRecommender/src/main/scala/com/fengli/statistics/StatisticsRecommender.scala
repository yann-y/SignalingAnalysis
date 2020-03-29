package com.fengli.statistics

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.SparkConf
import org.apache.spark.sql.types.{IntegerType, LongType}
import org.apache.spark.sql.{DataFrame, SparkSession}

case class Original(timestamp: String, imsi:String, lac_id:String, cell_id:String, phone:String,
                    timestamp1:String, tmp0:String,tmp1:String, nid:String, npid:String)
case class LngLat(lng:String, lat:String, laci:String)

case class MongoConfig(uri : String, db : String)

object StatisticsRecommender {
  val MONGODB_LngLat = "LngLat"
  val MONGODB_Original = "Original"
  val RATE_MORE_1 = "count_id"
  val RATE_MORE_2 = "per_time"

  def main(args: Array[String]): Unit = {
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://localhost:27017/recommender",
      "mongo.db" -> "recommender"
    )

    // 创建一个spark config
    val sparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("StatisticsRecommender")
    // 创建spark session
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()

    import spark.implicits._
    implicit val mongoConfig = MongoConfig( config("mongo.uri"),config("mongo.db"))

    // 加载数据
    val  OriginalDF = spark.read
      .option("uri",mongoConfig.uri)
      .option("collection", MONGODB_Original)
      .format("com.mongodb.spark.sql")
      .load()
      .as[Original]
      .toDF()

    // 创建一张临时表 originals
    OriginalDF.createOrReplaceTempView("originals")

    // TODO:用spark sql去做不同的数据统计
    //1. 出行方式占比
    //2. 区域拥堵排名
    //3. 人口驻留流量排行
    //4. 人口总流量
    //5. 人口总量
    val originalMoreDF = spark.sql("select count(distinct imsi) as count from originals")
    originalMoreDF.show()
//    storeDFInMongoDB_1( originalMoreDF, RATE_MORE_1 )

    // 创建一个日期格式化工具
    val simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss")
    // 注册UDF，将时间戳转化为年月日时分秒格式 yyyyMMddHHmmss
    spark.udf.register("changeDate", (x : Long) => simpleDateFormat.format(new Date(x)))
    val originalMoreOfChangeTime = spark.sql("select imsi,timestamp,timestamp1,lac_id,cell_id from originals")
    import org.apache.spark.sql.functions._
    originalMoreOfChangeTime
      .select(col("timestamp").cast(LongType),
      col("timestamp1").cast(LongType))
    val originalDF = spark.sql("select imsi,(changeDate(timestamp1) - changeDate(timestamp)) " +
      "as differTime, concat(concat(lac_id,'-'),cell_id) as lngLat from originals  order by lngLat")
//    originalDF.show()
    val frameDF = originalDF.select(col("lngLat"))
    val rows = frameDF.collect()
    for (r <- rows) println(r)
//    frameDF.show()
//    storeDFInMongoDB_2( originalDF, RATE_MORE_2)

    spark.stop()
  }

  /**
    * 将DF 数据写入mongoDB中
    * @param df DF
    * @param collection_name 表名
    * @param mongoConfig mongodb配置
    */
  def storeDFInMongoDB_1(df: DataFrame, collection_name: String)(implicit  mongoConfig: MongoConfig): Unit ={
    import org.apache.spark.sql.functions._
    // 输出列的类型
    df.select(col("count").cast(IntegerType))
      .write
      .option("uri", mongoConfig.uri)
      .option("collection", collection_name)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()
  }

  /**
    * 将DF 数据写入mongoDB中
    * @param df
    * @param collection_name
    * @param mongoConfig
    */
  def storeDFInMongoDB_2(df: DataFrame, collection_name: String) (implicit mongoConfig: MongoConfig): Unit ={
    df.write
      .option("uri", mongoConfig.uri)
      .option("collection", collection_name)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()
  }
}
