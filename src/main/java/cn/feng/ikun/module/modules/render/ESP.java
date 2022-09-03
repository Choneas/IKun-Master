package cn.feng.ikun.module.modules.render;

import cn.feng.ikun.event.Event3D;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ESP extends Module {
    public ESP() {
        super("ESP", "Draw a box on every player.", Type.RENDER);
    }

    private static void drawVerticalLine(double xPos, double yPos, double xSize, double thickness, Color color) {
        Tessellator tesselator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tesselator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(xPos - xSize, yPos - thickness / 2.0D, 0.0D).color((float) color.getRed() / 255.0F,
                        (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        worldRenderer.pos(xPos - xSize, yPos + thickness / 2.0D, 0.0D).color((float) color.getRed() / 255.0F,
                        (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        worldRenderer.pos(xPos + xSize, yPos + thickness / 2.0D, 0.0D).color((float) color.getRed() / 255.0F,
                        (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        worldRenderer.pos(xPos + xSize, yPos - thickness / 2.0D, 0.0D).color((float) color.getRed() / 255.0F,
                        (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        tesselator.draw();
    }

    @EventTarget
    public void on3D(Event3D e) {
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (!(o instanceof EntityLivingBase) || o == mc.thePlayer)
                continue;
            EntityLivingBase ent = (EntityLivingBase) o;
            doCornerESP(ent);
            GlStateManager.resetColor();
        }
    }

    private void doCornerESP(EntityLivingBase entity) {
        if (!entity.isInvisible()) {
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glDisable(2929);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.enableBlend();
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
            float partialTicks = mc.timer.renderPartialTicks;
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks
                    - RenderManager.renderPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks
                    - RenderManager.renderPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks
                    - RenderManager.renderPosZ;
            float DISTANCE = mc.thePlayer.getDistanceToEntity(entity);
            float DISTANCE_SCALE = Math.min(DISTANCE * 0.15F, 2.5F);
            float SCALE = 0.035F;
            SCALE /= 2.0F;
            GlStateManager.translate((float) x,
                    (float) y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float) z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(-SCALE, -SCALE, -SCALE);
            Color color = new Color(-13330213);
            if (entity.hurtTime > 0) {
                color = new Color(255, 0, 0);
            }
            double thickness = 2.0F + DISTANCE * 0.08F;
            double xLeft = -30.0D;
            double xRight = 30.0D;
            double yUp = (entity.isSneaking()) ? 28 : 18.0D;
            double yDown = 140.0D;
            double size = 10.0D;
            drawVerticalLine(xLeft + size / 2.0D, yUp, size / 2.0D, thickness, color);
            drawHorizontalLine(xLeft, yUp + size - 1, size, thickness, color);
            drawVerticalLine(xRight - size / 2.0D, yUp, size / 2.0D, thickness, color);
            drawHorizontalLine(xRight, yUp + size - 1, size, thickness, color);
            drawVerticalLine(xLeft + size / 2.0D, yDown, size / 2.0D, thickness, color);
            drawHorizontalLine(xLeft, yDown - size + 1, size, thickness, color);
            drawVerticalLine(xRight - size / 2.0D, yDown, size / 2.0D, thickness, color);
            drawHorizontalLine(xRight, yDown - size + 1, size, thickness, color);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GlStateManager.disableBlend();
            GL11.glDisable(3042);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glNormal3f(1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

    private void drawHorizontalLine(double xPos, double yPos, double ySize, double thickness, Color color) {
        Tessellator tesselator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tesselator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(xPos - thickness / 2.0D, yPos - ySize, 0.0D).color((float) color.getRed() / 255.0F,
                        (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        worldRenderer.pos(xPos - thickness / 2.0D, yPos + ySize, 0.0D).color((float) color.getRed() / 255.0F,
                        (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        worldRenderer.pos(xPos + thickness / 2.0D, yPos + ySize, 0.0D).color((float) color.getRed() / 255.0F,
                        (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        worldRenderer.pos(xPos + thickness / 2.0D, yPos - ySize, 0.0D).color((float) color.getRed() / 255.0F,
                        (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        tesselator.draw();
    }
}
