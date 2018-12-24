package controllers;

//import jdk.nashorn.internal.ir.RuntimeNode;

import models.Requests;
import models.ResponsesContainer;
import org.springframework.beans.factory.annotation.Autowired;
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


    ResponsesContainer container = new ResponsesContainer();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    @RequestMapping (value = "/online-logger", method = RequestMethod.POST)
    public void logging(@RequestBody(required = false) String body, HttpServletResponse response)  {

        System.out.println("Have a request\n");
        Date recieveDate = new Date();

        //сохранение
        container.AddToContainer(new Requests(body, recieveDate, -1));

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
        container.setResponseTime(container.getSize() - 1, responseDate.getTime() - recieveDate.getTime());
        container.UpdateMetrics();


        //Отладочные костыли

        System.out.println("//-----------//\n"
                + (int)container.AverageResponseTime + "\n"
                + container.MaxResponseTime + "\n"
                + "Count of logs: " + container.getSize());

        for (int i  = 0; i <  container.getSize(); i++){
            System.out.printf(simpleDateFormat.format(container.getElement(i).getRecieveDate()) + "\n");
        }


      long usedBytes = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1048576;
        System.out.println("Used bytes:"+ usedBytes);
        System.out.printf("Total: " + Runtime.getRuntime().maxMemory()/1048576);
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

            Date start_date = simpleDateFormat.parse(startDate);
            Date end_date = simpleDateFormat.parse(endDate);

            String startDateForInterval = startDate;
            String endDateForInterval = endDate;

            if (interval != "0")
                endDateForInterval= simpleDateFormat.format(
                    simpleDateFormat.parse(startDateForInterval).getTime() + Integer.parseInt(interval)*1000
                );

            ArrayList<ArrayList<Requests>> intervals = new ArrayList<>();
            for(int i = 0; !simpleDateFormat.parse(endDateForInterval).after(end_date); i++){
                int startBufIndex = buffer.getStartIndex(startDateForInterval);
                int endBufIndex = buffer.getEndIndex(endDateForInterval);

                if(startBufIndex >= 0) {

                    if (endBufIndex == -1) {
                        endBufIndex = container.getSize() - 1;
                    }
                }

                intervals.add(new ArrayList(buffer.getContainer().subList(startBufIndex, endBufIndex)));

                if(interval == "0")
                    break;
                else{
                    startDateForInterval = simpleDateFormat.format(
                            simpleDateFormat.parse(startDateForInterval).getTime()+ Integer.parseInt(interval)*1000);
                    endDateForInterval= simpleDateFormat.format(
                            simpleDateFormat.parse(startDateForInterval).getTime() + Integer.parseInt(interval)*1000);
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
