package cn.feng.ikun.utils.misc;

import cn.feng.ikun.event.EventTick;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class MC {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    protected static ScaledResolution sr = new ScaledResolution(mc);

    @EventTarget
    public void onTick(EventTick e) {
        sr = new ScaledResolution(mc);
    }
}
