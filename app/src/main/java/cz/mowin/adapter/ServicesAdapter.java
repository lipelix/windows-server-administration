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
import cz.mowin.communication.response.ServiceItem;
import cz.mowin.communication.response.UserItem;

/**
 * Adapter class acts as a bridge between an AdapterView and the underlying data for that view. The Adapter provides access to the data items. The Adapter is also responsible for making a View for each item in the data set.
 * This class bridges data of services.
 * @author Libor Vachal
 */
public class ServicesAdapter extends ArrayAdapter<ServiceItem> {

    /**
     * Adapter constructor.
     * @param context activity context
     * @param services list of services items
     */
    public ServicesAdapter(Context context, ArrayList<ServiceItem> services) {
        super(context, 0, services);
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

        ServiceItem service = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_services, parent, false);
        }

        TextView twName = (TextView) convertView.findViewById(R.id.tw_services_item_name);
        twName.setText(service.name);

        ImageView iwRunning = (ImageView) convertView.findViewById(R.id.iw_service_running);
        ImageView iwStopped = (ImageView) convertView.findViewById(R.id.iw_service_stopped);

        if (service.isRunning()) {
            iwRunning.setVisibility(View.VISIBLE);
            iwStopped.setVisibility(View.GONE);
        }
        else {
            iwRunning.setVisibility(View.GONE);
            iwStopped.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}