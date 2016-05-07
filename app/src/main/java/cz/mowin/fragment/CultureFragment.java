package cz.mowin.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import cz.mowin.R;
import cz.mowin.communication.Api;
import cz.mowin.communication.VolleyCallback;
import cz.mowin.communication.response.CultureResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class CultureFragment extends Fragment {

    private Api api;

    public CultureFragment() {
        api = Api.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_culture, container, false);

        Button button = (Button) view.findViewById(R.id.btn_culture_run);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView tw = (TextView) view.findViewById(R.id.tw_culture_output);
                api.getCulture(new VolleyCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Gson gson = new Gson();
                        CultureResponse resp = gson.fromJson(response, CultureResponse.class);
                        tw.setText(resp.toString());
                    }
                });
            }
        });

        return view;
    }

}
