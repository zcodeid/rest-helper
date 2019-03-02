package id.zcode.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    // ISO 8601 format date => https://en.m.wikipedia.org/wiki/ISO_8601
    public static Date toDate(String inDate) {
        String[] formats = {
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd HH:mm",
                "yyyy-MM-dd"
        };
        for (String f : formats) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(f);
            dateFormat.setLenient(false);
            try {
                return dateFormat.parse(inDate.trim());
            } catch (ParseException pe) {
            }
        }
        return null;
    }

    public static Boolean toBoolean(String s) {
        if (s.equalsIgnoreCase("true")) return true;
        else if (s.equalsIgnoreCase("false")) return false;
        else return null;
    }

}
