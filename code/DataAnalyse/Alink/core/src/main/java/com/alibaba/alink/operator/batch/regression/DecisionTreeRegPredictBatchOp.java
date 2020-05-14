package com.alibaba.alink.operator.batch.regression;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.operator.common.tree.predictors.RandomForestModelMapper;
import com.alibaba.alink.operator.batch.utils.ModelMapBatchOp;
import com.alibaba.alink.params.regression.DecisionTreeRegPredictParams;

/**
 *
 */
public final class DecisionTreeRegPredictBatchOp extends ModelMapBatchOp <DecisionTreeRegPredictBatchOp> implements
	DecisionTreeRegPredictParams <DecisionTreeRegPredictBatchOp> {
	public DecisionTreeRegPredictBatchOp() {
		this(null);
	}

	public DecisionTreeRegPredictBatchOp(Params params) {
		super(RandomForestModelMapper::new, params);
	}
}
