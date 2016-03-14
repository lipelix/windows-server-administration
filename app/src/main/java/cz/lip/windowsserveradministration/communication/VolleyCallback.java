package cz.lip.windowsserveradministration.communication;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(String response);
    void onError(VolleyError error);
}