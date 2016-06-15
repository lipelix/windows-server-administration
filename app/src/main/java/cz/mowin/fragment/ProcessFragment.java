package cz.mowin.fragment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import cz.mowin.AppController;
import cz.mowin.R;
import cz.mowin.communication.Api;
import cz.mowin.communication.VolleyCallback;
import cz.mowin.communication.response.ProcessResponse;
import cz.mowin.communication.response.ServiceResponse;
import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * Fragment for showing information about disks.
 */
public class ProcessFragment extends Fragment {

    private Api api;

    public ProcessFragment() {
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

        final View view = inflater.inflate(R.layout.fragment_process, container, false);

        final Bundle bundle = getArguments();
        final int processId =  bundle.getInt("process_id");
        final Button btnStop = (Button) view.findViewById(R.id.btn_process_stop);
        final TextView tw = (TextView) view.findViewById(R.id.tw_process_output);

        refreshState(processId, tw);

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setMessage(getResources().getString(R.string.stop_process_confirm))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                api.stopProcess(processId, new VolleyCallback() {
                                    @Override
                                    public void onSuccess(String response) {
                                        tw.setText("Process has been killed.");
                                        btnStop.setEnabled(false);
                                        refreshState(processId, tw);
                                    }
                                });
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .show();
            }
        });

        ImageButton help = (ImageButton) view.findViewById(R.id.btn_process_help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip.make(getActivity(),
                        new Tooltip.Builder()
                                .anchor(view, Tooltip.Gravity.CENTER)
                                .closePolicy(new Tooltip.ClosePolicy()
                                        .insidePolicy(true, false)
                                        .outsidePolicy(true, false), 0)
                                .text(getResources().getString(R.string.tooltip_process))
                                .maxWidth(500)
                                .withOverlay(true)
                                .fadeDuration(200)
                                .build()
                ).show();
            }
        });

        return view;
    }

    /**
     * Call webserver and refresh process view
     * @param id process id
     * @param output output view
     */
    private void refreshState(final int id, final TextView output) {
        api.getProcess(id, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                ProcessResponse process = gson.fromJson(response, ProcessResponse.class);
                output.setText(process.toString());
            }
        });
    }

}
