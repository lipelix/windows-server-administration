package cz.mowin.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Libor on 13.4.2016.
 */
public class Utils {

    public static boolean isAlphaNumeric(String string) {
        return string.matches("^[a-zA-Z0-9]+$");
    }

    public static boolean isAlphaNumericSpace(String string) {
        return string.matches("^[a-zA-Z0-9_ ]+$");
    }

    public static boolean isAlphaNumericDotSpace(String string) {
        return string.matches("^[a-zA-Z0-9_. ]+$");
    }

    public static boolean isAlphaNumericUnderscore(String string) {
        return string.matches("^[a-zA-Z0-9_]+$");
    }

    public static boolean hasSpace(String string) {
        if (string.length() < 1)
            return true;
        return string.contains(" ");
    }

    public static String mapToString(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String key : map.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            String value = map.get(key);
            try {
                stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
                stringBuilder.append("=");
                stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }

        return stringBuilder.toString();
    }
}
