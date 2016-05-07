package cz.mowin.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.mowin.AppController;
import cz.mowin.R;
import cz.mowin.adapter.InactiveAdapter;
import cz.mowin.communication.Api;
import cz.mowin.communication.VolleyCallback;
import cz.mowin.communication.response.InactiveItem;
import it.sephiroth.android.library.tooltip.Tooltip;


public class InactiveFragment extends Fragment {

    protected Api api;

    public InactiveFragment() {
        api = Api.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_inactive, container, false);

        ArrayList<InactiveItem> arrayOfInactive = new ArrayList<InactiveItem>();
        final InactiveAdapter adapter = new InactiveAdapter(getActivity(), arrayOfInactive);
        ListView listView = (ListView) view.findViewById(R.id.lw_inactive);
        listView.setAdapter(adapter);

        final Button button = (Button) view.findViewById(R.id.btn_inactive_run);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                api.getInactive(new VolleyCallback() {
                    @Override
                    public void onSuccess(String response) {
                        JSONArray inactives = null;
                        try {
                            inactives = new JSONArray(response);
                            ArrayList<InactiveItem> newInactives = InactiveItem.fromJson(inactives);
                            adapter.addAll(newInactives);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AppController.getAppContext(), "Problem with response format.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        ImageButton help = (ImageButton) view.findViewById(R.id.btn_inactive_help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip.make(getActivity(),
                        new Tooltip.Builder()
                                .anchor(view, Tooltip.Gravity.CENTER)
                                .closePolicy(new Tooltip.ClosePolicy()
                                        .insidePolicy(true, false)
                                        .outsidePolicy(true, false), 0)
                                .text(getResources().getString(R.string.tooltip_inactives))
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
