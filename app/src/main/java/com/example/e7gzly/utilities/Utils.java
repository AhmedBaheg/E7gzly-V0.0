package com.example.e7gzly.utilities;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String CALCULATE_LEAVE_TIME(String arrive_time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        Date arrive = null;
        Date waiting_time = null;
        try {
            arrive = dateFormat.parse(arrive_time);
            waiting_time = dateFormat.parse("00:15");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date leave_time = new Date(arrive.getTime() + waiting_time.getTime());
        return dateFormat.format(leave_time);
    }

}
