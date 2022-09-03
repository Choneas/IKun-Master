package cn.feng.ikun.ui.hud;

import cn.feng.ikun.Client;
import cn.feng.ikun.ui.font.HFontRenderer;
import cn.feng.ikun.utils.BlurUtil;
import cn.feng.ikun.utils.render.AnimationUtils;
import cn.feng.ikun.utils.render.Colors;
import cn.feng.ikun.utils.render.RenderUtils;
import cn.feng.ikun.utils.time.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;


public class Notification {
    private final String message;
    private final TimeHelper timer;
    private final int color;
    private final ResourceLocation image;
    private final long stayTime;
    Minecraft mc = Minecraft.getMinecraft();
    private double lastY;
    private double posY;
    private double width;
    private double height;
    private double animationX;
    private int imageWidth;

    public Notification(final String message, final Type type) {
        this.message = message;
        (this.timer = new TimeHelper()).reset();
        final HFontRenderer font = Client.instance.fontLoaders.get("sans", 16);
        this.width = font.getStringWidth(message) + 20;
        this.height = 20.0;
        this.animationX = this.width;
        this.stayTime = 2000L;
        this.imageWidth = 16;
        this.posY = -1.0;
        this.image = new ResourceLocation("ikun/notifications/" + type.name().toUpperCase() + ".png");
        this.color = Colors.BLACK.c;
    }

    public void draw(final double getY, final double lastY) {
        this.width = Client.instance.fontLoaders.get("sans", 16).getStringWidth(this.message) + 45;
        this.height = 22.0D;
        this.imageWidth = 11;
        this.lastY = lastY;
        this.animationX = AnimationUtils.getAnimationState((float) this.animationX, (float) (this.isFinished() ? this.width : 0.0D), (float) (Math.max(this.isFinished() ? 200 : 30, Math.abs(this.animationX - (this.isFinished() ? this.width : 0.0D)) * 20.0D) * 0.3D));
        if (this.posY == -1.0D) {
            this.posY = getY;
        } else {
            this.posY = AnimationUtils.getAnimationState((float) this.posY, (float) getY, 200.0F);
        }

        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int x1 = (int) ((double) res.getScaledWidth() - this.width + this.animationX);
        int x2 = (int) ((double) res.getScaledWidth() + this.animationX);
        int y1 = (int) this.posY - 22;
        int y2 = (int) ((double) y1 + this.height);
        RenderUtils.drawRect((float) x1, (float) y1, (float) x2, (float) y2, RenderUtils.reAlpha(this.color, 0.3F));
        BlurUtil.blurArea(x1, y1, x1, 22);
        RenderUtils.drawImage(this.image, (int) ((double) x1 + (this.height - (double) this.imageWidth) / 2.0D) - 1, y1 + (int) ((this.height - (double) this.imageWidth) / 2.0D), this.imageWidth, this.imageWidth);
        ++y1;
        if (this.message.contains(" Enabled")) {
            Client.instance.fontLoaders.get("sans", 16).drawString(this.message.replace(" Enabled", ""), (float) (x1 + 19), (float) ((double) y1 + this.height / 4.0D), -1);
            Client.instance.fontLoaders.get("sans", 16).drawString(" Enabled", (float) (x1 + 20 + Client.instance.fontLoaders.get("sans", 16).getStringWidth(this.message.replace(" Enabled", ""))), (float) ((double) y1 + this.height / 4.0D), Colors.GREY.c);
        } else if (this.message.contains(" Disabled")) {
            Client.instance.fontLoaders.get("sans", 16).drawString(this.message.replace(" Disabled", ""), (float) (x1 + 19), (float) ((double) y1 + this.height / 4.0D), -1);
            Client.instance.fontLoaders.get("sans", 16).drawString(" Disabled", (float) (x1 + 20 + Client.instance.fontLoaders.get("sans", 16).getStringWidth(this.message.replace(" Disabled", ""))), (float) ((double) y1 + this.height / 4.0D), Colors.GREY.c);
        } else {
            float var10002 = (float) (x1 + 20);
            float var10003 = (float) ((double) y1 + this.height / 4.0D);
            Client.instance.fontLoaders.get("sans", 16).drawString(this.message, var10002, var10003, -1);
        }}

    public boolean shouldDelete() {
        return this.isFinished() && this.animationX >= this.width;
    }

    private boolean isFinished() {
        return this.timer.timePassed(this.stayTime) && this.posY == this.lastY;
    }

    public double getHeight() {
        return this.height;
    }

    public enum Type {
        SUCCESS("SUCCESS", 0),
        INFO("INFO", 1),
        WARNING("WARNING", 2),
        ERROR("ERROR", 3);

        Type(final String s, final int n) {
        }
    }
}

