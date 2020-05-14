package config;


//配置类
public class Config {

    //kafka服务器端口
    public final static String kafkaPort = "192.168.1.103:9092";
    //源数据所在路径
    public final static String dataSource = "/home/ai/waibao/data/newSource.txt";
    //时间序列topic
    public final static String timeStream = "timeStream";
    //热力点topic
    public final static String heatMapData = "heatMapData";
    //驻留数据
    public final static String stayData = "stayData";
    //出行链
    public final static String routeChain = "routeChain";

}
