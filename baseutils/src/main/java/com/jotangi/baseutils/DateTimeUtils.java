package com.jotangi.baseutils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    public final static String DATEONLY = "yyyy-MM-dd";
    public final static String DATETIME24 = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentTimeString(String df) {
        SimpleDateFormat sdf = new SimpleDateFormat(df, Locale.getDefault());
        return sdf.format(new Date());
    }

}
