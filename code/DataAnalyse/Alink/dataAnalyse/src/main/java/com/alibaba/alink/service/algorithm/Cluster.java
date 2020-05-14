package com.alibaba.alink.service.algorithm;

import com.alibaba.alink.operator.batch.BatchOperator;
import org.apache.flink.types.Row;

import java.util.List;

public interface Cluster {

    public List<Row> getCluster(String means, BatchOperator data);

}
