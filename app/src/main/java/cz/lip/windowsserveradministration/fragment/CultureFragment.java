package cz.lip.windowsserveradministration.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import cz.lip.windowsserveradministration.AppController;
import cz.lip.windowsserveradministration.R;
import cz.lip.windowsserveradministration.communication.Api;
import cz.lip.windowsserveradministration.communication.VolleyCallback;
import cz.lip.windowsserveradministration.communication.response.CultureResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class CultureFragment extends Fragment {

    protected Api api;

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
                        Toast.makeText(AppController.getAppContext(), response, Toast.LENGTH_LONG).show();
                        Gson gson = new Gson();
                        CultureResponse resp = gson.fromJson(response, CultureResponse.class);
                        tw.setText(resp.toString());
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Toast.makeText(AppController.getAppContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onScriptError(String error) {
                        Toast.makeText(AppController.getAppContext(), error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return view;
    }

}
