package com.bigdata.visualization.heatMap.service.impl;

import com.bigdata.visualization.heatMap.pojo.Coord;
import com.bigdata.visualization.heatMap.service.GetCoordData;
import com.bigdata.visualization.heatMap.service.GetHeatMapData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GetCoordDataImpl implements GetCoordData {

    @Autowired
    private GetHeatMapData getHeatMapData;

    @Override
    public List<Coord> getCoordDataBySecond() {

        String msg = getHeatMapData.getHeatMapDataBySecond();
        List<Coord> coords = new ArrayList<>();
        process(msg, coords);
        return coords;
    }

    @Override
    public List<Coord> getCoordDataByMinute() {
        List<String> msg = getHeatMapData.getHeatMapDataByMinute();
        List<Coord> coords = new ArrayList<>();
        msg.forEach(m -> process(m, coords));
        return coords;
    }

    @Override
    public List<Coord> getCoordDataByHour() {
        List<String> msg = getHeatMapData.getHeatMapDataByHour();
        List<Coord> coords = new ArrayList<>();
        msg.forEach(m->process(m,coords));
        return coords;

    }


    public void process(String msg, List<Coord> coords) {
        if (msg.length() > 1) {
            String[] tmp = msg.split("_");
            Arrays.stream(tmp).forEach(c -> {
                String[] coord = c.substring(1, c.length() - 1).split(",");
                Coord cd = new Coord(coord[0], 10, Double.parseDouble(coord[2]), Double.parseDouble(coord[3]));
                coords.add(cd);
            });
        }
    }

}
