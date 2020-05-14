package com.alibaba.alink.service.dataSource;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.source.MemSourceBatchOp;
import com.alibaba.alink.pojo.Info;
import org.apache.flink.types.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetSourceData {
    public static HashMap<String,List<Info>> map = new HashMap<>();
    public  static BatchOperator getSourceData(ArrayList<String> list) {
        //获取数据
        List<Row> Rows = GetData.getData(list, map);
        BatchOperator data = new MemSourceBatchOp(Rows, new String[]{"id", "vec"});
        return data;
    }
}
