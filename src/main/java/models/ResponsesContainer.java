package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResponsesContainer {

    public int indexForSearch;
    public int requestCounter;
    public int sumResponseTimes;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static double AverageResponseTime = 0;
    public static int MaxResponseTime = 0;

    ArrayList<Requests> Container = new ArrayList<>();

    public ResponsesContainer(){
        indexForSearch = requestCounter = sumResponseTimes = 0;
    }

    public ResponsesContainer(List<Requests> list){
        Container = (ArrayList<Requests>)list;
    }


    public void AddToContainer(Requests request) {

        requestCounter++;
        Container.add(request);
    }

    public void updateMetrics(Requests request)
    {
        if(request.responseTime > MaxResponseTime)
            MaxResponseTime = request.responseTime;

        sumResponseTimes += request.responseTime;
        AverageResponseTime = sumResponseTimes/requestCounter;
    }

    public Requests getElement(int index) {
        return Container.get(index);
    }

    public int getSize() {
        return Container.size();
    }
    public void setResponseTime(int index, int response_time) {
        Container.get(index).responseTime = response_time;
    }

    public ArrayList getContainer() {
        return Container;
    }

    public int getStartIndex(String startDate) throws ParseException {

        int startIndex = -1;

        Date start_date = simpleDateFormat.parse(startDate);
        for(; indexForSearch < this.getSize(); indexForSearch++)
        {
            if(this.getElement(indexForSearch).getRecieveDate().equals(start_date) || this.getElement(indexForSearch).getRecieveDate().after(start_date)) {
                startIndex = indexForSearch;
                break;
            }
        }

       return startIndex;
    }

    public int getEndIndex(String endDate) throws ParseException {

        int endIndex = -1;




        Date end_date = simpleDateFormat.parse(endDate);
        for(; indexForSearch < this.getSize(); indexForSearch++)
        {
            if(this.getElement(indexForSearch).getRecieveDate().before(end_date) || this.getElement(indexForSearch).getRecieveDate().equals(end_date)) {
                continue;
            }
            else {
                endIndex = indexForSearch - 1;
                break;
            }
        }

        if(endIndex >= this.getSize())
            endIndex = this.getSize() - 1;

        indexForSearch = 0;

        return endIndex;
    }


    public void CLEANdoNotTouch()
    {
        sumResponseTimes = 0;
        AverageResponseTime = 0;
        requestCounter = 0;
        Container.clear();
    }

}
