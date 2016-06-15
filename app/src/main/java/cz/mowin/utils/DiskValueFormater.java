package cz.mowin.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Formates values used in Disk charts
 */
public class DiskValueFormater implements ValueFormatter {

    private DecimalFormat mFormat;

    /**
     * Get formater which formats uses space to one decimal
     */
    public DiskValueFormater() {
        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value);
    }
}