package com.google.appinventor.components.runtime;

import android.os.Handler;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.ChartData;

public abstract class ChartView<C extends Chart, D extends ChartData> {
    protected C chart;
    protected D data;

    /**
     * Returns the underlying Chart view.
     *
     * @return  Chart object instance
     */
    public C getView() {
        return chart;
    }

    /**
     * Sets the background color of the Chart.
     * @param argb  background color
     */
    public void setBackgroundColor(int argb) {
        chart.setBackgroundColor(argb);
    }

    /**
     * Sets the description text of the Chart.
     * @param text  description text
     */
    public void setDescription(String text) {
        chart.getDescription().setText(text);
    }

    private Handler uiHandler = new Handler();

    /**
     * Refreshes the Chart to react to Data Set changes.
     */
    public void Refresh() {
        // Notify the Data component of data changes (needs to be called
        // when Datasets get changed directly)
        // TODO: Possibly move to ChartDataBase?
        chart.getData().notifyDataChanged();

        /*
         * Line Charts in the MPAndroidChart library (in v3.1.0) seem
         * to cause issues with asynchronous operations and refreshing,
         * namely when there are a few data sets present and they all invoke
         * the refresh method (a NegativeArraySizeException gets thrown).
         * In order to circumvent the non-deterministic crash issue,
         * the try&catch block simply skips the Refresh to avoid issues.
         */
        try {
            // Notify the Chart of Data changes (needs to be called
            // when Data objects get changed directly)
            chart.notifyDataSetChanged();

            // Invalidate the Chart on the UI thread (via the Handler)
            // The invalidate method should only be invoked on the UI thread
            // to prevent exceptions.
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    chart.invalidate();
                }
            });
        } catch (Exception e) {
            // Ignore current refresh
        }
    }

    /**
     * Creates a new Chart Model object instance.
     *
     * @return  Chart Model instance
     */
    public abstract ChartDataModel createChartModel();

    /**
     * Sets the necessary default settings for the Chart view.
     */
    protected void initializeDefaultSettings() {
        // Center the Legend
        chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
    }
}
