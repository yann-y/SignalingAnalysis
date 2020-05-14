package com.bigdata.visualization.modeSplit.service.impl;

import com.bigdata.visualization.modeSplit.pojo.DataLabled;
import com.bigdata.visualization.modeSplit.service.GetDataLabled;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetDataLabledImpl implements GetDataLabled {

    private List<String> KMeansPool = new ArrayList<>();
    private List<String> KMeans = new ArrayList<>();
    private List<String> GaussianMixturePool = new ArrayList<>();
    private List<String> GaussianMixture = new ArrayList<>();
    private List<String> BisectingKMeansPool = new ArrayList<>();
    private List<String> BisectingKMeans = new ArrayList<>();

    @KafkaListener(topics = "KMeans")
    public void getKMeans(String msg) {

        if (msg.equals("end")) {
            KMeans.clear();
            KMeansPool.forEach(KMeans::add);
            KMeansPool.clear();
        }
        else
        {
            KMeansPool.add(msg);
        }
    }

    @KafkaListener(topics = "GaussianMixture")
    public void getGaussianMixture(String msg) {
        if (msg.equals("end")) {
            GaussianMixture.clear();
            GaussianMixturePool.forEach(GaussianMixture::add);
            GaussianMixturePool.clear();
        }
        else
        {
            GaussianMixturePool.add(msg);
        }
    }

    @KafkaListener(topics = "BisectingKMeans")
    public void getBisectingKMeans(String msg) {
        if (msg.equals("end")) {
            BisectingKMeans.clear();
            BisectingKMeansPool.forEach(BisectingKMeans::add);
            BisectingKMeansPool.clear();
        }
        else
        {
           BisectingKMeansPool.add(msg);
        }
    }

    @Override
    public List<DataLabled> getDataLabled(String means) {

        List<String> list = new ArrayList<>();
        if(means.equals("KMeans"))
        {
            list = KMeans;
        }
        else if(means.equals("GaussianMixture"))
        {

            list = GaussianMixture;
        }
        else
        {
            list = BisectingKMeans;
        }
        List<DataLabled> data = new ArrayList<>();
        list.forEach(c->{
            String method = c.substring(0,c.indexOf(","));
            String coords = c.substring(c.indexOf(",")+1);
            data.add(new DataLabled(method,coords));
        });
        return data;
    }
}
