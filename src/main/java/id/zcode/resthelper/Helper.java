package id.zcode.resthelper;


import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Helper {
    public static <E extends Enum<E>> E lookupEnum(Class<E> e, String id) {
        try {
            E result = Enum.valueOf(e, id);
            return result;
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static int add(int a, int b, int c) {
        return a + b + c;
    }

    public static Date add(Date date, long duration, TimeUnit timeUnit){
        duration = timeUnit.toMillis(duration);
        return new Date(date.getTime() + duration);
    }
}
