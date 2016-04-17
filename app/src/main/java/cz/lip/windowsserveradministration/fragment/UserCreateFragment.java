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
import cz.lip.windowsserveradministration.communication.response.UserCreateResponse;
import cz.lip.windowsserveradministration.communication.response.UserResponse;
import cz.lip.windowsserveradministration.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserCreateFragment extends Fragment {

    protected Api api;

    public UserCreateFragment() {
        api = Api.getInstance(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_user_create, container, false);

        final Button button = (Button) view.findViewById(R.id.btn_user_create_run);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView output = (TextView) view.findViewById(R.id.tw_user_create_output);
                final EditText name = (EditText) view.findViewById(R.id.tw_user_name);
                final EditText login = (EditText) view.findViewById(R.id.tw_user_login);
                final EditText pass = (EditText) view.findViewById(R.id.tw_user_password);

                if (!Utils.isAlphaNumericSpace(name.getText().toString())) {
                    name.setError(getString(R.string.error_invalid_alphanumeric));
                    return;
                } else if (!Utils.isAlphaNumeric(login.getText().toString())) {
                    login.setError(getString(R.string.error_invalid_alphanumeric));
                    return;
                } else if (!Utils.isAlphaNumeric(pass.getText().toString())) {
                    pass.setError(getString(R.string.error_invalid_alphanumeric));
                    return;
                }

                AppController.hideKeyboardFrom(getActivity());

                api.createUser(name.getText().toString(), login.getText().toString(), pass.getText().toString(), new VolleyCallback() {
                    @Override
                    public void onSuccess(String response) {

                        api.getUser(login.getText().toString(), new VolleyCallback() {
                            @Override
                            public void onSuccess(String response) {
                                Gson gson = new Gson();
                                UserCreateResponse resp = gson.fromJson(response, UserCreateResponse.class);
                                output.setText(resp.toString());
                            }

                        });

                    }

                });
            }
        });

        return view;
    }

}
