package cz.lip.windowsserveradministration.communication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

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
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                        // Handle error
                    }
                });

        volley.getRequestQueue().add(stringRequest);
    }

//    public void jsonReq(String url, final VolleyCallback callback) {
////        Map<String, String> params = new HashMap();
////        params.put("first_param", 1);
////        params.put("second_param", 2);
////
////        JSONObject parameters = new JSONObject(params);
//        JSONObject parameters = null;
//
//        // Formulate the request and handle the response.
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, parameters,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // Do something with the response
//                        callback.onSuccess(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        callback.onError(error);
//                        // Handle error
//                    }
//                });
//
//        volley.getRequestQueue().add(jsObjRequest);
//    }

    public void getCulture(VolleyCallback callback) {
        String url = "http://192.168.0.104:53978/api/pscripts/runscript/getCulture";
        stringReq(url, callback);
    }

}
