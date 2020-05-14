
package com.alibaba.alink.main;


import com.alibaba.alink.service.algorithm.DoCluster;
import com.alibaba.alink.service.algorithm.impl.DoClusterImpl;
import com.alibaba.alink.service.dataSource.GetSourceData;
import com.alibaba.alink.operator.batch.BatchOperator;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;

public class KmeansSort {


    public static void main(String[] args) throws Exception {
        // 配置信息
        Properties prop = new Properties();
        String kafkaPort = "192.168.1.103:9092";
        prop.put("bootstrap.servers",kafkaPort);
        // 指定消费者组
        //prop.put("group.id", "cluster-sort02");
        prop.put("group.id", "cluster-sort09");
        // 指定消费位置: earliest/latest/none
        prop.put("auto.offset.reset", "earliest");
        // 指定消费的key的反序列化方式
        prop.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // 指定消费的value的反序列化方式
        prop.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        prop.put("enable.auto.commit", "true");
        prop.put("session.timeout.ms", "30000");

        // 得到Consumer实例
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(prop);
        // 首先需要订阅topic
        //kafkaConsumer.subscribe(Collections.singletonList("cluster"));
        kafkaConsumer.subscribe(Collections.singletonList("routeChain"));

        // 开始消费数据
        int count = 0;
        ArrayList<String> list = new ArrayList<>();

        while (true) {
            ConsumerRecords<String, String> msgs = kafkaConsumer.poll(2000);
            System.out.println(msgs.count());

            if (msgs.count() == 0) {
                count++;

            }
            if (count == 12) {
                if(list.size()>100)
                {

                    BatchOperator data = GetSourceData.getSourceData(list);
                    String[] means = new String[]{"GaussianMixture","KMeans","BisectingKMeans"};
                    DoCluster doCluster = new DoClusterImpl();
                    for(int i=0;i<means.length;i++)
                    {
                        System.out.println("hah");
                        doCluster.doCluser(means[i],data);
                    }
                    list.clear();

                }
                System.out.println("tes");
                Thread.sleep(30*60*1000);
                count = 0;
            }
            Iterator<ConsumerRecord<String, String>> it = msgs.iterator();
            while (it.hasNext())
            {
                list.add(it.next().value());
            }
        }
    }
}
