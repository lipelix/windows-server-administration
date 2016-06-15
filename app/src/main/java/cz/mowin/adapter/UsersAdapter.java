package cz.mowin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cz.mowin.R;
import cz.mowin.communication.response.UserItem;

/**
 * Adapter class acts as a bridge between an AdapterView and the underlying data for that view. The Adapter provides access to the data items. The Adapter is also responsible for making a View for each item in the data set.
 * This class bridges data of users.
 * @author Libor Vachal
 */
public class UsersAdapter extends ArrayAdapter<UserItem> {

    /**
     * Adapter constructor.
     * @param context activity context
     * @param users list of users items
     */
    public UsersAdapter(Context context, ArrayList<UserItem> users) {
        super(context, 0, users);
    }

    /**
     * Get view of bridged item
     * @param position position in collection
     * @param convertView item view
     * @param parent parent view
     * @return view of converted item
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        UserItem user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_users, parent, false);
        }

        TextView twSName = (TextView) convertView.findViewById(R.id.tw_users_item_sname);
        TextView twName = (TextView) convertView.findViewById(R.id.tw_users_item_name);

        twSName.setText(user.samAccountName);
        twName.setText(user.name);

        ImageView iwEnabled = (ImageView) convertView.findViewById(R.id.iw_user_enabled);
        ImageView iwDisabled = (ImageView) convertView.findViewById(R.id.iw_user_disabled);

        if (user.enabled) {
            iwEnabled.setVisibility(View.VISIBLE);
            iwDisabled.setVisibility(View.GONE);
        }
        else {
            iwEnabled.setVisibility(View.GONE);
            iwDisabled.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}