package cz.lip.windowsserveradministration;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;


public class AppController extends Application {

    private static AppController instance;
    public static final String PREF_NAME = "cz.lip.windowsserveradministration";
    public static SharedPreferences pref;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        pref = this.getSharedPreferences(AppController.PREF_NAME, getAppContext().MODE_PRIVATE);
    }

    public static AppController getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

    public static void save(String key, String value) {
        pref.edit().putString(key, value).commit();
    }

    public static void saveLong(String key, Long value) {
        pref.edit().putLong(key, value).commit();
    }

    public static String load(String key) {
       return pref.getString(key, "-");
    }

    public static Long loadLong(String key) {
        return pref.getLong(key, Long.MAX_VALUE);
    }

    public static SharedPreferences getPref() {
        return pref;
    }

    public static void invalidateToken() {
        AppController.save("access_token", "-");
        AppController.saveLong("access_token_expires", Long.MIN_VALUE);
    }
}