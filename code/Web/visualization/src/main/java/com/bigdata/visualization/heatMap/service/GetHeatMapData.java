package com.bigdata.visualization.heatMap.service;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GetHeatMapData {


    String getHeatMapDataBySecond();

    List<String> getHeatMapDataByMinute();

    List<String> getHeatMapDataByHour();

}
