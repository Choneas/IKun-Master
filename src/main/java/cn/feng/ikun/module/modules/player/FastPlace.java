package cn.feng.ikun.module.modules.player;

import cn.feng.ikun.event.EventUpdate;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.value.FloatValue;
import com.darkmagician6.eventapi.EventTarget;

public class FastPlace extends Module {

    public static FloatValue speed = new FloatValue("Speed", 6, 0, 1);

    public FastPlace() {
        super("FastPlace", "place blocks fast af", Type.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        mc.setRightClickDelayTimer(Math.min(mc.getRightClickDelayTimer(), (int) speed.getValue()));
    }
}
