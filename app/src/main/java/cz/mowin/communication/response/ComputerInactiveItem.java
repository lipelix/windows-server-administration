package cz.mowin.communication.response;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ComputerInactiveItem {

    public String samAccountName;
    public Date lastLogonDate;

    public ComputerInactiveItem(JSONObject object){
        try {
            this.samAccountName = object.getString("SamAccountName");
            String dateString = object.getString("LastLogonDate");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            this.lastLogonDate = dateFormat.parse(dateString);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static ArrayList<ComputerInactiveItem> fromJson(JSONArray jsonObjects) {
        ArrayList<ComputerInactiveItem> computers = new ArrayList<ComputerInactiveItem>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                computers.add(new ComputerInactiveItem(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return computers;
    }
}