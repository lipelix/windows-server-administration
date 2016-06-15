package cz.mowin.communication.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Model class for service item
 * @author Libor Vachal
 */
public class ServiceItem {

    /**
     * Type of how can service be started
     */
    public int startType;

    /**
     * Name of service
     */
    public String name;

    /**
     * Current status of service
     */
    public int status;

    /**
     * Parse json response from server to model object
     * @param object json response
     */
    public ServiceItem(JSONObject object){
        try {
            this.startType = object.getInt("StartType");
            this.name = object.getString("Name");
            this.status = object.getInt("Status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if service is running
     * @return true if its running, false otherwise
     */
    public boolean isRunning() {
        if (status == 4)
            return true;
        return false;
    }

    /**
     * Parse json response from server to list of model objects
     * @param jsonObjects json response
     * @return list of model objects
     */
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