package com.alibaba.alink.service.algorithm.impl;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.pojo.InfoSorted;
import com.alibaba.alink.service.algorithm.Cluster;
import com.alibaba.alink.service.algorithm.DoCluster;
import com.alibaba.alink.service.algorithm.ModifyData;
import com.alibaba.alink.service.dataSource.GetSourceData;
import com.alibaba.alink.service.kafkaOutput.Output;
import com.alibaba.alink.service.outputData.OutputData;
import org.apache.flink.types.Row;

import java.util.HashMap;
import java.util.List;

public class DoClusterImpl implements DoCluster {
    @Override
    public void doCluser(String means, BatchOperator data) {
        Cluster cluster = new ClusterImpl();
        List<Row> rows = cluster.getCluster(means, data);

        ModifyData modifyData = new ModifyDataImpl();

        HashMap<String, List<InfoSorted>> map = modifyData.modifyData(rows, GetSourceData.map);

        Output.outputData(means,map);
    }
}
