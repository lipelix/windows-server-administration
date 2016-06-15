package cz.mowin.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import cz.mowin.adapter.ServicesAdapter;
import cz.mowin.adapter.UsersAdapter;
import cz.mowin.communication.Api;
import cz.mowin.communication.VolleyCallback;
import cz.mowin.communication.response.ServiceItem;
import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * Fragment for showing information about services.
 */
public class ServicesFragment extends Fragment {

    protected Api api;

    public ServicesFragment() {
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

        final View view = inflater.inflate(R.layout.fragment_services, container, false);

        ArrayList<ServiceItem> arrayOfServices = new ArrayList<ServiceItem>();
        final ServicesAdapter adapter = new ServicesAdapter(getActivity(), arrayOfServices);
        ListView listView = (ListView) view.findViewById(R.id.lw_services);
        listView.setAdapter(adapter);

        final Button button = (Button) view.findViewById(R.id.btn_services_run);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                api.getServices(new VolleyCallback() {
                    @Override
                    public void onSuccess(String response) {
                        JSONArray services = null;
                        try {
                            services = new JSONArray(response);
                            ArrayList<ServiceItem> newServices = ServiceItem.fromJson(services);
                            adapter.addAll(newServices);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AppController.getAppContext(), "Problem with response format.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        ImageButton help = (ImageButton) view.findViewById(R.id.btn_services_help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip.make(getActivity(),
                        new Tooltip.Builder()
                                .anchor(view, Tooltip.Gravity.CENTER)
                                .closePolicy(new Tooltip.ClosePolicy()
                                        .insidePolicy(true, false)
                                        .outsidePolicy(true, false), 0)
                                .text(getResources().getString(R.string.tooltip_services))
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
