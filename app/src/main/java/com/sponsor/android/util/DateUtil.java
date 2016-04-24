package com.sponsor.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ouarea on 2015/12/18.
 */
public class DateUtil {

    public static Date getDate(String date, String format) {
        if (null == format) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static String format(Date date, String format) {
        if (null == format) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String convert(String date, String sourceFormat, String destFormat) {
        return format(getDate(date, sourceFormat), destFormat);
    }
}
