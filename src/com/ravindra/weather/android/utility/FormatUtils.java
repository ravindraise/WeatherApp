package com.ravindra.weather.android.utility;


import java.util.Locale;

public class FormatUtils {

	 public static final float CIRCLE_FULL = 360;
	 
	 public static final float CIRCLE_ZERO = 0;
    /**
     * Hidden constructor, to prevent instantiating.
     */
    protected FormatUtils() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }
    /**
     * Formats an angle (in °) to a string.
     * The number format is localized.
     *
     * @param angle Angle in °
     * @param precision number of decimals
     * @return formatted angle with unit (°)
     */
    public static String formatAngle(final double angle, final int precision) {
        if (precision < 0) {
            throw new IllegalArgumentException(
                    "Precision can't be a negative value");
        }

        String unit = "°";

        // generate format string
        // format number with variable precision (%s.xf), with x = precision
        String formatString = "%1$." + String.format("%d", precision) + "f";
        // add unit
        formatString += "%2$s";

        // formatting
        return String.format(Locale.getDefault(), formatString, angle, unit);
    }

    public static double normalizeAngle(final double angle) {
        float range = CIRCLE_FULL - CIRCLE_ZERO;
        double convertedAngle = angle;

        // returned value should be between 0° and 360°
        if (angle < CIRCLE_ZERO) {
            convertedAngle += range * Math.ceil(Math.abs(angle / range));
        } else if (angle >= CIRCLE_FULL) {
            convertedAngle -= range * Math.floor(angle / range);
        }

        return convertedAngle;
    }
   
}
