package controllers;

//import jdk.nashorn.internal.ir.RuntimeNode;
import models.Requests;
import models.ResponsesContainer;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RestController
public class MainController {

    ResponsesContainer container = new ResponsesContainer();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @PostMapping (value = "/online-logger")
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
        for (int i = 0; i< container.getSize(); i++) {
            System.out.println("body: " + container.getElement(i).getBody()
                    + "recieveDate: " + container.getElement(i).getRecieveDate()
                    + "\nresponseTime: " + container.getElement(i).getResponseTime());
        }

        System.out.println("//-----------//\n"
                + (int)container.AverageResponseTime + "\n"
                + container.MaxResponseTime + "\n");

      //  long usedBytes = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1048576;
    }

    @GetMapping (value = "/online-logger")
    public void getLogs(@RequestBody(required = false) String body,
                        @RequestParam("startDate") String startDate,
                        @RequestParam("endDate") String endDate,
                        @RequestParam("interval") String interval,
                        HttpServletResponse response) throws ParseException {


        System.out.println("Get");
        int index = 0;

        int startIndex = -1;
        int endIndex = -1;

        Date start_date = simpleDateFormat.parse(startDate);

        if(startIndex >= 0){

            index = startIndex;

            if(endIndex == -1){
                endIndex = container.getSize() - 1;
            }
            List<Requests> buffer = new ArrayList<>();
            buffer = container.getContainer().subList(startIndex, endIndex);

            Date startDateForInterval = start_date;
            Date endDateForInterval = new Date(startDateForInterval.getTime() + Integer.parseInt(interval)*60000);



            response.setHeader("Content-Transfer-Encoding", "UTF-8");;
            response.setStatus(200);
            try {
                response.getWriter().write(startIndex + "\n" + endIndex);
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
