package com.bigdata.visualization.heatMap.service;


import com.bigdata.visualization.heatMap.pojo.Coord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GetCoordData {


    List<Coord> getCoordDataBySecond();


    List<Coord> getCoordDataByMinute();


    List<Coord> getCoordDataByHour();

}
