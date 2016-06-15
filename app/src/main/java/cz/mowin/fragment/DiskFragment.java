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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import cz.mowin.R;
import cz.mowin.communication.Api;
import cz.mowin.communication.VolleyCallback;
import cz.mowin.communication.response.DiskResponse;
import cz.mowin.utils.DiskValueFormater;
import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * Fragment for showing information about disks.
 */
public class DiskFragment extends Fragment {

    private final Api api;
    private final int CHARTS_PADDING = 10;

    public DiskFragment() {
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

        final View view = inflater.inflate(R.layout.fragment_disk, container, false);

        Button button = (Button) view.findViewById(R.id.btn_disk_run);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView tw = (TextView) view.findViewById(R.id.tw_disk_output);
                api.showDiskSpace(new VolleyCallback() {
                    @Override
                    public void onSuccess(String response) {
                        LinearLayout chartsLayout = (LinearLayout) view.findViewById(R.id.ll_charts);
                        chartsLayout.removeAllViews();
                        DiskResponse[] disks;
                        Gson gson = new Gson();

                        try {
                            disks = gson.fromJson(response, DiskResponse[].class);
                        } catch (com.google.gson.JsonSyntaxException e) {
                            e.printStackTrace();
                            disks = new DiskResponse[]{gson.fromJson(response, DiskResponse.class)};
                        }

                        int size = 0;
                        if (chartsLayout.getHeight() < chartsLayout.getWidth())
                            size = chartsLayout.getHeight();
                        else
                            size = chartsLayout.getWidth();

                        tw.setText(R.string.available_drives);
                        for (int i = 0; i < disks.length; i++) {
                            tw.append(disks[i].Name.toString() + " ");
                            PieChart chart = (PieChart) new PieChart(getActivity());
                            int chartSize = (size / disks.length) - (CHARTS_PADDING * disks.length);
                            chart.setMinimumHeight(chartSize);
                            chart.setMinimumWidth(chartSize);
                            chart.setPadding(CHARTS_PADDING, CHARTS_PADDING, CHARTS_PADDING, CHARTS_PADDING);
                            setData(chart, disks[i].Name, disks[i].Free, disks[i].Used);
                            chartsLayout.addView(chart);
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
                                .text(getResources().getString(R.string.tooltip_drives))
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
     * Sets charts data
     * @param chart chart which show data
     * @param name name of disk
     * @param free available space
     * @param used used space
     */
    private void setData(PieChart chart, String name, double free, double used) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        DecimalFormat df = new DecimalFormat("####.##");
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
        data.setValueFormatter(new DiskValueFormater());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);
        chart.setCenterText(name);
        chart.setDescription("");

        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();
    }

}
