package cz.mowin.communication;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cz.mowin.AppController;
import cz.mowin.utils.Utils;

/**
 * Created by Libor on 5.3.2016.
 */
public class Api {

    private String TAG = "API";
    private String protocol = "https";

    private static Api mInstance;
    private VolleySingleton volley;
    private Context mCtx;
    public static ProgressDialog progressDialog;
    private String host;
    private Context activityCtx;


    private Api(Context context) {
        mCtx = context;
        volley = VolleySingleton.getInstance();
//        host = AppController.getPref().getString("host", BuildConfig.API_URL);
    }

    public static synchronized Api getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Api(context);
        }

        return mInstance;
    }

    public void setActivityContext(Context ctx) {
        activityCtx = ctx;
        progressDialog = new ProgressDialog(activityCtx);
        progressDialog.setMessage("Operation in progress...");
        progressDialog.setCancelable(false);
    }

    public void setHost(String url) {
        host = url;
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
                            if (!json.isNull("error")) {
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
                        handleErrorResponse(error);
                    }

                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + AppController.getPref().getString("access_token", ""));
                params.put("Content-Type", "application/json");

                return params;
            }
        };


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
                    if (response.charAt(response.length() - 1) == '"' && response.charAt(0) == '"') {
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\\"", "\"");
                    }

                Log.i(TAG, response.toString());

                try {
                    JSONObject json = new JSONObject(response);
                    if (!json.isNull("error")) {
                        Toast.makeText(activityCtx, json.getString("error"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                handleErrorResponse(error);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                header.put("Authorization", "Bearer " + AppController.getPref().getString("access_token", ""));
//                params.put("Content-Type", "application/json");

                return header;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String httpPostBody = params;
                return httpPostBody.getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Log.i(TAG, url);
        Log.i(TAG, params);
        volley.getRequestQueue().add(stringRequest);
    }

    private void handleErrorResponse(VolleyError error) {
        progressDialog.dismiss();

        if (error instanceof TimeoutError) {
            Log.e(TAG, "TimeoutError: " + error.getNetworkTimeMs() + " ms");
            Toast.makeText(activityCtx, "TimeoutError: " + error.getNetworkTimeMs() + " ms", Toast.LENGTH_LONG).show();
        } else if (error instanceof NoConnectionError) {
            Log.e(TAG, "NoConnectionError: " + error.getMessage());
            Toast.makeText(activityCtx, "NoConnectionError: " + error.getMessage(), Toast.LENGTH_LONG).show();
        } else if (error instanceof ServerError) {
            try {
                JSONObject json = new JSONObject(new String(error.networkResponse.data));
                Log.e(TAG, json.getString("error_description"));
                Toast.makeText(activityCtx, json.getString("error_description"), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "ServerError: " + error.getMessage());
                Toast.makeText(activityCtx, "ServerError " + error.networkResponse.statusCode , Toast.LENGTH_LONG).show();
            }
        } else if (error instanceof NetworkError) {
            Log.e(TAG, "NetworkError: " + error.getMessage());
            Toast.makeText(activityCtx, "NetworkError: " + error.getMessage(), Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            Log.e(TAG, "ParseError: " + error.getMessage());
            Toast.makeText(activityCtx, "ParseError: " + error.getMessage(), Toast.LENGTH_LONG).show();
        } else if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                JSONObject json = new JSONObject(new String(error.networkResponse.data));
                Log.e(TAG, json.getString("Message"));
                Toast.makeText(activityCtx, error.networkResponse.statusCode + ": " + json.getString("Message"), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getCert(final VolleyCallback callback) {
        progressDialog.show();

        String url= "http://" + host + "/api/account/getcert";

        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, url,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        try {
                            if (response != null) {
                                FileOutputStream outputStream;

                                try {
                                    outputStream = AppController.getAppContext().openFileOutput(AppController.CERT_FILE_NAME, Context.MODE_PRIVATE);
                                    outputStream.write(response);
                                    outputStream.close();
                                    Toast.makeText(AppController.getAppContext(), "Download certificate complete", Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "Download certificate complete");

                                    callback.onSuccess("ok");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "Error during saving certificate");
                                    Toast.makeText(AppController.getAppContext(), "Error during saving certificate", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(AppController.getAppContext(), "Unable to download certificate", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Unable to download certificate");
                            e.printStackTrace();
                        }

                        progressDialog.show();
                    }
                } ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                handleErrorResponse(error);
            }
        }, null);

        request.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue mRequestQueue = Volley.newRequestQueue(AppController.getAppContext(), new HurlStack());
        mRequestQueue.add(request);
    }

    public void ping(VolleyCallback callback) {
        String url = "https://" + host + "api/account/ping";
        stringReq(url, callback);
    }

    public void getToken(String username, String password, VolleyCallback callback) {
        String url = protocol + "://" + host + "/api/token";

        Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("username", username);
        params.put("password", password);

        stringPostReq(url, Utils.mapToString(params), callback);
    }

    public void isValid(String username, String password, VolleyCallback callback) {
        String url = protocol + "://" + host + "/api/account/isValid";

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        stringPostReq(url, Utils.mapToString(params), callback);
    }

    public void getCulture(VolleyCallback callback) {
        String url = protocol + "://" + host + "/api/pscripts/runscript/getCulture";
        stringReq(url, callback);
    }

    public void getUser(String name, VolleyCallback callback) {
        String url = protocol + "://" + host + "/api/pscripts/runscriptpost/getUser";

        Map<String, String> params = new HashMap<String, String>();
        params.put("User", name);

        stringPostReq(url, Utils.mapToString(params), callback);
    }

    public void getUserMembership(String name, VolleyCallback callback) {
        String url = protocol + "://" + host + "/api/pscripts/runscriptpost/getUserMembership";

        Map<String, String> params = new HashMap<String, String>();
        params.put("Name", name);

        stringPostReq(url, Utils.mapToString(params), callback);
    }

    public void getUsers(VolleyCallback callback) {
        String url = protocol + "://" + host + "/api/pscripts/runscript/getUsers";
        stringReq(url, callback);
    }

    public void getServices(VolleyCallback callback) {
        String url = protocol + "://" + host + "/api/pscripts/runscript/getServices";
        stringReq(url, callback);
    }

    public void getProcesses(VolleyCallback callback) {
        String url = protocol + "://" + host + "/api/pscripts/runscript/getProcesses";
        stringReq(url, callback);
    }

    public void getInactive(VolleyCallback callback) {
        String url = protocol + "://" + host + "/api/pscripts/runscript/getInactive";
        stringReq(url, callback);
    }

    public void showDiskSpace(VolleyCallback callback) {
        String url = protocol + "://" + host + "/api/pscripts/runscript/showDiskSpace";
        stringReq(url, callback);
    }

    public void createUser(String name, String login, String password, VolleyCallback callback) {
        String url = protocol + "://" + host + "/api/pscripts/runscriptpost/createUser";

        Map<String, String> params = new HashMap<String, String>();
        params.put("Name", name);
        params.put("Login", login);
        params.put("Password", password);

        stringPostReq(url, Utils.mapToString(params), callback);
    }

}
