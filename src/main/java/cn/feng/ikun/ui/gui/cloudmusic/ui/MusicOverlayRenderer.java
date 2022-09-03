package cn.feng.ikun.ui.gui.cloudmusic.ui;

import cn.feng.ikun.Client;
import cn.feng.ikun.module.modules.render.HUD;
import cn.feng.ikun.ui.font.HFontRenderer;
import cn.feng.ikun.ui.gui.cloudmusic.MusicManager;
import cn.feng.ikun.utils.render.Colors;
import cn.feng.ikun.utils.render.HanabiRenderUtils;
import cn.feng.ikun.utils.render.RenderUtils;
import cn.feng.ikun.utils.time.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public enum MusicOverlayRenderer {
    INSTANCE;

    public String downloadProgress = "0";

    public long readedSecs = 0;
    public long totalSecs = 0;

    public float animation = 0;

    public TimeHelper timer = new TimeHelper();

    public boolean firstTime = true;

    public Client hanaInstance = Client.instance;

    public void renderOverlay() {
        int addonX = HUD.musicPosX.getValue();
        int addonY = HUD.musicPosY.getValue();
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        if (MusicManager.INSTANCE.getCurrentTrack() != null && MusicManager.INSTANCE.getMediaPlayer() != null) {
            readedSecs = (int) MusicManager.INSTANCE.getMediaPlayer().getCurrentTime().toSeconds();
            totalSecs = (int) MusicManager.INSTANCE.getMediaPlayer().getStopTime().toSeconds();
        }

        if (MusicManager.INSTANCE.getCurrentTrack() != null && MusicManager.INSTANCE.getMediaPlayer() != null) {
            hanaInstance.fontLoaders.get("google", 18).drawString(MusicManager.INSTANCE.getCurrentTrack().name + " - " + MusicManager.INSTANCE.getCurrentTrack().artists, 36f + addonX, 10 + addonY, Colors.WHITE.c);
            hanaInstance.fontLoaders.get("google", 18).drawString(formatSeconds((int) readedSecs) + "/" + formatSeconds((int) totalSecs), 36f + addonX, 20 + addonY, 0xffffffff);

            if (MusicManager.INSTANCE.circleLocations.containsKey(MusicManager.INSTANCE.getCurrentTrack().id)) {
                GL11.glPushMatrix();
                GL11.glColor4f(1, 1, 1, 1);
                ResourceLocation icon = MusicManager.INSTANCE.circleLocations.get(MusicManager.INSTANCE.getCurrentTrack().id);
                HanabiRenderUtils.drawImage(icon, 4 + addonX, 6 + addonY, 28, 28);
                GL11.glPopMatrix();
            } else {
                MusicManager.INSTANCE.getCircle(MusicManager.INSTANCE.getCurrentTrack());
            }

            try {
                float currentProgress = (float) (MusicManager.INSTANCE.getMediaPlayer().getCurrentTime().toSeconds() / Math.max(1, MusicManager.INSTANCE.getMediaPlayer().getStopTime().toSeconds())) * 100;
                HanabiRenderUtils.drawArc(18 + addonX, 19 + addonY, 14, Colors.WHITE.c, 0, 360, 4);
                HanabiRenderUtils.drawArc(18 + addonX, 19 + addonY, 14, Colors.BLUE.c, 180, 180 + (currentProgress * 3.6f), 4);
            } catch (Exception ignored) {
            }
        }

        if (MusicManager.INSTANCE.lyric) {
            {
                HFontRenderer lyricFont = Client.instance.fontLoaders.get("google", 18);
                int addonYlyr = HUD.musicPosYlyr.getValue();
                //Lyric
                int col = MusicManager.INSTANCE.tlrc.isEmpty() ? Colors.GREY.c : 0xff00af87;

                GlStateManager.disableBlend();
                lyricFont.drawCenteredString(MusicManager.INSTANCE.lrcCur.contains("_EMPTY_") ? "等待中......." : MusicManager.INSTANCE.lrcCur, sr.getScaledWidth() / 2f - 0.5f, sr.getScaledHeight() - 140 - 80 + addonYlyr, 0xff00af87);
                lyricFont.drawCenteredString(MusicManager.INSTANCE.tlrcCur.contains("_EMPTY_") ? "Waiting......." : MusicManager.INSTANCE.tlrcCur, sr.getScaledWidth() / 2f, sr.getScaledHeight() - 125 + 0.5f - 80 + addonYlyr, col);
                GlStateManager.enableBlend();
            }
        }

        if ((MusicManager.showMsg)) {
            if (firstTime) {
                timer.reset();
                firstTime = false;
            }

            HFontRenderer wqy = Client.instance.fontLoaders.get("google", 18);
            HFontRenderer sans = Client.instance.fontLoaders.get("sans", 25);

            float width1 = wqy.getStringWidth(MusicManager.INSTANCE.getCurrentTrack().name);
            float width2 = sans.getStringWidth("Now playing");
            float allWidth = (Math.max(Math.max(width1, width2), 150));

            HanabiRenderUtils.drawRect(sr.getScaledWidth() - animation, 5, sr.getScaledWidth(), 40, RenderUtils.reAlpha(Colors.BLACK.c, 0.7f));

            if (MusicManager.INSTANCE.circleLocations.containsKey(MusicManager.INSTANCE.getCurrentTrack().id)) {
                GL11.glPushMatrix();
                GL11.glColor4f(1, 1, 1, 1);
                ResourceLocation icon = MusicManager.INSTANCE.circleLocations.get(MusicManager.INSTANCE.getCurrentTrack().id);
                HanabiRenderUtils.drawImage(icon, sr.getScaledWidth() - animation + 5, 8, 28, 28);
                GL11.glPopMatrix();
            } else {
                MusicManager.INSTANCE.getCircle(MusicManager.INSTANCE.getCurrentTrack());
            }

            HanabiRenderUtils.drawArc(sr.getScaledWidth() - animation - 31 + 50, 22, 14, Colors.WHITE.c, 0, 360, 2);

            sans.drawString("Now playing", sr.getScaledWidth() - animation - 12 + 50, 8, Colors.WHITE.c);
            wqy.drawString(MusicManager.INSTANCE.getCurrentTrack().name, sr.getScaledWidth() - animation - 12 + 50, 26, Colors.WHITE.c);

            if (timer.timePassed(5000)) {
                this.animation = (float) HanabiRenderUtils.getAnimationStateSmooth(0, animation, 10.0f / Minecraft.getDebugFPS());
                if (this.animation <= 0) {
                    MusicManager.showMsg = false;
                    firstTime = true;
                }
            } else {
                this.animation = (float) HanabiRenderUtils.getAnimationStateSmooth(allWidth, animation, 10.0f / Minecraft.getDebugFPS());
            }

        }

        GlStateManager.resetColor();
    }

    public String formatSeconds(int seconds) {
        String rstl = "";
        int mins = seconds / 60;
        if (mins < 10) {
            rstl += "0";
        }
        rstl += mins + ":";
        seconds %= 60;
        if (seconds < 10) {
            rstl += "0";
        }
        rstl += seconds;
        return rstl;
    }
}
