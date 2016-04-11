package cz.lip.windowsserveradministration.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.lip.windowsserveradministration.AppController;
import cz.lip.windowsserveradministration.R;
import cz.lip.windowsserveradministration.communication.Api;
import cz.lip.windowsserveradministration.communication.VolleyCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    protected TextView twExpires; // textview to display the countdown
    private Api api;
    private CountDown counter;

    public InfoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        api = Api.getInstance(getActivity());

        View view = inflater.inflate(R.layout.fragment_info, container, false);

        final CheckedTextView chtw = (CheckedTextView) view.findViewById(R.id.chtw_connected);

        if (AppController.load("access_token").length() > 0)
            chtw.setChecked(true);
        else {
            chtw.setChecked(false);
        }

        if (AppController.loadLong("access_token_expires") > 0) {
            twExpires = (TextView) view.findViewById(R.id.tw_token_expires);
            refreshCountDown();
        }

        final TextView twHost = (TextView) view.findViewById(R.id.tw_host);
        twHost.setText(AppController.load("host"));

        final TextView teLogin = (TextView) view.findViewById(R.id.tw_login);
        teLogin.setText(AppController.load("login"));

        Button button = (Button) view.findViewById(R.id.btn_info_run);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setCancelable(false);
                ad.setTitle(getString(R.string.prompt_password));

                // Set up the input
                final EditText pass = new EditText(getActivity());
                pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ad.setView(pass);

                ad.setPositiveButton(getActivity().getString(R.string.ok_text), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        api.getToken(AppController.load("login"), pass.getText().toString(), new VolleyCallback() {
                            @Override
                            public void onSuccess(String response) {

                                try {
                                    JSONObject json = new JSONObject(response);
                                    AppController.save("access_token", json.getString("access_token"));

                                    Date now = new Date(System.currentTimeMillis());
                                    AppController.saveLong("access_token_expires", now.getTime() + (json.getLong("expires_in") * 1000));
                                    Toast.makeText(AppController.getAppContext(), "Token refreshed", Toast.LENGTH_LONG);
                                    refreshCountDown();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {

                            }

                            @Override
                            public void onScriptError(String error) {

                            }
                        });

                        dialog.dismiss();
                    }
                });

                ad.setNegativeButton(getActivity().getString(R.string.cancel_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = ad.create();
                alert.show();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void refreshCountDown() {
        Long expires = Long.valueOf(AppController.loadLong("access_token_expires"));
        Date now = new Date(System.currentTimeMillis());
        if (counter != null)
            counter.cancel();
        counter = new CountDown(expires - now.getTime(), 1000);
        counter.start();
    }

    public class CountDown extends CountDownTimer {

        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            twExpires.setText("-");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            twExpires.setText("" + millisUntilFinished / 1000);
        }
    }

}
