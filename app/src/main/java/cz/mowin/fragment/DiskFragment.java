package cz.mowin.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import cz.mowin.AppController;
import cz.mowin.R;
import cz.mowin.communication.Api;
import cz.mowin.communication.VolleyCallback;
import cz.mowin.communication.response.DiskResponse;
import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiskFragment extends Fragment {

    private final Api api;

    public DiskFragment() {
        api = Api.getInstance(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_disk, container, false);

        Button button = (Button) view.findViewById(R.id.btn_disk_run);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView tw = (TextView) view.findViewById(R.id.tw_disk_output);
                api.showDiskSpace(new VolleyCallback() {
                    @Override
                    public void onSuccess(String response) {
                        LinearLayout rl = (LinearLayout) view.findViewById(R.id.ll_charts);
                        rl.removeAllViews();
                        DiskResponse[] disks;
                        Gson gson = new Gson();

                        try {
                            disks = gson.fromJson(response, DiskResponse[].class);
                        } catch (com.google.gson.JsonSyntaxException e) {
                            e.printStackTrace();
                            disks = new DiskResponse[]{gson.fromJson(response, DiskResponse.class)};
                        }

                        tw.setText("Available disk units: ");
                        for (int i = 0; i < disks.length; i++) {
                            tw.append(disks[i].Name.toString() + " ");
                            PieChart chart = (PieChart) new PieChart(getActivity());
                            chart.setMinimumHeight(500);
                            chart.setMinimumWidth(500);
                            chart.setPadding(10, 10, 10, 10);
                            setData(chart, disks[i].Name, disks[i].Free, disks[i].Used);
                            rl.addView(chart);
                        }
                    }
                });
            }
        });

        ImageButton help = (ImageButton) view.findViewById(R.id.btn_disk_help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip.make(getActivity(),
                        new Tooltip.Builder()
                                .anchor(view, Tooltip.Gravity.CENTER)
                                .closePolicy(new Tooltip.ClosePolicy()
                                        .insidePolicy(true, false)
                                        .outsidePolicy(true, false), 0)
                                .text("By executing this script you will get information about system drives. You will see overall, used and free capacities for all units.")
                                .maxWidth(500)
                                .withOverlay(true)
                                .fadeDuration(200)
                                .build()
                ).show();
            }
        });

        return view;
    }

    private void setData(PieChart chart, String name, double free, double used) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);

        yVals1.add(new Entry((float) free, 0));
        yVals1.add(new Entry((float) used, 1));

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add(0, "Free [GB]");
        xVals.add(1, "Used [GB]");

        PieDataSet dataSet = new PieDataSet(yVals1, name + " disk space");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);

//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//
//        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new LargeValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);
        chart.setCenterText(name);
        chart.setDescription("");
//        chart.setHoleRadius();

        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();
    }

}
