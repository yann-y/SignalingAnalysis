package com.bigdata.visualization.modeSplit.service.impl;

import com.bigdata.visualization.heatMap.pojo.Coord;
import com.bigdata.visualization.modeSplit.pojo.DataLabled;
import com.bigdata.visualization.modeSplit.pojo.ModeData;
import com.bigdata.visualization.modeSplit.service.GetDataLabled;
import com.bigdata.visualization.modeSplit.service.GetModeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class GetModeDataImpl implements GetModeData {

    @Autowired
    private GetDataLabled getDataLabled;


    @Override
    public List<ModeData> getModeData(String means) {
        List<DataLabled> dataLabled = getDataLabled.getDataLabled(means);

        List<ModeData> methodData = new ArrayList<>();
        dataLabled.forEach(s -> {
            ModeData md = new ModeData();
            md.setMethod(s.getMethod());
            String[] tempCoords = s.getCoords().split("_");
            List<Coord> coords = new ArrayList<>();
            for (int i = 0; i < tempCoords.length; i++) {
                String[] tmp = tempCoords[i].substring(1, tempCoords[i].length() - 1).split(",");
                Coord coord = new Coord();
                coord.setLongitude(Double.parseDouble(tmp[0]));
                coord.setLatitude(Double.parseDouble(tmp[1]));
                coord.setTimeStamp(tmp[2]);
                coords.add(coord);
            }
            md.setCoords(coords);
            methodData.add(md);
        });
        return methodData;

    }
}
