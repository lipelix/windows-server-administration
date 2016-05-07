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

public class ServicesAdapter extends ArrayAdapter<ServiceItem> {
    public ServicesAdapter(Context context, ArrayList<ServiceItem> services) {
        super(context, 0, services);
    }

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