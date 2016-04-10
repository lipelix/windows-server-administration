package cz.lip.windowsserveradministration;

import android.app.Application;
import android.content.Context;


public class AppController extends Application {

    private static AppController instance;
    public static final String PREF_NAME = "cz.lip.windowsserveradministration";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static AppController getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}