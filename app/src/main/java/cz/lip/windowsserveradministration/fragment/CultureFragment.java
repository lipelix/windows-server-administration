package cz.lip.windowsserveradministration.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;

import cz.lip.windowsserveradministration.AppController;
import cz.lip.windowsserveradministration.R;
import cz.lip.windowsserveradministration.communication.Api;
import cz.lip.windowsserveradministration.communication.VolleyCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class CultureFragment extends Fragment {

    protected Api api;

    public CultureFragment() {
        // Required empty public constructor
        api = Api.getInstance(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_culture, container, false);

        Button button = (Button) view.findViewById(R.id.btn_culture_run);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.getCulture(new VolleyCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(AppController.getAppContext(), response, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Toast.makeText(AppController.getAppContext(), "chyba", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
