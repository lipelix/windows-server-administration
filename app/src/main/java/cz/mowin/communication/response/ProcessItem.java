package cz.mowin.communication.response;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProcessItem {

    public double cpu;
    public double cpuPercent;
    public String name;
    public String description;
    public double ws;

    public ProcessItem(JSONObject object){
        try {
            this.cpu = object.getDouble("CPU");
            this.cpuPercent = object.getDouble("CPUPercent");
            this.name = object.getString("Name");
            this.description = object.getString("Description");
            this.ws = object.getDouble("WS");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // UserItem.fromJson(jsonArray);
    public static ArrayList<ProcessItem> fromJson(JSONArray jsonObjects) {
        ArrayList<ProcessItem> procesess = new ArrayList<ProcessItem>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                procesess.add(new ProcessItem(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return procesess;
    }
}