package com.example.rifkinc196finalproject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static long todayLong() {
        String currentDate = DateUtil.dateFormat.format(new Date());
        return getDateTimestamp(currentDate);
    }

    public static long todayLongWithTime() {
        return System.currentTimeMillis();
    }
    public static long getDateTimestamp(String dateInput) {
        try {
            Date date = DateUtil.dateFormat.parse(dateInput + " MST");
            return date.getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd z", Locale.getDefault());
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS z", Locale.getDefault());
}
