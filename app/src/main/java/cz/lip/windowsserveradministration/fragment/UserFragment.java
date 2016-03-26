package cz.lip.windowsserveradministration.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import cz.lip.windowsserveradministration.AppController;
import cz.lip.windowsserveradministration.R;
import cz.lip.windowsserveradministration.communication.Api;
import cz.lip.windowsserveradministration.communication.VolleyCallback;
import cz.lip.windowsserveradministration.communication.response.UserResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    protected Api api;

    public UserFragment() {
        api = Api.getInstance(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_user, container, false);

        Button button = (Button) view.findViewById(R.id.btn_user_run);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView output = (TextView) view.findViewById(R.id.tw_user_output);
                final EditText userName = (EditText) view.findViewById(R.id.btn_user_name);

                if (userName.getText().toString().trim().length() < 1) {
                    userName.setError(getResources().getString(R.string.error_empty_input));
                    return;
                }

                api.getUser(String.valueOf(userName.getText()), new VolleyCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(AppController.getAppContext(), response, Toast.LENGTH_LONG).show();
                        Gson gson = new Gson();
                        UserResponse resp = gson.fromJson(response, UserResponse.class);
                        output.setText(resp.toString());
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
