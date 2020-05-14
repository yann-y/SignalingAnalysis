package com.alibaba.alink.service.algorithm.impl;

import com.alibaba.alink.Utils.DistanceUtil.DistanceUtil;
import com.alibaba.alink.pojo.InfoSorted;
import com.alibaba.alink.service.dataSource.GetLocate;

import java.util.HashMap;
import java.util.List;

public class CorrectData {


    public static void correctData(HashMap<String, List<InfoSorted>> map) {
        Long startTime = Long.parseLong("20181003060000");
        Long lastTime = Long.parseLong("20181003223000");
        HashMap<String, String> locate = GetLocate.getLocate();
        for (String key : map.keySet()) {
            if (map.get(key).get(0).getSort().equals("驾车")) {
                List<InfoSorted> infoSorteds = map.get(key);
                double count = 0;
                for (InfoSorted infoSorted : infoSorteds) {
                    for (String s : locate.keySet()) {
                        if (locate.get(s).equals("地铁")) {
                            String[] str = s.split("-");
                            double distance = DistanceUtil.getDistance(str[0], str[1], infoSorted.getLongitude(), infoSorted.getLatitude());
                            if (distance < 1500 && Long.parseLong(infoSorted.timeStamp) > startTime && Long.parseLong(infoSorted.getTimeStamp()) < lastTime) {
                                count++;
                                break;
                            }
                        }
                    }
                }
                if (count == infoSorteds.size()) {
                    System.out.println("地铁");
                    map.get(key).forEach(c -> c.setSort("地铁"));
                }
            }
        }
    }

}
