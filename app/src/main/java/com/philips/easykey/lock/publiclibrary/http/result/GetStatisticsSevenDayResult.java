package com.philips.easykey.lock.publiclibrary.http.result;

import java.util.List;

public class GetStatisticsSevenDayResult extends BaseResult{

    /**
     {
     "code": "200",
     "msg": "成功",
     "nowTime": 1564473520,
     "data": {
     "statisticsList": [{
     "date": "2021-04-29",
     "openLockCount": 10,
     "doorbellCount": 3,
     "alarmCount": 3
     }
     ]}
     }
     */

    private int nowTime;

    private DataBean data;

    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setNowTime(int nowTime){
        this.nowTime = nowTime;
    }
    public int getNowTime(){
        return this.nowTime;
    }
    public void setData(DataBean data){
        this.data = data;
    }
    public DataBean getData(){
        return this.data;
    }

    public static class DataBean {
        private List<StatisticsList> statisticsList;

        public void setStatisticsList(List<StatisticsList> statisticsList){
            this.statisticsList = statisticsList;
        }
        public List<StatisticsList> getStatisticsList(){
            return this.statisticsList;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "statisticsList=" + statisticsList +
                    '}';
        }
    }

    public static class StatisticsList
    {
        private String date;

        private int operationCount;

        private int doorbellCount;

        private int alarmCount;

        public void setDate(String date){
            this.date = date;
        }
        public String getDate(){
            return this.date;
        }

        public int getOperationCount() {
            return operationCount;
        }

        public void setOperationCount(int operationCount) {
            this.operationCount = operationCount;
        }

        public void setDoorbellCount(int doorbellCount){
            this.doorbellCount = doorbellCount;
        }
        public int getDoorbellCount(){
            return this.doorbellCount;
        }
        public void setAlarmCount(int alarmCount){
            this.alarmCount = alarmCount;
        }
        public int getAlarmCount(){
            return this.alarmCount;
        }

        @Override
        public String toString() {
            return "StatisticsList{" +
                    "date='" + date + '\'' +
                    ", operationCount=" + operationCount +
                    ", doorbellCount=" + doorbellCount +
                    ", alarmCount=" + alarmCount +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetStatisticsSevenDayResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", nowTime=" + nowTime +
                ", data=" + data +
                '}';
    }
}
