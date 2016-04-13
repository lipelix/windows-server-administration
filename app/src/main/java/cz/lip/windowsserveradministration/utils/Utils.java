package cz.lip.windowsserveradministration.utils;

/**
 * Created by Libor on 13.4.2016.
 */
public class Utils {

    public static boolean isAlphaNumeric(String string) {
        return string.matches("[a-zA-Z0-9]+");
    }

    public static boolean isAlphaNumericSpace(String string) {
        return string.matches("[a-zA-Z0-9 ]+");
    }
}
