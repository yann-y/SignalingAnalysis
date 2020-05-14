package com.bigdata.visualization.modeSplit.service.impl;


import com.bigdata.visualization.Utils.DistanceUtil.DistanceUtil;
import com.bigdata.visualization.Utils.TimeUtil.TimeUtil;
import com.bigdata.visualization.heatMap.pojo.Coord;
import com.bigdata.visualization.modeSplit.pojo.AverData;
import com.bigdata.visualization.modeSplit.pojo.ModeData;
import com.bigdata.visualization.modeSplit.service.GetAverData;
import com.bigdata.visualization.modeSplit.service.GetModeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GetAverDataImpl implements GetAverData {

    @Autowired
    private GetModeData getModeData;

    @Override
    public List[] getAverData(String means) {

        List<ModeData> modeData = getModeData.getModeData(means);

        HashMap<String, List<AverData>> averData = new HashMap<>();

        modeData.forEach(md -> {
            List<Coord> coords = md.getCoords();
            double distance = 0;
            double time = 0;
            double averSpeed = 0;
            for (int i = 0; i < coords.size() - 1; i++) {
                Coord from = coords.get(i);

                Coord to = coords.get(i + 1);

                time += TimeUtil.getSecond(from.getTimeStamp(), to.getTimeStamp());

                distance += DistanceUtil.getDistance(from.getLongitude() + "", from.getLatitude() + "", to.getLongitude() + "", to.getLatitude() + "");

            }
            averSpeed = distance / time;
            AverData temp = new AverData(md.getMethod(), String.format("%.2f", distance / 1000), String.format("%.2f", time / 60), String.format("%.2f", averSpeed * 3.6));
            if (averData.get(temp.getMethod()) != null) {
                averData.get(temp.getMethod()).add(temp);
            } else {
                List<AverData> ad = new ArrayList<>();
                ad.add(temp);
                averData.put(temp.getMethod(), ad);
            }

        });
        List[] lists = new List[averData.size()];
        int index = 0;

        for (String key : averData.keySet()) {
            lists[index++] = averData.get(key);
        }
        return lists;
    }
}
