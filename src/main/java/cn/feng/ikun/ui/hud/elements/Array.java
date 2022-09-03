package cn.feng.ikun.ui.hud.elements;

import cn.feng.ikun.Client;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.modules.render.HUD;
import cn.feng.ikun.ui.font.HFontRenderer;
import cn.feng.ikun.ui.hud.Element;
import cn.feng.ikun.utils.BlurUtil;
import cn.feng.ikun.utils.render.AnimationUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class Array extends Element {
    public static final int SECONDARY = new Color(23, 23, 23, 120).getRGB();
    public static int MAIN;
    HFontRenderer font = Client.instance.fontLoaders.get("poppins", 17);

    @Override
    public void draw(float partialTicks) {
        if (!HUD.useArray.getValue()) return;
        int rainbowTick = 0;
        GlStateManager.pushMatrix();

        int yStart = 6;

        ScaledResolution sr = new ScaledResolution(mc);

        List<Module> mods = Client.instance.moduleManager.getEnabledModList();
        mods.sort((o1, o2) -> font.getStringWidth(o2.getSuffix() == null ? o2.getName() : o2.getName() + "," + o2.getSuffix()) - font.getStringWidth(o1.getSuffix() == null ? o1.getName() : o1.getName() + "," + o1.getSuffix()));

        for (Module module : mods) {
            MAIN = new Color(Color.HSBtoRGB((float) ((double) mc.thePlayer.ticksExisted / 50.0 + Math.sin((double) rainbowTick / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f)).getRGB();
            int startX = sr.getScaledWidth() - font.getStringWidth(module.getSuffix() == null ? module.getName() : module.getName() + "," + module.getSuffix()) - 5;
            // Gui.drawRect(sr.getScaledWidth() - 1, yStart - 1, sr.getScaledWidth(), yStart + 10, SECONDARY);
            BlurUtil.blurArea(startX, 5, sr.getScaledWidth() - 20, yStart + 5);

            font.drawStringWithShadow(module.getName(), startX + (!module.getSuffix().equals("") ? 0 : 3), yStart + 1, MAIN);
            if (!Objects.equals(module.getSuffix(), "")) {
                font.drawStringWithShadow(module.getSuffix(), startX + 1 + font.getStringWidth(module.getName() + ","), yStart + 1, Color.WHITE.darker().getRGB());
            }
//            BlurUtil.blurArea(startX, 5, sr.getScaledWidth() - 20, yStart + 5);

            if (++rainbowTick > 50) {
                rainbowTick = 0;
            }

            yStart = (int) AnimationUtil.calculateCompensation(yStart += 11, yStart, 1, 1);
        }

        GlStateManager.popMatrix();

        RenderHelper.enableGUIStandardItemLighting();

        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1);
    }
}
