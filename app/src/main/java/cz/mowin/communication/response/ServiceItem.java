package cz.mowin.communication.response;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServiceItem {

    public int startType;
    public String name;
    public int status;

    public ServiceItem(JSONObject object){
        try {
            this.startType = object.getInt("StartType");
            this.name = object.getString("Name");
            this.status = object.getInt("Status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        if (status == 4)
            return true;
        return false;
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // UserItem.fromJson(jsonArray);
    public static ArrayList<ServiceItem> fromJson(JSONArray jsonObjects) {
        ArrayList<ServiceItem> services = new ArrayList<ServiceItem>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                services.add(new ServiceItem(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return services;
    }
}