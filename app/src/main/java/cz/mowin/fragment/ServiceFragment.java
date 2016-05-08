package cz.mowin.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import cz.mowin.R;
import cz.mowin.communication.Api;
import cz.mowin.communication.VolleyCallback;
import cz.mowin.communication.response.CultureResponse;
import cz.mowin.communication.response.ServiceResponse;
import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends Fragment {

    private Api api;

    public ServiceFragment() {
        api = Api.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_service, container, false);

        final Bundle bundle = getArguments();
        final String serviceName =  bundle.getString("service_name");
        final Button btnStart = (Button) view.findViewById(R.id.btn_service_start);
        final Button btnStop = (Button) view.findViewById(R.id.btn_service_stop);
        final Button btnRestart = (Button) view.findViewById(R.id.btn_service_restart);
        final TextView tw = (TextView) view.findViewById(R.id.tw_service_output);

        refreshState(serviceName, btnStart, btnStop, btnRestart, tw);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setMessage(getResources().getString(R.string.start_service_confirm))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                api.startService(serviceName, new VolleyCallback() {
                                    @Override
                                    public void onSuccess(String response) {
                                        refreshState(serviceName, btnStart, btnStop, btnRestart, tw);
                                    }
                                });
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .show();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setMessage(getResources().getString(R.string.stop_service_confirm))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                api.stopService(serviceName, new VolleyCallback() {
                                    @Override
                                    public void onSuccess(String response) {
                                        refreshState(serviceName, btnStart, btnStop, btnRestart, tw);
                                    }
                                });
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .show();
            }
        });

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setMessage(getResources().getString(R.string.restart_service_confirm))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                api.restartService(serviceName, new VolleyCallback() {
                                    @Override
                                    public void onSuccess(String response) {
                                        refreshState(serviceName, btnStart, btnStop, btnRestart, tw);
                                    }
                                });
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .show();
            }
        });

        ImageButton help = (ImageButton) view.findViewById(R.id.btn_service_help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip.make(getActivity(),
                        new Tooltip.Builder()
                                .anchor(view, Tooltip.Gravity.CENTER)
                                .closePolicy(new Tooltip.ClosePolicy()
                                        .insidePolicy(true, false)
                                        .outsidePolicy(true, false), 0)
                                .text(getResources().getString(R.string.tooltip_service))
                                .maxWidth(500)
                                .withOverlay(true)
                                .fadeDuration(200)
                                .build()
                ).show();
            }
        });

        return view;
    }

    private void refreshState(String name, final Button start, final Button stop, final Button restart, final TextView output) {
        ServiceResponse service = null;

        api.getService(name, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                ServiceResponse service = gson.fromJson(response, ServiceResponse.class);
                output.setText(service.toString());

                if (service.getStatus().equals("Running")) {
                    start.setEnabled(false);
                    stop.setEnabled(true);
                    restart.setEnabled(true);
                } else if (service.getStatus().equals("Stopped")) {
                    start.setEnabled(true);
                    stop.setEnabled(false);
                    restart.setEnabled(false);
                } else {
                    start.setEnabled(true);
                    stop.setEnabled(true);
                    restart.setEnabled(true);
                }
            }
        });
    }

}
