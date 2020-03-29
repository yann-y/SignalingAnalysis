package com.fengli.recommender

import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoClientURI}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}


/**
  * 1538498482636,  记录开始时间
  * 460000095007329090, 用户唯一识别码
  * 16789,基站位置区编码
  * 67567924, 扇区编号
  * 86137666647316, 电话号
  * 1538498481670,  记录结束时间
  * , 一级行政区编号
  * , 二级行政区编号
  * 4,  信息类别1
  * #*#6137 信息类别2
  */
case class Original(timestamp: String, imsi:String, lac_id:String, cell_id:String, phone:String,
                        timestamp1:String, tmp0:String,tmp1:String, nid:String, npid:String)

/**
  * 123.4159698,  经度
  * 41.80778122,  维度
  * 16789-67567924  基站信息
  */
case class LngLat(lng:String, lat:String, laci:String)
/**
  * 123.453579, 经度
  * 41.743061,  维度
  * 地铁, 出行方式
  * 奥体中心(地铁站),  站名
  * 2-9 线路
  */
case class GoingOutStatic(lng:String, lat:String, mode:String, modeName:String)

/**
  * MongoDB连接配置
  * @param uri  MongoDB的uri
  * @param db  操作的db
  */
case class MongoConfig(uri : String, db : String)

object DataLoader {
  // 定义数据文件路径
  val Original_DATA_PATH = "E:\\IdeaProject\\ECommerceRecommendSystem2\\recommender\\DataLoader\\src\\main\\resources\\OriginalData.csv"
  val GoingOutStatic_DATA_PATH = "E:\\IdeaProject\\ECommerceRecommendSystem2\\recommender\\DataLoader\\src\\main\\resources\\GoingOutStaticData.csv"
  val LngLat_DATA_PATH = "E:\\IdeaProject\\ECommerceRecommendSystem2\\recommender\\DataLoader\\src\\main\\resources\\LngLatData.csv"

  // 定义mongodb中存储的表名
  val MONGODB_Original = "Original"
  val MONGODB_GoingOutStatic = "GoingOutStatic"
  val MONGODB_LngLat = "LngLat"

  def main(args: Array[String]): Unit = {
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://localhost:27017/recommender",
      "mongo.db" -> "recommender"
    )

    // 创建一个spark config
    val sparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("DataLoader")
    // 创建spark session
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()

    import spark.implicits._
    // 加载数据
    val OriginalRDD = spark.sparkContext.textFile(Original_DATA_PATH)
    val OriginalDF = OriginalRDD.map( item => {
      // Original数据通过,分隔，切分出来
      val attr = item.split(",")

      Original( attr(0).trim,attr(1).trim, attr(2).trim,attr(3).trim,attr(4).trim,
        attr(5).trim,attr(6).trim,attr(7).trim,attr(8).trim,attr(9).trim)
    }).toDF()

    val LngLatRDD = spark.sparkContext.textFile(LngLat_DATA_PATH)
    val lngLatDF = LngLatRDD.map( item => {
      val attr = item.split(",")
      LngLat( attr(0).trim, attr(1).trim, attr(2).trim)
    }).toDF()

    val GoingOutStaticRDD = spark.sparkContext.textFile(GoingOutStatic_DATA_PATH)
    val GoingOutStaticDF = GoingOutStaticRDD.map( item => {
      val attr = item.split(",")
        GoingOutStatic(attr(0).trim, attr(1).trim, attr(2).trim, attr(3).trim)
    }).toDF()

    implicit val mongoConfig = MongoConfig( config("mongo.uri"),config("mongo.db"))
    storeDataInMongoDB(OriginalDF, lngLatDF, GoingOutStaticDF)

    spark.stop()
  }
  def storeDataInMongoDB(OriginalDF: DataFrame,LngLatDF: DataFrame,
                         GoingOutStatic: DataFrame )(implicit mongoConfig: MongoConfig): Unit = {
    // 新建一个  mongodb的连接 , 客户端
    val mongoClient = MongoClient( MongoClientURI(mongoConfig.uri))

    // 定义要操作的mongodb表  db.Original
    val OriginalCollection = mongoClient( mongoConfig.db )( MONGODB_Original )
    val LngLatCollection = mongoClient( mongoConfig.db )( MONGODB_LngLat )
    val GoingOutStaticCollection = mongoClient( mongoConfig.db )( MONGODB_GoingOutStatic )

    //  如果表已经存在就删掉
    OriginalCollection.dropCollection()
    LngLatCollection.dropCollection()
    GoingOutStaticCollection.dropCollection()

    // 将当前数据存入对应表中
    OriginalDF.write
      .option("uri",mongoConfig.uri)
      .option("collection", MONGODB_Original)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    LngLatDF.write
      .option("uri",mongoConfig.uri)
      .option("collection", MONGODB_LngLat)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    GoingOutStatic.write
      .option("uri",mongoConfig.uri)
      .option("collection", MONGODB_GoingOutStatic)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    // 创建索引
    OriginalCollection.createIndex( MongoDBObject( "lac_id" -> 1))

    mongoClient.close()
  }
}
