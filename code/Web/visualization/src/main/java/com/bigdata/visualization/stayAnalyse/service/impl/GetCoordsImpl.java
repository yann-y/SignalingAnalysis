package com.bigdata.visualization.stayAnalyse.service.impl;

import com.bigdata.visualization.heatMap.pojo.Coord;
import com.bigdata.visualization.stayAnalyse.service.GetCoords;
import com.bigdata.visualization.stayAnalyse.service.GetStayData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetCoordsImpl implements GetCoords {


    @Autowired
    private GetStayData getStayData;


    @Override
    public List<Coord> getCoords() {

        List<String> source = getStayData.getStayData();

        List<Coord> coords = new ArrayList<>();
        source.forEach(s -> {
            String coord = s.substring(s.indexOf(":") + 1);

            String[] cs = coord.split("_");

            for (int i = 0; i < cs.length; i++) {
                String[] tmep = cs[i].substring(1, cs[i].length() - 1).split(",");
                coords.add(new Coord(tmep[2], 10, Double.parseDouble(tmep[0]), Double.parseDouble(tmep[1])));
            }
        });

        return coords;

    }
}
