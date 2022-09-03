package cn.feng.ikun.ui.hud;

import cn.feng.ikun.Client;
import cn.feng.ikun.event.Event2D;
import cn.feng.ikun.module.modules.render.HUD;
import cn.feng.ikun.ui.hud.elements.Array;
import cn.feng.ikun.ui.hud.elements.Effects;
import cn.feng.ikun.ui.hud.elements.Hotbar;
import cn.feng.ikun.ui.hud.elements.WaterMark;
import cn.feng.ikun.utils.misc.MC;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.List;

public class HudManager extends MC {
    public static List<Element> elements = new ArrayList<>();
    public static ArrayList<Notification> notifications = new ArrayList<>();

    public HudManager() {
        Client.instance.eventManager.register(this);
        init();
    }

    public void drawNotifications() {
        ScaledResolution res = new ScaledResolution(mc);
        double startY = res.getScaledHeight() - 50;
        final double lastY = startY;
        for (int i = 0; i < notifications.size(); i++) {
            Notification not = notifications.get(i);
            if (not.shouldDelete()) {
                notifications.remove(i);
            } else if (notifications.size() > 10) {
                notifications.remove(i);
            }
            not.draw(startY, lastY);
            startY -= not.getHeight() + 3;
        }
    }

    public void showNotice(Notification notification) {
        notifications.add(notification);
    }

    private void init() {
        elements.add(new Hotbar());
        elements.add(new Array());
        elements.add(new WaterMark());
        elements.add(new Effects());
    }

    @EventTarget
    public void on2D(Event2D e) {
        if (Client.instance.moduleManager.getModule(HUD.class).isToggle()) {
            elements.forEach(it -> it.draw(e.getPartialTicks()));
            if (HUD.useNotify.getValue()) {
                drawNotifications();
            }
        }
    }
}
