package com.bigdata.visualization.stayAnalyse.service;

import com.bigdata.visualization.heatMap.pojo.Coord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GetCoords {


    List<Coord> getCoords();
}
