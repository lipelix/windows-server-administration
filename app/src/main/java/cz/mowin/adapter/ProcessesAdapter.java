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

public class ProcessesAdapter extends ArrayAdapter<ProcessItem> {
    public ProcessesAdapter(Context context, ArrayList<ProcessItem> processes) {
        super(context, 0, processes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ProcessItem process = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_processes, parent, false);
        }

        TextView twCpuPercent = (TextView) convertView.findViewById(R.id.tw_processes_item_cpu_percent);
        TextView twName = (TextView) convertView.findViewById(R.id.tw_processes_item_name);
        TextView twWs = (TextView) convertView.findViewById(R.id.tw_processes_item_ws);

        twCpuPercent.setText(String.valueOf(process.cpuPercent));
        twWs.setText(String.valueOf(process.ws));
        twName.setText(process.name);

        return convertView;
    }
}