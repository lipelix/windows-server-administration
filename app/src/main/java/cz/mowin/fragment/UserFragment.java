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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.mowin.AppController;
import cz.mowin.R;
import cz.mowin.communication.Api;
import cz.mowin.communication.VolleyCallback;
import cz.mowin.communication.response.UserResponse;
import it.sephiroth.android.library.tooltip.Tooltip;

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
        final TextView output = (TextView) view.findViewById(R.id.tw_user_output);
        final TextView outputGroups = (TextView) view.findViewById(R.id.tw_user_group_output);
        final EditText userName = (EditText) view.findViewById(R.id.tw_user_name);

        final Button button = (Button) view.findViewById(R.id.btn_user_run);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userName.getText().toString().trim().length() < 1) {
                    userName.setError(getResources().getString(R.string.error_empty_input));
                    return;
                }

                AppController.hideKeyboardFrom(getActivity());

                api.getUser(String.valueOf(userName.getText()), new VolleyCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Gson gson = new Gson();
                        UserResponse resp = gson.fromJson(response, UserResponse.class);
                        output.setText(resp.toString());

                        api.getUserMembership(String.valueOf(userName.getText()), new VolleyCallback() {
                            @Override
                            public void onSuccess(String response) {
                                outputGroups.setText("Member of: ");
                                try {
                                    JSONArray groups = new JSONArray(response);
                                    for (int i = 0; i < groups.length(); i++) {
                                        outputGroups.append("\n\t" + groups.getString(i));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        });

        ImageButton help = (ImageButton) view.findViewById(R.id.btn_user_help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip.make(getActivity(),
                        new Tooltip.Builder()
                                .anchor(view, Tooltip.Gravity.CENTER)
                                .closePolicy(new Tooltip.ClosePolicy()
                                        .insidePolicy(true, false)
                                        .outsidePolicy(true, false), 0)
                                .text("By executing this script you will get information about Active Directory user. You will see overall information about user at the bottom and user membership at the top of the output.")
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
