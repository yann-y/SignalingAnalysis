package com.alibaba.alink.service.dataSource;

import com.alibaba.alink.Utils.DistanceUtil.DistanceUtil;
import com.alibaba.alink.Utils.SpeedUtil.SpeedUtil;
import com.alibaba.alink.Utils.TimeUtil.TimeUtil;
import com.alibaba.alink.pojo.Info;
import com.alibaba.alink.pojo.Value;
import org.apache.flink.types.Row;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetData {

    public static List<Row> getData(ArrayList<String> strs, HashMap<String, List<Info>> map) {

        for (String s : strs) {
            int index = s.indexOf(":");
            String id = s.substring(0, index);
            String[] infos = s.substring(index + 1).split("_");
            ArrayList<Info> temp = new ArrayList<>();

            for (int j = 0; j < infos.length; j++) {
                String[] info = infos[j].substring(1, infos[j].length() - 1).split(",");
                temp.add(new Info(info[0], info[1], info[2]));
            }
            map.put(id, temp);
        }
        HashMap<String, Value> hashMap = new HashMap<>();
        for (String key : map.keySet()) {
            List<Info> temp = map.get(key);
            double time = 0;
            double distance = 0;
            for (int i = 0; i < temp.size() - 1; i++) {
                Info from = temp.get(i);
                Info to = temp.get(i + 1);
                time += TimeUtil.getMinute(from.getTimeStamp(), to.getTimeStamp());

                distance += DistanceUtil.getDistance(from.longitude, from.latitude, to.longitude, to.latitude);
            }
            double speed = SpeedUtil.getSpeedBySecond(time * 60, distance) * 3.6;
            Value value = new Value(String.format("%.2f", time / 60), String.format("%.2f", distance / 1000), String.format("%.2f", speed));
            hashMap.put(key, value);
        }
        List<Row> Rows = new ArrayList<>();

        for (String key : hashMap.keySet()) {
            Row temp = Row.of(key, hashMap.get(key).distance+" "+hashMap.get(key).time+" "+hashMap.get(key).speed);
            Rows.add(temp);
        }
        return Rows;
    }
}
