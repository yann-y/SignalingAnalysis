package com.alibaba.alink.service.algorithm.impl;

import com.alibaba.alink.service.algorithm.Cluster;
import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.pipeline.clustering.BisectingKMeans;
import com.alibaba.alink.pipeline.clustering.GaussianMixture;
import com.alibaba.alink.pipeline.clustering.KMeans;
import org.apache.flink.types.Row;

import java.util.List;

public class ClusterImpl implements Cluster {
    private final int sortNumber = 5;
    @Override
    public List<Row> getCluster(String means, BatchOperator data) {
        if (means.equals("GaussianMixture")) {
            GaussianMixture gaussianMixture = new GaussianMixture()
                    .setVectorCol("vec")
                    //聚类中心
                    .setK(sortNumber)
                    .setPredictionCol("sort")
                    //聚类选项
                    .setReservedCols("id", "vec")
                    .setMaxIter(3000)
                    .setTol(Math.pow(10, (-8)));
            return gaussianMixture.fit(data).transform(data).collect();
        } else if (means.equals("KMeans")) {
            KMeans kMeans = new KMeans()
                    .setVectorCol("vec")
                    //聚类中心
                    .setK(sortNumber)
                    .setPredictionCol("sort")
                    //聚类选项
                    .setReservedCols("id", "vec")
                    .setMaxIter(3000)
                    .setEpsilon(Math.pow(10, (-15)));
            return kMeans.fit(data).transform(data).collect();
        } else  {
            BisectingKMeans bisectingKMeans = new BisectingKMeans()
                    .setVectorCol("vec")
                    //聚类中心
                    .setK(sortNumber)
                    .setPredictionCol("sort")
                    //聚类选项
                    .setReservedCols("id", "vec")
                    .setMaxIter(200);
            return bisectingKMeans.fit(data).transform(data).collect();
        }

    }
}
