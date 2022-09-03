package cn.feng.ikun.utils.misc;

import cn.feng.ikun.module.Module;
import cn.feng.ikun.ui.font.HFontRenderer;

import java.util.Comparator;

public class ModuleComparator extends MC implements Comparator<Object> {

    private final HFontRenderer fontRenderer;

    public ModuleComparator(HFontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
    }

    @Override
    public int compare(final Object o1, final Object o2) {
        final String name = ((Module) o1).getName() + ((Module) o1).getSuffix();
        final String name2 = ((Module) o2).getName() + ((Module) o2).getSuffix();


        return Float.compare(-fontRenderer.getStringWidth(name), -fontRenderer.getStringWidth(name2));
    }
}
