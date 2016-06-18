package cz.mowin.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import cz.mowin.AppController;
import cz.mowin.R;
import cz.mowin.communication.Api;
import cz.mowin.communication.VolleyCallback;
import cz.mowin.communication.response.UserCreateResponse;
import cz.mowin.utils.Utils;
import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * Fragment for creating new users.
 */
public class UserCreateFragment extends Fragment {

    protected Api api;

    public UserCreateFragment() {
        api = Api.getInstance(getActivity());
    }


    /**
     * Initialize view with buttons and layout. Register listeners for loading data
     * @param inflater layout inflater
     * @param container container view
     * @param savedInstanceState saved data from previous interaction
     * @return fragment view
     */
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
                    name.setError(getString(R.string.error_invalid_alphanumericspace));
                    return;
                } else if (!Utils.isAlphaNumericUnderscore(login.getText().toString())) {
                    login.setError(getString(R.string.error_invalid_alphanumericunderscore));
                    return;
                } else if (Utils.isAlphaNumeric(pass.getText().toString().trim())) {
                    pass.setError(getString(R.string.error_invalid_create_pass));
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

        ImageButton help = (ImageButton) view.findViewById(R.id.btn_user_create_help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip.make(getActivity(),
                        new Tooltip.Builder()
                                .anchor(view, Tooltip.Gravity.CENTER)
                                .closePolicy(new Tooltip.ClosePolicy()
                                        .insidePolicy(true, false)
                                        .outsidePolicy(true, false), 0)
                                .text(getResources().getString(R.string.tooltip_create_user))
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
