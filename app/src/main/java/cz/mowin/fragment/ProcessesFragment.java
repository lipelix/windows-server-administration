package cz.mowin.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.mowin.AppController;
import cz.mowin.R;
import cz.mowin.adapter.ProcessesAdapter;
import cz.mowin.communication.Api;
import cz.mowin.communication.VolleyCallback;
import cz.mowin.communication.response.ProcessItem;
import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * Fragment for showing information about processes.
 */
public class ProcessesFragment extends Fragment {

    protected Api api;

    public ProcessesFragment() {
        api = Api.getInstance(getActivity());
    }

    /**
     * Initialize view with buttons and layout. Register listeners and adapters for loading data
     * @param inflater layout inflater
     * @param container container view
     * @param savedInstanceState saved data from previous interaction
     * @return fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_processes, container, false);

        ArrayList<ProcessItem> arrayOfProcesses = new ArrayList<ProcessItem>();
        final ProcessesAdapter adapter = new ProcessesAdapter(getActivity(), arrayOfProcesses);
        ListView listView = (ListView) view.findViewById(R.id.lw_processes);
        listView.setAdapter(adapter);

        final Button button = (Button) view.findViewById(R.id.btn_processes_run);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                api.getProcesses(new VolleyCallback() {
                    @Override
                    public void onSuccess(String response) {
                        JSONArray processes = null;
                        try {
                            processes = new JSONArray(response);
                            ArrayList<ProcessItem> newProcesses = ProcessItem.fromJson(processes);
                            adapter.addAll(newProcesses);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AppController.getAppContext(), "Problem with response format.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        ImageButton help = (ImageButton) view.findViewById(R.id.btn_processes_help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip.make(getActivity(),
                        new Tooltip.Builder()
                                .anchor(view, Tooltip.Gravity.CENTER)
                                .closePolicy(new Tooltip.ClosePolicy()
                                        .insidePolicy(true, false)
                                        .outsidePolicy(true, false), 0)
                                .text(getResources().getString(R.string.tooltip_processes))
                                .maxWidth(500)
                                .withOverlay(true)
                                .fadeDuration(200)
                                .build()
                ).show();
            }
        });

        return view;
    }

}
