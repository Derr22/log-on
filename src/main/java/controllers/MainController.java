package controllers;

import models.Requests;
import models.ResponsesContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RestController
public class MainController {

    @Autowired
    SpringTemplateEngine messageTemplateEngine;

    int iterateForName = 0;


    ResponsesContainer container = new ResponsesContainer();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @RequestMapping (value = "/online-logger", method = RequestMethod.POST)
    public void logging(@RequestBody(required = false) String body, HttpServletResponse response) throws InterruptedException {

        System.out.println("Have a request\n");
        Date recieveDate = new Date();

        //сохранение
        String name = "name" + iterateForName;
        container.AddToContainer(new Requests(name, recieveDate, -1));
        iterateForName++;

        //ответ
        response.setHeader("Content-Transfer-Encoding", "UTF-8");;
        response.setStatus(200);
        try {
            response.getWriter().write("Logging is Okay");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //Добавление времени ответа
        Date responseDate = new Date();
        int responseTime = (int)(responseDate.getTime() - recieveDate.getTime());
        container.setResponseTime(container.getSize() - 1, responseTime);
        container.updateMetrics(container.getElement(container.getSize()-1));



        //Отладочные костыли
        for (int i  = 0; i <  container.getSize(); i++){
            System.out.printf(simpleDateFormat.format(container.getElement(i).getRecieveDate()) + "\n");
        }

        System.out.println("//-----------//\n"
                + "Average: " + (int)container.AverageResponseTime + "\n"
                + "Max: " + container.MaxResponseTime + "\n"
                + "Count of logs: " + container.getSize());

        System.out.println("Used Memory:"+ Runtime.getRuntime().totalMemory()/1048576);
        System.out.println("Max Memory: " + Runtime.getRuntime().maxMemory()/1048576);
    }

    @GetMapping (value = "/online-logger")
    public void getLogs(@RequestParam("startDate") String startDate,
                        @RequestParam("endDate") String endDate,
                        @RequestParam("interval") String interval,
                        HttpServletResponse response) throws ParseException {

        int startIndex = -1;
        int endIndex = -1;

        try{
            startIndex = container.getStartIndex(startDate);
            endIndex = container.getEndIndex(endDate);
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        if(startIndex >= 0){

            if(endIndex == -1){
                endIndex = container.getSize() - 1;
            }

            ResponsesContainer buffer = new ResponsesContainer(new ArrayList(container.getContainer().subList(startIndex, endIndex)));

            Date end_date = simpleDateFormat.parse(endDate);

            String startDateForInterval = startDate;
            String endDateForInterval = endDate;

            if (!"all".equals(interval)) {
                endDateForInterval = simpleDateFormat.format(
                        simpleDateFormat.parse(startDateForInterval).getTime() + Integer.parseInt(interval) * 60000
                );
            }

            ArrayList<ArrayList<Requests>> intervals = new ArrayList<>();
            for(; !simpleDateFormat.parse(endDateForInterval).after(end_date);){
                int startBufIndex = buffer.getStartIndex(startDateForInterval);
                int endBufIndex = buffer.getEndIndex(endDateForInterval);

                if(startBufIndex >= 0) {

                    if (endBufIndex == -1 ) {
                        endBufIndex = buffer.getSize() - 1;
                    }


                    ArrayList<Requests> temp = new ArrayList<>();
                    for (int i = startBufIndex; i <= endBufIndex; i++)
                    {
                        temp.add(buffer.getElement(i));
                    }
                    if(!temp.isEmpty())
                        intervals.add(temp);


                    if(interval.equals("all"))
                        break;
                    else{
                        startDateForInterval = simpleDateFormat.format(
                                simpleDateFormat.parse(startDateForInterval).getTime()+ Integer.parseInt(interval)*60000);
                        endDateForInterval= simpleDateFormat.format(
                                simpleDateFormat.parse(startDateForInterval).getTime() + Integer.parseInt(interval)*60000);
                }


                }
            }

            Context context = new Context();
            context.setVariable("intervals", intervals);
            String content = messageTemplateEngine.process("xml/getLogs", context);

            response.setHeader("Content-Transfer-Encoding", "UTF-8");;
            response.setStatus(200);
            try {
                response.getWriter().write(content);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Nothing to parse");
        }





    }




}
