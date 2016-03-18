package cz.lip.windowsserveradministration.communication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import cz.lip.windowsserveradministration.AppController;
import cz.lip.windowsserveradministration.BuildConfig;

/**
 * Created by Libor on 5.3.2016.
 */
public class Api {

    private static Api mInstance;
    private VolleySingleton volley;
    private static Context mCtx;

    private Api(Context context) {
        mCtx = context;
        volley = VolleySingleton.getInstance();
    }

    public static synchronized Api getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Api(context);
        }
        return mInstance;
    }

    public void stringReq(String url, final VolleyCallback callback) {
        final ProgressDialog progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage("Operation in progress...");
        progressDialog.show();

        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        callback.onSuccess(response);
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                        progressDialog.dismiss();
                    }
                });

        volley.getRequestQueue().add(stringRequest);
    }

    public void getCulture(VolleyCallback callback) {
        String url = BuildConfig.API_URL + "api/pscripts/runscript/getCulture";
        stringReq(url, callback);
    }

}
