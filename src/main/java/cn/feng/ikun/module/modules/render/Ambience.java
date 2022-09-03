package cn.feng.ikun.module.modules.render;

import cn.feng.ikun.event.Event2D;
import cn.feng.ikun.event.EventPacketReceive;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.value.ListValue;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

public class Ambience extends Module {
    public Ambience() {
        super("Ambience", "Change world time.", Type.RENDER);
    }
    
    private static final ListValue Mode = new ListValue("Mode", new String[]{"Darkness", "Sunset", "Sunrise", "Day"}, "Day");
    
    @EventTarget
    public void on2D(Event2D e) {
        if (Mode.isCurrentMode("Darkness")) {
            mc.theWorld.setWorldTime(-18000);
        } else if (Mode.isCurrentMode("Sunset")) {
            mc.theWorld.setWorldTime(-13000);
        } else if (Mode.isCurrentMode("Day")) {
            mc.theWorld.setWorldTime(2000);
        } else if(Mode.isCurrentMode("Sunrise")) {
            mc.theWorld.setWorldTime(22500);
        }
    }

    @EventTarget
    public void onReceive(EventPacketReceive e) {
        if (e.packet instanceof S03PacketTimeUpdate) {
            e.setCancelled(true);
        }
    }
}
