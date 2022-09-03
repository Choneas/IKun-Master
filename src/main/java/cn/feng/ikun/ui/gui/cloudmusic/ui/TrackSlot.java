package cn.feng.ikun.ui.gui.cloudmusic.ui;

import cn.feng.ikun.Client;
import cn.feng.ikun.ui.gui.cloudmusic.MusicManager;
import cn.feng.ikun.ui.gui.cloudmusic.impl.Track;
import cn.feng.ikun.utils.render.HanabiRenderUtils;
import cn.feng.ikun.utils.render.RenderUtils;

import java.awt.*;

public class TrackSlot {

    public Track track;
    public float x;
    public float y;

    public TrackSlot(Track t) {
        this.track = t;
    }

    public void render(float a, float b, int mouseX, int mouseY) {
        this.x = a;
        this.y = b;

        HanabiRenderUtils.drawRoundedRect(x, y, x + 137, y + 20, 2, 0xff34373c);

        Client.instance.fontLoaders.wqy16.drawString(track.name, x + 2, y + 1, Color.WHITE.getRGB());
        Client.instance.fontLoaders.wqy13.drawString(track.artists, x + 2, y + 10, Color.WHITE.getRGB());

        HanabiRenderUtils.drawRoundedRect(x + 122, y, x + 137, y + 20, 2, 0xff34373c);
        //HanabiRenderUtils.drawGradientSideways(x + 100, y, x + 124, y + 20, 0x00818181, 0xff34373c);

        Client.instance.fontLoaders.micon15.drawString("J", x + 125.5f, y + 5.5f, Color.WHITE.getRGB());

        //HanabiRenderUtils.drawOutlinedRect(x + 125, y + 5, x + 135, y + 15, .5f, Color.RED.getRGB());
    }

    public void click(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtils.isHovering(mouseX, mouseY, x + 125, y + 5, x + 135, y + 15) && mouseButton == 0) {
            MusicManager.INSTANCE.play(track);
        }
    }
}
