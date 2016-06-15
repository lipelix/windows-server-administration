package cz.mowin.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Class for validation strings and other util methods.
 */
public class Utils {

    /**
     * Check if string is alphanumeric. Uses regex ^[a-zA-Z0-9]+$
     * @param string input string
     * @return true if string is valid, false otherwise
     */
    public static boolean isAlphaNumeric(String string) {
        return string.matches("^[a-zA-Z0-9]+$");
    }

    /**
     * Check if string is alphanumeric with space. Uses regex ^[a-zA-Z0-9_ ]+$
     * @param string input string
     * @return true if string is valid, false otherwise
     */
    public static boolean isAlphaNumericSpace(String string) {
        return string.matches("^[a-zA-Z0-9_ ]+$");
    }

    /**
     * Check if string is alphanumeric with space or dot. Uses regex ^[a-zA-Z0-9_. ]+$
     * @param string input string
     * @return true if string is valid, false otherwise
     */
    public static boolean isAlphaNumericDotSpace(String string) {
        return string.matches("^[a-zA-Z0-9_. ]+$");
    }

    /**
     * Check if string is alphanumeric with underscore. Uses regex ^[a-zA-Z0-9_]+$
     * @param string input string
     * @return true if string is valid, false otherwise
     */
    public static boolean isAlphaNumericUnderscore(String string) {
        return string.matches("^[a-zA-Z0-9_]+$");
    }

    /**
     * Check if string contains space.
     * @param string input string
     * @return true if string is valid, false otherwise
     */
    public static boolean hasSpace(String string) {
        if (string.length() < 1)
            return true;
        return string.contains(" ");
    }

    /**
     * Convert map collection into string, which is usable in POST requests
     * @param map collection of parameters
     * @return converted string
     */
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
