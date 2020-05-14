package com.alibaba.alink.service.kafkaOutput;

import com.alibaba.alink.pojo.InfoSorted;
import com.alibaba.alink.pojo.SortedData;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class Output {


    public static void outputData(String topic, HashMap<String, List<InfoSorted>> map) {

        Properties prop = new Properties();
        // 指定请求的kafka集群列表
        prop.put("bootstrap.servers", "192.168.1.103:9092");// 指定响应方式
        //prop.put("acks", "0")
        prop.put("acks", "all");
        // 请求失败重试次数
        //prop.put("retries", "3")
        // 指定key的序列化方式, key是用于存放数据对应的offset
        prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 指定value的序列化方式
        prop.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 配置超时时间
        prop.put("request.timeout.ms", "60000");
        // 得到生产者的实例
        KafkaProducer producer = new KafkaProducer<String, String>(prop);
        // 模拟一些数据并发送给kafka
        for (String key : map.keySet()) {

            List<InfoSorted> list = map.get(key);
            String method = list.get(0).getSort();

            StringBuilder builder = new StringBuilder();
            for (InfoSorted infoSorted : list) {
                builder.append("(");
                builder.append(infoSorted.getLongitude());
                builder.append(",");
                builder.append(infoSorted.getLatitude());
                builder.append(",");
                builder.append(infoSorted.getTimeStamp());
                builder.append(")_");
            }
            String info = (builder.delete(builder.length() - 1, builder.length()).toString());
            producer.send(new ProducerRecord<String, String>(topic, method + "," + info));
        }
        producer.send(new ProducerRecord<String, String>(topic, "end"));
        producer.flush();
    }

}
