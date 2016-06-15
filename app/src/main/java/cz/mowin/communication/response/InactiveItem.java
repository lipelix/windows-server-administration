package cz.mowin.communication.response;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Model class for user inactive item
 * @author Libor Vachal
 */
public class InactiveItem {

    /**
     * Logon name in Active Directory
     */
    public String samAccountName;

    /**
     * Type of object
     */
    public String type;

    /**
     * Date of last logon into system
     */
    public Date lastLogonDate;


    /**
     * Parse json response from server to model object
     * @param object json response
     */
    public InactiveItem(JSONObject object){
        try {
            this.samAccountName = object.getString("SamAccountName");
            this.type = object.getString("ObjectClass");
            String dateString = object.getString("LastLogonDate");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            this.lastLogonDate = dateFormat.parse(dateString);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse json response from server to list of model objects
     * @param jsonObjects json response
     * @return list of model objects
     */
    public static ArrayList<InactiveItem> fromJson(JSONArray jsonObjects) {
        ArrayList<InactiveItem> users = new ArrayList<InactiveItem>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                users.add(new InactiveItem(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }
}