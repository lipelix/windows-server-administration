package cz.mowin.communication.response;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserItem {

    public String samAccountName;
    public String name;
    public Boolean enabled;

    public UserItem(JSONObject object){
        try {
            this.samAccountName = object.getString("SamAccountName");
            this.name = object.getString("Name");
            this.enabled = object.getBoolean("Enabled");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // UserItem.fromJson(jsonArray);
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