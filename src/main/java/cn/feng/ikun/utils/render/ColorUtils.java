package cn.feng.ikun.utils.render;

import java.awt.*;

public enum ColorUtils {
    BLACK(-16711423),
    BLUE(-12028161),
    DARKBLUE(-12621684),
    GREEN(-9830551),
    DARKGREEN(-9320847),
    WHITE(-65794),
    AQUA(-7820064),
    DARKAQUA(-12621684),
    GREY(-9868951),
    DARKGREY(-14342875),
    RED(-65536),
    DARKRED(-8388608),
    ORANGE(-29696),
    DARKORANGE(-2263808),
    YELLOW(-256),
    DARKYELLOW(-2702025),
    MAGENTA(-18751),
    DARKMAGENTA(-2252579);

    public int c;

    ColorUtils(int co) {
        this.c = co;
    }

    public static int getColor(Color color) {
        return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getColor(int brightness) {
        return getColor(brightness, brightness, brightness, 255);
    }

    public static int getColor(int brightness, int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }

    public static int getColor(int red, int green, int blue) {
        return getColor(red, green, blue, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        byte color = 0;
        int color1 = color | alpha << 24;
        color1 |= red << 16;
        color1 |= green << 8;
        color1 |= blue;
        return color1;
    }

    //thanks thomaz <3
    public static int blendColors(final int[] Colors, final double progress) {
        final int size = Colors.length;
        if (progress == 1.f) return Colors[0];
        else if (progress == 0.f) return Colors[size - 1];
        final double mulProgress = Math.max(0, (1 - progress) * (size - 1));
        final int index = (int) mulProgress;
        return fadeBetween(Colors[index], Colors[index + 1], mulProgress - index);
    }

    public static int fadeBetween(int startColor, int endColor, double progress) {
        if (progress > 1) progress = 1 - progress % 1;
        return fadeTo(startColor, endColor, progress);
    }

    public static int fadeBetween(int startColor, int endColor, long offset) {
        return fadeBetween(startColor, endColor, ((System.currentTimeMillis() + offset) % 2000L) / 1000.0);
    }

    public static int fadeBetween(int startColor, int endColor) {
        return fadeBetween(startColor, endColor, 0L);
    }

    public static int fadeTo(int startColor, int endColor, double progress) {
        double invert = 1.0 - progress;
        int r = (int) ((startColor >> 16 & 0xFF) * invert +
                (endColor >> 16 & 0xFF) * progress);
        int g = (int) ((startColor >> 8 & 0xFF) * invert +
                (endColor >> 8 & 0xFF) * progress);
        int b = (int) ((startColor & 0xFF) * invert +
                (endColor & 0xFF) * progress);
        int a = (int) ((startColor >> 24 & 0xFF) * invert +
                (endColor >> 24 & 0xFF) * progress);
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF);
    }

    public static Color parseToColor(String c) {

        Color convertedColor = Color.ORANGE;

        try {

            convertedColor = new Color(Integer.parseInt(c, 16));

        } catch (NumberFormatException e) {

            e.getStackTrace();

        }

        return convertedColor;

    }

    public static String toHexEncoding(Color color) {

        String R, G, B;

        StringBuffer sb = new StringBuffer();

        R = Integer.toHexString(color.getRed());

        G = Integer.toHexString(color.getGreen());

        B = Integer.toHexString(color.getBlue());

        R = R.length() == 1 ? "0" + R : R;

        G = G.length() == 1 ? "0" + G : G;

        B = B.length() == 1 ? "0" + B : B;

        sb.append("0x");

        sb.append(R);

        sb.append(G);

        sb.append(B);

        return sb.toString();

    }
}
