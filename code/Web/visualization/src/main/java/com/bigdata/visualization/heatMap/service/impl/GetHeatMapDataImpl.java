package com.bigdata.visualization.heatMap.service.impl;


import com.bigdata.visualization.heatMap.service.GetHeatMapData;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetHeatMapDataImpl implements GetHeatMapData {

    private String msg = "";


    private List<String> msgByMinutePool = new ArrayList<>();

    private List<String> msgByHourPool  = new ArrayList<>();

    private List<String> msgByMinute = new ArrayList<>();

    private List<String> msgByHour = new ArrayList<>();

    @KafkaListener(topics = "heatMapData")
    public void getData(String msg) {
        this.msg = msg;
        this.msgByMinutePool.add(msg);
        this.msgByHourPool.add(msg);

        if(msgByMinutePool.size() >= 12 )
        {
            msgByMinute.clear();
            msgByMinutePool.forEach(msgByMinute::add);
            msgByMinutePool.clear();
        }
        if(msgByHourPool.size()>=(60 * 60 / 5))
        {
            msgByHour.clear();
            msgByHourPool.forEach(msgByHour::add);
            msgByHourPool.clear();
        }
    }

    @Override
    public String getHeatMapDataBySecond() {
        return msg;
    }


    @Override
    public List<String> getHeatMapDataByMinute() {
        return msgByMinute;
    }

    @Override
    public List<String> getHeatMapDataByHour() {
        return msgByHour;
    }

}
