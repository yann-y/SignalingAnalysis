package com.alibaba.alink.service.algorithm.impl;

import com.alibaba.alink.Utils.DistanceUtil.DistanceUtil;
import com.alibaba.alink.pojo.Info;
import com.alibaba.alink.pojo.InfoSorted;
import com.alibaba.alink.service.dataSource.GetLocate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetRate {


    public static void getRate(String method, HashMap<String, List<InfoSorted>> newMap) {
        HashMap<String, String> locate = GetLocate.getLocate();


        double discount = 0;
        for (String key : newMap.keySet()) {
            if (newMap.get(key).get(0).getSort().equals(method)) {
                List<InfoSorted> infoSorteds = newMap.get(key);
                double count = 0;
                List<Info> temp = new ArrayList<>();
                for (InfoSorted infoSorted : infoSorteds) {
                    double min = Double.MAX_VALUE;
                    String lon = "";
                    String lat = "";
                    for (String s : locate.keySet()) {
                        if (locate.get(s).equals("公交")) {
                            String[] str = s.split("-");
                            double distance = DistanceUtil.getDistance(str[0], str[1], infoSorted.getLongitude(), infoSorted.getLatitude());
                            if (distance < min) {
                                min = distance;
                                lon = str[0];
                                lat = str[1];
                                Info info = new Info(lon, lat, "");
                                temp.add(info);
                            }
                        }
                    }
                }
                boolean flag = true;
                for (int i = 0; i < temp.size() - 1; i++) {
                    Info from = temp.get(i);
                    Info to = temp.get(i + 1);
                    if (DistanceUtil.getDistance(from.getLongitude(), from.getLatitude(), to.getLongitude(), to.getLatitude()) > 15000)
                    {
                        flag = false;
                    }
                }
                if (!flag) {
                    infoSorteds.forEach(c -> c.setSort("驾车"));
                }
            }
        }
    }

}
