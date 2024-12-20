package com.example.testcarte;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {

    private final static SimpleDateFormat sdf =new SimpleDateFormat("dd.MM.YYYY", Locale.US);

    public static Date toDate(String s)
    {
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(Date d)
    {
        if(d==null)
        {
            return null;
        }
        return sdf.format(d);
    }
}
