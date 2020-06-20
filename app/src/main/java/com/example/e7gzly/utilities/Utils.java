package com.example.e7gzly.utilities;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static String CALCULATE_LEAVE_TIME(String arrive_time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date arrive = null;
        try {
            arrive = dateFormat.parse(arrive_time);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(arrive);
        calendar.add(Calendar.MINUTE,15);

        return dateFormat.format(calendar.getTime());
    }

}
