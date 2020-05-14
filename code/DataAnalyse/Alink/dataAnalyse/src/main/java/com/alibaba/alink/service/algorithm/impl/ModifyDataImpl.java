package com.alibaba.alink.service.algorithm.impl;

import com.alibaba.alink.service.algorithm.ModifyData;
import com.alibaba.alink.pojo.Info;
import com.alibaba.alink.pojo.InfoSorted;
import com.alibaba.alink.pojo.Result;
import org.apache.flink.types.Row;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ModifyDataImpl implements ModifyData {


    private final int sortNumber = 5;

    @Override
    public HashMap<String, List<InfoSorted>> modifyData(List<Row> rows, HashMap<String, List<Info>> oldMap) {
        List[] datas = new List[sortNumber];
        HashMap<String, List<InfoSorted>> newMap = new HashMap<>();
        for (String key : oldMap.keySet()) {
            List<Info> temp = oldMap.get(key);

            List<InfoSorted> value = new ArrayList<>();
            for (int i = 0; i < temp.size(); i++) {
                value.add(new InfoSorted(temp.get(i)));
            }
            newMap.put(key, value);
        }

        for (int i = 0; i < datas.length; i++) {
            List<Result> temp = new ArrayList<>();

            datas[i] = temp;
        }
        double[] averSpeed = new double[datas.length];
        for (Row r : rows) {
            String id = r.getField(0).toString();

            String[] vec = r.getField(1).toString().split(" ");
            Double distance = Double.parseDouble(vec[0]);
            Double time = Double.parseDouble(vec[1]);
            Double speed = Double.parseDouble(vec[2]);
            Integer sort = Integer.parseInt(r.getField(2).toString());
            datas[sort].add(new Result(id, time, distance, speed, sort));
            averSpeed[sort] += speed;
        }
        HashMap<Integer, String> lable = new HashMap<>();
        for (int i = 0; i < averSpeed.length; i++) {
            averSpeed[i] /= datas[i].size();
        }

        List<Double> tempList = new ArrayList<>();
        for(int i=0;i<averSpeed.length;i++)
        {
            tempList.add(averSpeed[i]);
        }

        Collections.sort(tempList);

        for(int i=0;i<tempList.size();i++)
        {
            double temp = tempList.get(i);
            for(int j=0;j<averSpeed.length;j++)
            {
                if(averSpeed[j]==temp)
                {
                    if(i==0)
                    {
                        lable.put(j,"步行");
                    }
                    else if(i==1)
                    {
                        lable.put(j,"自行车");
                    }
                    else if(i==2)
                    {
                        lable.put(j,"公交");
                    }
                    else if(i==3)
                    {
                        lable.put(j,"驾车");
                    }
                    else if(i==4)
                    {
                        lable.put(j,"驾车");
                    }
                }
            }
        }
        for(int i=0;i<datas.length;i++)
        {
            for(int j=0;j<datas[i].size();j++)
            {
                Result result = (Result) datas[i].get(j);
                newMap.get(result.getId()).forEach(c->c.setSort(lable.get(result.getSort())));
            }
        }


        int length = 0;
        for (int i = 0; i < datas.length; i++) {
            if (datas[i].size() > length) {
                length = datas[i].size();
            }
        }
        int index = 0;
        for (int i = 0; i < sortNumber; i++) {
            if (i != sortNumber - 1) {
                System.out.print(i + 1 + "\t\t\t");
            } else {
                System.out.println(i + 1);
            }
        }
        while (index < length) {
            for (int i = 0; i < datas.length; i++) {
                if (index >= datas[i].size()) {
                    System.out.print("\t\t\t");
                } else {
                    Result temp = (Result) datas[i].get(index);
                    System.out.print(String.format("%.2f", temp.getSpeed()) + "\t\t");
                }
            }
            System.out.println();
            index++;
        }
        for (int i = 0; i < averSpeed.length; i++) {
            System.out.print(String.format("%.2f", averSpeed[i]) + "\t\t");
        }
        System.out.println();

        //地铁获取
        CorrectData.correctData(newMap);
        //公交车矫正
        GetRate.getRate("公交",newMap);

        return newMap;
    }
}
