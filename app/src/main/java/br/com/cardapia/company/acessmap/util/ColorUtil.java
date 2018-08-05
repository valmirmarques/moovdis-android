package br.com.cardapia.company.acessmap.util;

import android.graphics.Color;

/**
 * Created by tvtios-01 on 02/11/17.
 */

public class ColorUtil {

    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }
}
