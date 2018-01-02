package com.mangonon.johnry.popularmovieapp.utils;

import android.content.Context;
import android.content.res.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Helper {

    public static String getYear(String stringDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = simpleDateFormat.parse(stringDate);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return String.valueOf(gc.get(Calendar.YEAR));
    }

    public static boolean isLandscape(Context context) {
        int orientation  = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return  true;
        }
        return false;
    }

}
