package com.idyllix.bol_tech.utils;

import android.content.Context;
import android.content.res.Resources;

import com.idyllix.bol_tech.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GetCurrentDate {

    public  static String getNow(){
        SimpleDateFormat sdf =new SimpleDateFormat("dd.MM.yyyy", new Locale("tr"));
        return sdf.format(new Date());
    }

    public  static String getToday(){
        SimpleDateFormat sdf =new SimpleDateFormat("dd-MM-yyyy ", new Locale("tr"));
        return sdf.format(new Date());
    }

    public  static String getThisTime(){
        SimpleDateFormat sdf =new SimpleDateFormat("dd.MM.yyyy kk:mm:ss", new Locale("tr"));
        return sdf.format(new Date());
    }

    public  static String getThisTimeSQL(){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-M-d kk:mm:ss", new Locale("tr"));
        return sdf.format(new Date());
    }

    public static String getDuration(String beginingTime, String finishingTime, Context context){
        Resources resources= context.getResources();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss", new Locale("tr"));

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(finishingTime);
            d2 = format.parse(beginingTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = 0;
        if (d2 != null&&d1!=null) {
            diff = d1.getTime() - d2.getTime();
        }
        long diffSeconds = (diff / 1000)%60;
        long diffMinutes = (diff / (60 * 1000))%60;
        long diffHours = (diff / (60 * 60 * 1000))%24;
        //long diffInDays = diff / (1000 * 60 * 60 * 24);

        return String.format(resources.getString(R.string.duration_place_holder),
                diffHours, diffMinutes, diffSeconds);
    }

    public static String dateConvertToString(Date date){
        SimpleDateFormat sdf =new SimpleDateFormat("dd.MM.yyyy kk:mm:ss", new Locale("tr"));
        return sdf.format(date);
    }

}
