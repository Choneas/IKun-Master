package cn.feng.ikun.ui.hud.elements;

import cn.feng.ikun.Client;
import cn.feng.ikun.module.modules.render.HUD;
import cn.feng.ikun.ui.hud.Element;
import cn.feng.ikun.utils.render.HotbarUtils;
import cn.feng.ikun.utils.render.PaletteUtil;
import cn.feng.ikun.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.awt.*;

import static net.minecraft.client.gui.GuiIngame.widgetsTexPath;

public class Hotbar extends Element {
    @Override
    public void draw(float partialTicks) {
        if (!HUD.useHotbar.getValue()) return;

        if (Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer) {
            Minecraft mc = Minecraft.getMinecraft();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(widgetsTexPath);
            EntityPlayer entityplayer = (EntityPlayer) mc.getRenderViewEntity();
            int i = sr.getScaledWidth() / 2;
            int itemX = i - 91 + HotbarUtils.getHotbarEasePos(entityplayer.inventory.currentItem * 20);
            GlStateManager.disableTexture2D();
            RenderUtils.drawRect(i - 91, sr.getScaledHeight() - 22, i + 91, sr.getScaledHeight(), new Color(0, 0, 0, HUD.hotbarAlpha.getValue()));
            RenderUtils.drawRect(itemX, sr.getScaledHeight() - 22, itemX + 22, sr.getScaledHeight() - 21, PaletteUtil.fade(Color.WHITE).getRGB());
            RenderUtils.drawRect(itemX, sr.getScaledHeight() - 21, itemX + 22, sr.getScaledHeight(), new Color(0, 0, 0, HUD.hotbarAlpha.getValue()));
            GlStateManager.enableTexture2D();


            RenderUtils.drawRect(0, sr.getScaledHeight() - 22, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, HUD.infoAlpha.getValue()));

            //info
            Client.instance.fontLoaders.get("poppins", 15).drawStringWithShadow("§6X: §f" + (int) mc.thePlayer.posX + " §6Y: §f" + (int) mc.thePlayer.posY + " §6Z: §f" + (int) mc.thePlayer.posZ, 5, sr.getScaledHeight() - 14, Color.WHITE.getRGB());
            Client.instance.fontLoaders.get("poppins", 15).drawStringWithShadow("Welcome, " + mc.session.getUsername(), sr.getScaledWidth() - 5 - Client.instance.fontLoaders.get("poppins", 15).getStringWidth("Welcome, " + mc.session.getPlayerID()), sr.getScaledHeight() - 14, Color.WHITE.getRGB());
        }
        RenderHelper.enableGUIStandardItemLighting();

        for (int j = 0; j < 9; ++j) {
            int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
            int l = sr.getScaledHeight() - 16 - 3;
            this.customRenderHotbarItem(j, k, l, partialTicks, mc.thePlayer);
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
    }


    public void customRenderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer p_175184_5_) {

        GlStateManager.disableBlend();

        ItemStack itemstack = p_175184_5_.inventory.mainInventory[index];

        if (itemstack != null) {
            float f = (float) itemstack.animationsToGo - partialTicks;

            if (f > 0.0F) {
                GlStateManager.pushMatrix();
                float f1 = 1.0F + f / 5.0F;
                GlStateManager.translate((float) (xPos + 8), (float) (yPos + 12), 0.0F);
                GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((float) (-(xPos + 8)), (float) (-(yPos + 12)), 0.0F);
            }

            mc.getRenderItem().renderItemAndEffectIntoGUI(itemstack, xPos, yPos);

            if (f > 0.0F) {
                GlStateManager.popMatrix();
            }

            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemstack, xPos - 1, yPos);
        }
    }
}
