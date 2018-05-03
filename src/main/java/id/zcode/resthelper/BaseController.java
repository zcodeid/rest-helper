package id.zcode.resthelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

import java.util.Iterator;

public class BaseController {

    /**
     * Convert query string into java object
     * @param queryString
     * @param clazz
     * @param <T>
     * @return Java Object
     */
    protected <T> T loadValuesOf(String queryString, Class<T> clazz) {
        if (queryString == null) try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return serializeQueryString(queryString, clazz);
    }

    private static <T> T serializeQueryString(String paramIn, Class<T> clazz) {
        JSONObject a = new JSONObject(createObject(paramIn));
        JSONObject jsonObject = new JSONObject(createObject(paramIn));
        Iterator<String> iter = a.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            String value = jsonObject.getString(key);
            if (key.contains("_")) {
                fattening(jsonObject, key, value);
                jsonObject.remove(key);
            } else {
                jsonObject.put(key, value);
            }
        }
        Gson gson = new GsonBuilder().create();
        T p = gson.fromJson(jsonObject.toString(), clazz);
        return p;
    }

    private static String createObject(String paramIn) {
        paramIn = paramIn.replaceAll("=", "\":\"");
        paramIn = paramIn.replaceAll("&", "\",\"");
        paramIn = "{\"" + paramIn + "\"}";
        return paramIn;
    }

    private static void fattening(JSONObject jsonObject, String key, String value) {
        String[] arr = key.split("_");
        String k = arr[0];
        String v = arr[1];
        JSONObject o = jsonObject.isNull(k) ?
                new JSONObject() :
                jsonObject.getJSONObject(k);
        if (arr.length > 2) {
            jsonObject.put(k, o);
            String newKey = key.substring(key.indexOf("_") + 1, key.length());
            fattening(o, newKey, value);
        } else {
            o.put(v, value);
            jsonObject.put(k, o);
        }
    }

    private enum FieldType {
        INT, DOUBLE, BYTE, SHORT, LONG, FLOAT, BOOLEAN, CHAR, JAVALANGSTRING, JAVAUTILUUID;

        static public FieldType lookup(String id) {
            return Helper.lookupEnum(FieldType.class, id);
        }
    }
}
