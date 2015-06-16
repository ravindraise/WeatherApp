package com.ravindra.weather.android.utility;


import android.content.Context;

public abstract class AbstractGeoCoordinate {
    /**
     * Unformatted coordinate value.
     */
    private double value;

    /**
     * Current context.
     */
    private Context mContext = null;

    /**
     * Lower value of allowed value range.
     */
    private double mRangeLow;

    /**
     * Higher value of allowed value range.
     */
    private double mRangeHigh;

    /**
     * Constructor.
     *
     * @param newValue New value for unformatted value.
     */
    AbstractGeoCoordinate(final double newValue) {
        init();
        setValue(newValue);
    }

    /**
     * Constructor.
     *
     * @param context App Context.
     * @param newValue New value for unformatted value.
     */
    AbstractGeoCoordinate(final Context context, final double newValue) {
        setContext(context);
        init();
        setValue(newValue);
    }

    /**
     * Initialize coordinate value range.
     */
    protected abstract void init();

    /**
     * Set coordinate value range.
     *
     * @param rangeLow Lower limit of allowed range
     * @param rangeHigh Higher limit of allowed range
     */
    final void setRange(
            final double rangeLow,
            final double rangeHigh) {
        mRangeLow = rangeLow;
        mRangeHigh = rangeHigh;
    }

    /**
     * Set unformatted value.
     *
     * @param newValue New value for unformatted value.
     * @throws IllegalArgumentException if new value is out of range.
     */
    public final void setValue(final double newValue) {
        if (checkRange(newValue)) {
            value = newValue;
        } else {
            throw new IllegalArgumentException(
                    "newValue is not in range "
                            + mRangeLow + " .. " + mRangeHigh);
        }
    }

    /**
     * Get unformatted value.
     *
     * @return Unformatted value.
     */
    public final double getValue() {
        return value;
    }

    /**
     * Set current context.
     *
     * @param context Current context
     */
    final void setContext(final Context context) {
        if (context != null) {
            mContext = context;
        }
    }

    /**
     * Get current context.
     *
     * @return Current context
     */
    final Context getContext() {
        return mContext;
    }

    /**
     * Check if submitted value is within the allowed range.
     *
     * @param coordinate coordinate value
     * @return true if coordinate is within range
     */
    private boolean checkRange(final double coordinate) {
        return coordinate <= mRangeHigh && coordinate >= mRangeLow;
    }

    /**
     * Convert coordinate value according to segment.
     *
     * @return converted coordinate value
     */
    protected abstract double getConvertedValue();

    /**
     * Format an unformatted angle to a GeoCoordinate.
     *
     * @return String formatted string
     */
    public final String format() {
        return  getSegmentUnit();
    }

    /**
     * Format value.
     *
     * @return formatted value
     */
    protected abstract String formatValue();

    /**
     * Determine value segment.
     *
     * @return segment code
     */
    public abstract int getSegment();

    /**
     * Get segment unit.
     *
     * @return unit
     */
    public abstract String getSegmentUnit();
}
