package com.myappcompany.steve.gameoflifeandroid;

import android.graphics.Color;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ColorUtil {
    //currently strips alpha information

    String rgbColorString;
    int R, G, B, A;
    double Rp, Gp, Bp;
    double Cmin, Cmax, Delta;
    double Hdeg, Hrad, S, V;

    public ColorUtil(int color) {
        //converts an integer color into a hexadecimal string RRGGBB
        this(String.format("#%06X", (0xFFFFFF & color)));
    }

    public ColorUtil(String rgbColorString) {
        if(rgbColorString.charAt(0) == '#') {
            rgbColorString = rgbColorString.substring(1);

            //add alpha values if none
            if(rgbColorString.length() == 6) {
                rgbColorString = "FF" + rgbColorString;
            }
        }
        this.rgbColorString = rgbColorString;
        R = stringToR(rgbColorString);
        G = stringToG(rgbColorString);
        B = stringToB(rgbColorString);

        Rp = ((float) R) /(255d);
        Gp = ((float) G) /(255d);
        Bp = ((float) B) /(255d);

        Cmax = max(max(Rp, Gp), Bp);
        Cmin = min(min(Rp, Gp), Bp);
        Delta = Cmax - Cmin;

        calculateHdeg();
        Hrad = Hdeg * (Math.PI / 180d);
        V = (Cmax + Cmin) / 2d;
        S = (Delta == 0 ? 0 : Delta/(1d - Math.abs(2d*V - 1d)));
    }

    private void calculateHdeg() {
        if(Delta == 0) {
            Hdeg = 0;
        } else if(Cmax == Rp) {
            Hdeg = 60 * (((Gp - Bp) / Delta) % 6d);
        } else if(Cmax == Gp) {
            Hdeg = 60 * (((Bp - Rp) / Delta) + 2d);
        } else {
            Hdeg = 60 * (((Rp - Gp) / Delta) + 4d);
        }

        while(Hdeg < 0 ) {
            Hdeg += 360;
        }
    }

    public int stringToR(String rgbColorString) {
        //AARRGGBB
        //01234567
        String redHexString = rgbColorString.substring(2,4);
        return Integer.parseInt(redHexString, 16);
    }

    public int stringToG(String rgbColorString) {
        //AA RR GG BB
        //01 23 45 67
        String greenHexString = rgbColorString.substring(4,6);
        return Integer.parseInt(greenHexString, 16);
    }

    public int stringToB(String rgbColorString) {
        //AA RR GG BB
        //01 23 45 67
        String blueHexString = rgbColorString.substring(6);
        return Integer.parseInt(blueHexString, 16);
    }

    public String printRGB() {
        return "(" + R + "," + G + "," + B + ")";
    }

    public String printHSV(String radOrDeg) {

        if(radOrDeg.equals("rad")) {
            return "(" + Hrad + "," + Math.round(S * 100d) + "%," + Math.round(V * 100d) + "%)";
        } else {
            return "(" + Hdeg + "°," + Math.round(S * 100d) + "%," + Math.round(V * 100d) + "%)";
        }
    }

    public String printHSV() {
        return printHSV("deg");
    }

    @Override
    public String toString() {
        return printRGB();
    }

    public int toInt() {
        //strip the alpha info. If you want to include alpha values will have to switch to a long.
        return Color.parseColor("#" + rgbColorString);
    }

    public long toLong() {
        return Long.parseLong(rgbColorString, 16);
    }
}
