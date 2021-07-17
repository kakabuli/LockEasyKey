package com.philips.easykey.lock.bean;

import java.util.Arrays;

public class SevendayDataStatisticsBean {

    private String statisticsTypeName;

    private int[] ordinateValue ;

    private String[] transverseValue;

    public String getStatisticsTypeName() {
        return statisticsTypeName;
    }

    public void setStatisticsTypeName(String statisticsTypeName) {
        this.statisticsTypeName = statisticsTypeName;
    }

    public int[] getOrdinateValue() {
        return ordinateValue;
    }

    public void setOrdinateValue(int[] ordinateValue) {
        this.ordinateValue = ordinateValue;
    }

    public String[] getTransverseValue() {
        return transverseValue;
    }

    public void setTransverseValue(String[] transverseValue) {
        this.transverseValue = transverseValue;
    }

    @Override
    public String toString() {
        return "SevendayDataStatisticsBean{" +
                "statisticsTypeName='" + statisticsTypeName + '\'' +
                ", ordinateValue=" + Arrays.toString(ordinateValue) +
                ", transverseValue=" + Arrays.toString(transverseValue) +
                '}';
    }
}
