package com.waruna.editor.util;

import android.graphics.Color;

public class ColorHelper {
    public static String toRGBAString(int color) {
        // format: #RRGGBBAA
        String alpha = Integer.toHexString(Color.alpha(color));
        String red = Integer.toHexString(Color.red(color));
        String green = Integer.toHexString(Color.green(color));
        String blue = Integer.toHexString(Color.blue(color));
        if (alpha.length() == 1)
            alpha = "0" + alpha;
        if (red.length() == 1)
            red = "0" + red;
        if (green.length() == 1)
            green = "0" + green;
        if (blue.length() == 1)
            blue = "0" + blue;
        return "#"  + red + green + blue + alpha;
    }
}
