package com.bigdata.visualization.modeSplit.controller;


import com.bigdata.visualization.modeSplit.pojo.AverData;
import com.bigdata.visualization.modeSplit.service.GetAverData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/modeSpilt")
public class ModeSplitController {

    @Autowired
    private GetAverData getAverData;

    @RequestMapping("/GaussianMixture.html")
    public ModelAndView getMethodData() {
        ModelAndView mv = new ModelAndView();
        List[] averData = getAverData.getAverData("GaussianMixture");
        mv.addObject("averData", averData);
        mv.setViewName("modesplit/GaussianMixture.html");
        return mv;
    }

    @RequestMapping("/Kmeans.html")
    public ModelAndView getKMeansData() {
        ModelAndView mv = new ModelAndView();
        List[] averData = getAverData.getAverData("KMeans");
        mv.addObject("averData", averData);
        mv.setViewName("modesplit/KMeans.html");
        return mv;
    }

    @RequestMapping("/BisectingKMeans.html")
    public ModelAndView getBisectingKMeansData() {
        ModelAndView mv = new ModelAndView();
        List[] averData = getAverData.getAverData("BisectingKMeans");
        mv.addObject("averData", averData);
        mv.setViewName("modesplit/BisectingKMeans.html");
        return mv;
    }

    @RequestMapping("/BisectingKMeans")
    @ResponseBody
    public List[] getBisectingKMeans() {

        List[] averData = getAverData.getAverData("BisectingKMeans");
        return averData;
    }

    @RequestMapping("/KMeans")
    @ResponseBody
    public List[] getKMeans() {
        List[] averData = getAverData.getAverData("KMeans");
        return averData;
    }

    @ResponseBody
    @RequestMapping("/GaussianMixture")
    public List[] getGaussianMixture() {

        List[] averData = getAverData.getAverData("GaussianMixture");
        return averData;
    }

}
