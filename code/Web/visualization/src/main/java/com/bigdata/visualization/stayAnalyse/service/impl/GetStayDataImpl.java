package com.bigdata.visualization.stayAnalyse.service.impl;

import com.bigdata.visualization.stayAnalyse.service.GetStayData;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetStayDataImpl implements GetStayData {

    List<String> pool = new ArrayList<>();

    List<String> data = new ArrayList<>();

    Long time = 0L;
    @KafkaListener(topics = "stayData")
    public void source(String msg)
    {
        String coord = msg.substring(msg.indexOf(":")+1,msg.indexOf("_"));
        String t = coord.substring(1,coord.length()-1).split(",")[2];
        Long current = Long.parseLong(t);
        if(time==0)
        {
            time = current;
        }
        else if(current-time>=3000)
        {
            data.clear();
            pool.forEach(data::add);
            pool.clear();
            time = current;
        }
        else
        {
            pool.add(msg);
        }
    }
    @Override
    public List<String> getStayData() {
        return data;
    }
}
