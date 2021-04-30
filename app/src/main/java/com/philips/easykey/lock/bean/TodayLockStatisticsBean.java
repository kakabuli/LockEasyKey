package com.philips.easykey.lock.bean;

public class TodayLockStatisticsBean {

    private int statisticsType;
    private int statisticsCount;

    public int getStatisticsType() {
        return statisticsType;
    }

    public void setStatisticsType(int statisticsType) {
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
