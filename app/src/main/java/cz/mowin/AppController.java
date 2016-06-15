package cz.mowin;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;

/**
 * Class for maintain state of application. Provides methods for operating with application data store.
 * @author Libor Vachal
 */
public class AppController extends Application {

    private static AppController instance;
    public static final String PREF_NAME = "cz.mowin";
    public static final String CERT_FILE_NAME = "certificate.pfx";
    private static SharedPreferences pref;

    /**
     * On application start save instance a create data store
     */
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        pref = this.getSharedPreferences(AppController.PREF_NAME, getAppContext().MODE_PRIVATE);
    }

    /**
     * Get application instance
     * @return application instance
     */
    public static AppController getInstance() {
        return instance;
    }

    /**
     * Get application context
     * @return application context
     */
    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

    /**
     * Method for hiding keyboard from activity
     * @param activity current activity, where keyboard is shown
     */
    public static void hideKeyboardFrom(Activity activity) {
        if (activity == null)
            return;
        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * Save string data to datastore
     * @param key data key
     * @param value data value
     */
    public static void save(String key, String value) {
        pref.edit().putString(key, value).commit();
    }

    /**
     * Save numeric data to datastore
     * @param key data key
     * @param value data value
     */
    public static void saveLong(String key, Long value) {
        pref.edit().putLong(key, value).commit();
    }

    /**
     * Get string data from datastore
     * @param key data key
     * @return data value
     */
    public static String load(String key) {
       return pref.getString(key, "-");
    }

    /**
     * Get numeric data from datastore
     * @param key data key
     * @return data value
     */
    public static Long loadLong(String key) {
        return pref.getLong(key, Long.MAX_VALUE);
    }

    /**
     * Get shared preferences
     * @return shared preferences
     */
    public static SharedPreferences getPref() {
        return pref;
    }

    /**
     * Invalidate authorization token
     */
    public static void invalidateToken() {
        AppController.save("access_token", "-");
        AppController.saveLong("access_token_expires", Long.MIN_VALUE);
    }
}