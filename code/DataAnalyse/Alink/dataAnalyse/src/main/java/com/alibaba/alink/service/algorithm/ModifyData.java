package com.alibaba.alink.service.algorithm;

import com.alibaba.alink.pojo.Info;
import com.alibaba.alink.pojo.InfoSorted;
import org.apache.flink.types.Row;

import java.util.HashMap;
import java.util.List;

public interface ModifyData {

    public HashMap<String, List<InfoSorted>> modifyData(List<Row> rows,HashMap<String, List<Info>> oldMap);

}
