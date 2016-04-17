package cz.lip.windowsserveradministration.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import cz.lip.windowsserveradministration.AppController;
import cz.lip.windowsserveradministration.R;
import cz.lip.windowsserveradministration.communication.Api;
import cz.lip.windowsserveradministration.communication.VolleyCallback;
import cz.lip.windowsserveradministration.communication.response.CultureResponse;
import cz.lip.windowsserveradministration.communication.response.DiskResponse;

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
                        Gson gson = new Gson();
                        DiskResponse[] disks = gson.fromJson(response, DiskResponse[].class);
                        tw.setText("Available disk units: ");
                        LinearLayout rl = (LinearLayout) view.findViewById(R.id.ll_charts);
                        rl.removeAllViews();

                        for (int i = 0; i < disks.length; i++) {
                            tw.append(disks[i].Name.toString() + " ");
                            PieChart chart = (PieChart) new PieChart(getActivity());
                            chart.setMinimumHeight(500);
                            chart.setMinimumWidth(500);
                            chart.setPadding(10,10,10,10);
                            setData(chart, disks[i].Name, disks[i].Free, disks[i].Used);
                            rl.addView(chart);
                        }
                    }
                });
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
        xVals.add(0, "Free");
        xVals.add(1, "Used");

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