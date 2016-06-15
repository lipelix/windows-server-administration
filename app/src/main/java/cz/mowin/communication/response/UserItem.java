package cz.mowin.communication.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Model class for user item
 * @author Libor Vachal
 */
public class UserItem {

    /**
     * Logon name in Active Directory
     */
    public String samAccountName;

    /**
     * Username in Active Directory
     */
    public String name;

    /**
     * User is enabled
     */
    public Boolean enabled;

    /**
     * Parse json response from server to model object
     * @param object json response
     */
    public UserItem(JSONObject object){
        try {
            this.samAccountName = object.getString("SamAccountName");
            this.name = object.getString("Name");
            this.enabled = object.getBoolean("Enabled");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse json response from server to list of model objects
     * @param jsonObjects json response
     * @return list of model objects
     */
    public static ArrayList<UserItem> fromJson(JSONArray jsonObjects) {
        ArrayList<UserItem> users = new ArrayList<UserItem>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                users.add(new UserItem(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }
}