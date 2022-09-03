package cn.feng.ikun.ui.hud.elements;

import cn.feng.ikun.Client;
import cn.feng.ikun.ui.hud.Element;

import java.awt.*;

public class WaterMark extends Element {
    @Override
    public void draw(float partialTicks) {
        Client.instance.fontLoaders.get("Array", 20).drawStringWithShadow("IKun Client", 5, 5, Color.WHITE.getRGB());
        Client.instance.fontLoaders.get("baloo", 12).drawStringWithShadow(Client.instance.ver, 8 + Client.instance.fontLoaders.get("Array", 20).getStringWidth("IKun Client"), 2, Color.WHITE.getRGB());
    }
}
