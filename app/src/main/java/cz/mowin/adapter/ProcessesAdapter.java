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
import cz.mowin.communication.response.ProcessItem;

/**
 * Adapter class acts as a bridge between an AdapterView and the underlying data for that view. The Adapter provides access to the data items. The Adapter is also responsible for making a View for each item in the data set.
 * This class bridges data of processes.
 * @author Libor Vachal
 */
public class ProcessesAdapter extends ArrayAdapter<ProcessItem> {

    /**
     * Adapter constructor.
     * @param context activity context
     * @param processes list of processes items
     */
    public ProcessesAdapter(Context context, ArrayList<ProcessItem> processes) {
        super(context, 0, processes);
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

        ProcessItem process = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_processes, parent, false);
        }

        TextView twCpuPercent = (TextView) convertView.findViewById(R.id.tw_processes_item_cpu_percent);
        TextView twName = (TextView) convertView.findViewById(R.id.tw_processes_item_name);
        TextView twWs = (TextView) convertView.findViewById(R.id.tw_processes_item_ws);
        TextView twId = (TextView) convertView.findViewById(R.id.tw_processes_item_id);

        twCpuPercent.setText(String.valueOf(process.cpuPercent));
        twWs.setText(String.valueOf(process.ws));
        twName.setText(process.name);
        twId.setText(String.valueOf(process.id));

        return convertView;
    }
}