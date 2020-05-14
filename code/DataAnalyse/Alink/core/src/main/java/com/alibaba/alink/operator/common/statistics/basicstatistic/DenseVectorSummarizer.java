package com.alibaba.alink.operator.common.statistics.basicstatistic;

import com.alibaba.alink.common.linalg.DenseVector;
import com.alibaba.alink.common.linalg.Vector;
import com.alibaba.alink.common.linalg.VectorUtil;

import java.util.Arrays;

/**
 * It is summary of dense vector, and uses DenseVector to store intermediate result.
 * It will compute sum, squareSum = sum(x_i*x_i), min, max, normL1.
 * Other statistics value can be calculated from these statistics.
 *
 *  Inheritance relationship as follow:
 *           BaseSummarizer
 *              /       \
 *             /         \
 *   TableSummarizer   BaseVectorSummarizer
 *                       /            \
 *                      /              \
 *        SparseVectorSummarizer    DenseVectorSummarizer
 *
 *  SparseVectorSummarizer is for sparse vector, DenseVectorSummarizer is for dense vector.
 *  It can use toSummary() to get the result BaseVectorSummary.
 */
public class DenseVectorSummarizer extends BaseVectorSummarizer {

    /**
     * sum(x_i)
     */
    public DenseVector sum;

    /**
     * sum(x_i * x_i)
     */
    public DenseVector squareSum;

    /**
     * min(x_i)
     */
    public DenseVector min;

    /**
     * max(x_i)
     */
    public DenseVector max;

    /**
     * l1 norm = sum(abs(x_i))
     */
    public DenseVector normL1;

    /**
     * number of not zero.
     */
    public DenseVector numNonZero;

    /**
     * default constructor, outerProduct is not calculated default.
     */
    DenseVectorSummarizer() {
        this.calculateOuterProduct = false;
    }

    /**
     * if calculateOuterProduct is true, outerProduct will be calculated, i
     * t will be use to calculate correlation and coviance.
     * if calculateOuterProduct is false, outerProduct will not be calculate.
     */
    public DenseVectorSummarizer(boolean calculateOuterProduction) {
        this.calculateOuterProduct = calculateOuterProduction;
    }

    /**
     * update by vector.
     */
    @Override
    public BaseVectorSummarizer visit(Vector vec) {
        if (vec instanceof DenseVector) {
            DenseVector dv = (DenseVector) vec;

            int n = dv.size();

            if (count == 0) {
                init(n);
            }

            count++;

            if (sum.size() >= n) {
                for (int i = 0; i < n; i++) {
                    double value = dv.get(i);
                    if (!Double.isNaN(value)) {
                        if (value < min.get(i)) {
                            min.set(i, value);
                        }

                        if (value > max.get(i)) {
                            max.set(i, value);
                        }

                        if (0 != value) {
                            sum.add(i, value);
                            normL1.add(i, Math.abs(value));
                            squareSum.add(i, value * value);
                            numNonZero.add(i, 1);
                        }
                    }
                }

                if (calculateOuterProduct) {
                    if (outerProduct == null) {
                        outerProduct = dv.outer();
                    } else {
                        for (int i = 0; i < n; i++) {
                            for (int j = 0; j < n; j++) {
                                outerProduct.add(i, j, dv.get(i) * dv.get(j));
                            }
                        }
                    }
                }
                return this;
            } else {
                DenseVectorSummarizer summarizer = new DenseVectorSummarizer(calculateOuterProduct);
                summarizer.visit(vec);
                return VectorSummarizerUtil.merge(summarizer, this);
            }
        } else {
            SparseVectorSummarizer sparseSrt = new SparseVectorSummarizer(calculateOuterProduct);
            sparseSrt.visit(vec);
            return VectorSummarizerUtil.merge(sparseSrt, this);
        }
    }

    /**
     * init members.
     */
    private void init(int n) {
        sum = new DenseVector(n);
        squareSum = new DenseVector(n);
        normL1 = new DenseVector(n);

        double[] minValues = new double[n];
        Arrays.fill(minValues, Double.MAX_VALUE);
        min = new DenseVector(minValues);

        double[] maxValues = new double[n];
        Arrays.fill(maxValues, -Double.MAX_VALUE);
        max = new DenseVector(maxValues);

        numNonZero = new DenseVector(n);
    }

    @Override
    public String toString() {
        StringBuilder sbd = new StringBuilder()
            .append("rowNum: ")
            .append(count)
            .append("\n");

        if (count != 0) {
            sbd.append("sum: ")
                .append(VectorUtil.toString(sum))
                .append("\n")
                .append("squareSum: ")
                .append(VectorUtil.toString(squareSum))
                .append("\n")
                .append("min: ")
                .append(VectorUtil.toString(min))
                .append("\n")
                .append("max: ")
                .append(VectorUtil.toString(max))
                .append("\n")
                .append("normL1: ")
                .append(VectorUtil.toString(normL1));
        }

        return sbd.toString();
    }

    /**
     * get summary result.
     */
    @Override
    public BaseVectorSummary toSummary() {
        DenseVectorSummary summary = new DenseVectorSummary();
        summary.count = count;
        summary.sum = sum;
        summary.squareSum = squareSum;
        summary.normL1 = normL1;
        summary.min = min;
        summary.max = max;

        return summary;
    }

    protected DenseVectorSummarizer copy() {
        DenseVectorSummarizer summarizer = new DenseVectorSummarizer();
        summarizer.count = count;
        summarizer.sum = sum.clone();
        summarizer.squareSum = squareSum.clone();
        summarizer.normL1 = normL1.clone();
        summarizer.min = min.clone();
        summarizer.max = max.clone();
        summarizer.numNonZero = numNonZero.clone();

        if (outerProduct != null) {
            summarizer.outerProduct = outerProduct.clone();
        }

        return summarizer;
    }


}