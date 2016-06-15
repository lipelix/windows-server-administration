package cz.mowin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz.mowin.R;
import cz.mowin.communication.response.InactiveItem;

/**
 * Adapter class acts as a bridge between an AdapterView and the underlying data for that view. The Adapter provides access to the data items. The Adapter is also responsible for making a View for each item in the data set.
 * This class bridges data of inactive computers and users.
 * @author Libor Vachal
 */
public class InactiveAdapter extends ArrayAdapter<InactiveItem> {

    /**
     * Adapter constructor.
     * @param context activity context
     * @param inactives list of inactive items
     */
    public InactiveAdapter(Context context, ArrayList<InactiveItem> inactives) {
        super(context, 0, inactives);
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

        InactiveItem inactive = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_inactives, parent, false);
        }

        TextView twDate = (TextView) convertView.findViewById(R.id.tw_inactives_item_date);
        TextView twName = (TextView) convertView.findViewById(R.id.tw_inactives_item_name);

        twName.setText(inactive.samAccountName);
        if (inactive.lastLogonDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("d.M.yyyy HH:mm:ss");
            twDate.setText(format.format(inactive.lastLogonDate));
        } else {
            twDate.setText("-");
        }

        ImageView iwUser = (ImageView) convertView.findViewById(R.id.iw_inactives_item_user);
        ImageView iwComputer = (ImageView) convertView.findViewById(R.id.iw_inactives_item_computer);

        if (inactive.type.equals("user")) {
            iwUser.setVisibility(View.VISIBLE);
            iwComputer.setVisibility(View.GONE);
        }
        else if (inactive.type.equals("computer")) {
            iwUser.setVisibility(View.GONE);
            iwComputer.setVisibility(View.VISIBLE);
        } else {
            iwUser.setVisibility(View.GONE);
            iwComputer.setVisibility(View.GONE);
        }

        return convertView;
    }
}