package cz.mowin.communication.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Model class for process item
 * @author Libor Vachal
 */
public class ProcessItem {


    public int id;
    public double cpu;
    public double cpuPercent;
    public String name;
    public String description;
    public double ws;

    /**
     * Parse json response from server to model object
     * @param object json response
     */
    public ProcessItem(JSONObject object){
        try {
            this.id = object.getInt("Id");
            this.cpu = object.getDouble("CPU");
            this.cpuPercent = object.getDouble("CPUPercent");
            this.name = object.getString("Name");
            this.description = object.getString("Description");
            this.ws = object.getDouble("WS");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse json response from server to list of model objects
     * @param jsonObjects json response
     * @return list of model objects
     */
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