package cz.lip.windowsserveradministration.communication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import cz.lip.windowsserveradministration.AppController;
import cz.lip.windowsserveradministration.BuildConfig;
import cz.lip.windowsserveradministration.R;
import cz.lip.windowsserveradministration.communication.response.ErrorResponse;

/**
 * Created by Libor on 5.3.2016.
 */
public class Api {

    private String TAG = "API";

    private static Api mInstance;
    private VolleySingleton volley;
    private static Context mCtx;
    public ProgressDialog progressDialog;


    private Api(Context context) {
        mCtx = context;
        volley = VolleySingleton.getInstance();
        progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage("Operation in progress...");
    }

    public static synchronized Api getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Api(context);
        }
        return mInstance;
    }

    public void stringReq(final String url, final VolleyCallback callback) {
        progressDialog.show();

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
                            Log.e(TAG, error.getMessage());
                        callback.onError(error);
                    }
                });


//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                5000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        volley.getRequestQueue().add(stringRequest);
    }

    private void stringPostReq(String url, final String params, final VolleyCallback callback) {
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        // remove double quotes around response
//                        response = response.substring(1, response.length() - 1);
//                        response = response.replace("\\\"", "\"");
                        Log.i(TAG, response.toString());

//                        try {
//                            JSONObject json = new JSONObject(response);
//                            if(!json.isNull("error")) {
//                                callback.onScriptError(json.getString("error"));
//                                return;
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Log.e(TAG, "TimeoutError: " + error.getNetworkTimeMs() + " ms");
                            Toast.makeText(mCtx, "TimeoutError: " + error.getNetworkTimeMs() + " ms", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Log.e(TAG, "AuthFailureError: " + error.getMessage());
                            Toast.makeText(mCtx, "AuthFailureError: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Log.e(TAG, "NetworkError: " + error.getMessage());
                            Toast.makeText(mCtx, "NetworkError: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }

                        callback.onError(error);
                    }
        }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("Content-Type","application/x-www-form-urlencoded");
//                return params;
//            }

            @Override
            public byte[] getBody() throws AuthFailureError {
//                String httpPostBody="grant_type=password&username=admin&password=123456";
                String httpPostBody = params;
                return httpPostBody.getBytes();
            }
        };

        volley.getRequestQueue().add(stringRequest);
    }

    private String mapToString(Map<String, String> map) {
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

    public void ping(VolleyCallback callback) {
        String url = BuildConfig.API_URL + "api/account/ping";
        stringReq(url, callback);
    }

    public void getToken(String username, String password, VolleyCallback callback) {
        String url = BuildConfig.API_URL + "api/token";

        Map<String,String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("username", username);
        params.put("password", password);

        stringPostReq(url, mapToString(params), callback);
    }

    public void isValid(String username, String password, VolleyCallback callback) {
        String url = BuildConfig.API_URL + "api/account/isValid";

        Map<String,String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        stringPostReq(url, mapToString(params) , callback);
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
