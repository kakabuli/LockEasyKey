package com.philips.easykey.lock.bean;

public class TodayLockStatisticsBean {

    private String statisticsType;
    private int statisticsCount;

    public String getStatisticsType() {
        return statisticsType;
    }

    public void setStatisticsType(String statisticsType) {
        this.statisticsType = statisticsType;
    }

    public int getStatisticsCount() {
        return statisticsCount;
    }

    public void setStatisticsCount(int statisticsCount) {
        this.statisticsCount = statisticsCount;
    }

    @Override
    public String toString() {
        return "TodayLockStatisticsBean{" +
                "statisticsType='" + statisticsType + '\'' +
                ", statisticsCount=" + statisticsCount +
                '}';
    }
}
