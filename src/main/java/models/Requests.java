package models;

import java.util.Date;

public class Requests {
    String requesName;
    Date recieveDate;

    int responseTime;

    public Requests(String reqName, Date reqDate, int response_time) {
        this.requesName = reqName;
        this.recieveDate = reqDate;
        this.responseTime = response_time;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public String getRequesName() {
        return requesName;
    }

    public Date getRecieveDate() {
        return recieveDate;
    }


}
