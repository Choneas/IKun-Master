package cn.feng.ikun.ui.font;

import cn.feng.ikun.utils.misc.LogUtils;
import cn.feng.ikun.utils.misc.MC;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FontLoaders extends MC {

    public Map<String, HFontRenderer> fonts = new HashMap<>();
    public final HFontRenderer comfortaa16;
    public final HFontRenderer wqy13;
    public final HFontRenderer wqy16;
    public final HFontRenderer micon30;
    public final HFontRenderer micon15;

    public FontLoaders() {
        System.setProperty("java.awt.headless", "true");

        long time = System.currentTimeMillis();

        load("baloo", 12, 28);
        load("comfortaa", 12, 28);
        load("icon", 20, 30);
        load("flux", 12, 30);
        load("icon2", 20, 30);
        load("poppins", 12, 28);
        load("sans", 12, 28);
        load("Array", 10, 30);
        load("google", 10, 30);
        load("wqy", 10, 30);
        load("micon", 20, 30);
        comfortaa16 = get("comfortaa", 16);
        wqy13 = get("wqy", 13);
        wqy16 = get("wqy", 16);
        micon30 = get("micon", 30);
        micon15 = get("micon", 15);
        LogUtils.info("Fonts loading used " + (System.currentTimeMillis() - time) / 1000d + " seconds");
    }

    public HFontRenderer get(String name, int size) {
        if (fonts.get(name + size) == null) {
            fonts.put(name + size, getFont(name, size));
            LogUtils.info("Font " + name + " not found, loading default font");
        }

        return fonts.get(name + size);
    }

    public HFontRenderer get(String name) {
        if (fonts.get(name) == null) {
            String regEx = "\\D";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(name);
            int size = Integer.parseInt(m.replaceAll(""));
            fonts.put(name, getFont(name.replaceAll(m.replaceAll(""), ""), size));
        }

        return fonts.get(name);
    }

    private HFontRenderer getFont(String name, int size) {
        Font font;
        try {
            InputStream is = mc.getResourceManager().getResource(new ResourceLocation("ikun/font/" + name + ".ttf")).getInputStream();
            font = Font.createFont(0, Objects.requireNonNull(is)).deriveFont(Font.PLAIN, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtils.info("Error loading font");
            font = new Font("Arial", Font.PLAIN, size);
        }

        return new HFontRenderer(font, size, true);
    }

    private void load(String name, int minSize, int maxSize) {
        for (int i = minSize; i <= maxSize; ++i)
            fonts.put(name + i, getFont(name, i));
    }
}

