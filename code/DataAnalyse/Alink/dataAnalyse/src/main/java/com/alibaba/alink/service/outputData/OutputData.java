package com.alibaba.alink.service.outputData;

import com.alibaba.alink.pojo.InfoSorted;
import com.alibaba.alink.pojo.SortedData;

import java.util.HashMap;
import java.util.List;

public class OutputData {

    public static void outputData(String means, HashMap<String, List<InfoSorted>> map)
    {
        AddData addData = new AddData();
        addData.deleteData(means);
        for (String key : map.keySet()) {

            List<InfoSorted> list = map.get(key);
            String method = list.get(0).getSort();

            StringBuilder builder = new StringBuilder();
            for (InfoSorted infoSorted : list) {
                builder.append("(");
                builder.append(infoSorted.getLongitude());
                builder.append(",");
                builder.append(infoSorted.getLatitude());
                builder.append(",");
                builder.append(infoSorted.getTimeStamp());
                builder.append(")_");
            }
            String info = (builder.delete(builder.length() - 1, builder.length()).toString());
            addData.addData(new SortedData(method, info),means);
        }
    }
}
