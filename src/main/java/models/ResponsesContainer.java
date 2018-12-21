package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ResponsesContainer {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static double AverageResponseTime = 0;
    public static int MaxResponseTime = 0;

    ArrayList<Requests> Container = new ArrayList<>();

    public ResponsesContainer(){ }

    public void AddToContainer(Requests request) {
        Container.add(request);
    }

    public void UpdateMetrics() {
        AverageResponseTime = Container.stream().mapToInt(Requests::getResponseTime).filter(r -> r>0).average().getAsDouble();
        MaxResponseTime = Container.stream().mapToInt(Requests::getResponseTime).filter(r -> r>0).max().getAsInt();
    }

    public Requests getElement(int index)
    {
        return Container.get(index);
    }

    public int getSize()
    {
        return Container.size();
    }
    public void setResponseTime(int index, long response_time)
    {
        Container.get(index).responseTime = response_time;
    }

    public ArrayList getContainer()
    {
        return Container;
    }

//    public getIndexesOfFitsStrings(String startDate, ) throws ParseException {
//        int index = 0;
//
//        int startIndex = -1;
//        int endIndex = -1;
//
//        Date start_date = simpleDateFormat.parse(startDate);
//        for(; index < this.getSize(); index++)
//        {
//            if(this.getElement(index).getRecieveDate().equals(start_date) || this.getElement(index).getRecieveDate().after(start_date)) {
//                startIndex = index;
//                break;
//            }
//        }
//
//        Date end_date = simpleDateFormat.parse(endDate);
//        for(; index < container.getSize(); index++)
//        {
//            if(container.getElement(index).getRecieveDate().before(end_date) || container.getElement(index).getRecieveDate().equals(end_date)) {
//                continue;
//            }
//            else {
//                endIndex = index - 1;
//            }
//        }
//    }
}
