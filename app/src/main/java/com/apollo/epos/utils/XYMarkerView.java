
package com.apollo.epos.utils;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.apollo.epos.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class XYMarkerView extends MarkerView {

    // private TextView tvContent;
    private IAxisValueFormatter xAxisValueFormatter;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindColor(R.color.colorBlack)
    protected int blackColor;
    @BindColor(R.color.btn_color)
    protected int yellowColor;

    private DecimalFormat format;

    public XYMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.custom_marker_view);
        ButterKnife.bind(this);
        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = (TextView) findViewById(R.id.tvContent);
        format = new DecimalFormat("###.0");
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
//        if (e instanceof CandleEntry) {
//            CandleEntry ce = (CandleEntry) e;
//            Log.e("Marker", "Values : " + Utils.formatNumber(ce.getHigh(), 0, true) + ", High value: " + ce.getHigh());
//            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
//        } else {
//            Log.e("Marker", "Values of Y : " + Utils.formatNumber(e.getY(), 0, true) + ", High value: " + e.getY());
//            tvContent.setText("" + Utils.formatNumber(e.getY(), 0, true));
//        }
        tvContent.setText(ActivityUtils.convertSpannableStrings("Total Orders:",
                "" + (int) e.getY(), 1.0f, 1.1f, blackColor, yellowColor));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
