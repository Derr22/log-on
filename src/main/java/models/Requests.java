package models;

import java.util.Date;

public class Requests {
    String Body;
    Date recieveDate;

    long responseTime;

    public Requests(String body, Date reqDate, long response_time) {
        this.Body = body;
        this.recieveDate = reqDate;
        this.responseTime = response_time;
    }

    public int getResponseTime() {
        return (int)responseTime;
    }

    public String getBody() {
        return Body;
    }

    public Date getRecieveDate() {
        return recieveDate;
    }


}
