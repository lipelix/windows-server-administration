package cz.lip.windowsserveradministration.communication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import cz.lip.windowsserveradministration.AppController;
import cz.lip.windowsserveradministration.BuildConfig;
import cz.lip.windowsserveradministration.communication.response.ErrorResponse;

/**
 * Created by Libor on 5.3.2016.
 */
public class Api {

    private String TAG = "API";

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
                        progressDialog.dismiss();
                        // remove double quotes around response
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\\"", "\"");
                        Log.d(TAG, response.toString());

                        try {
                            JSONObject json = new JSONObject(response);
                            if(!json.isNull("error")) {
                                callback.onScriptError(json.getString("error"));
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error.getMessage() != null)
                            Log.d(TAG, error.getMessage());
                        callback.onError(error);
                    }
                });

        volley.getRequestQueue().add(stringRequest);
    }

    public void getCulture(VolleyCallback callback) {
        String url = BuildConfig.API_URL + "api/pscripts/runscript/getCulture";
        stringReq(url, callback);
    }

    public void getUser(String name, VolleyCallback callback) {
        String url = BuildConfig.API_URL + "api/pscripts/runscript/getUser?User=" + name;
        stringReq(url, callback);
    }

}
