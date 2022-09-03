package cn.feng.ikun.module.modules.movement;

import cn.feng.ikun.event.EventUpdate;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.value.BoolValue;
import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "Toggle sprint.", Type.MOVEMENT);
    }

    private static final BoolValue forward = new BoolValue("Forward", true);
    private static final BoolValue back = new BoolValue("Back", false);
    private static final BoolValue lr = new BoolValue("Left && Right", false);
    private static final BoolValue noSneak = new BoolValue("No Sneak", true);

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.isSneaking() && noSneak.getValue()) return;
        if (mc.thePlayer.movementInput.moveForward > 0 && forward.getValue()) {
            mc.thePlayer.setSprinting(true);
        }
        if (mc.thePlayer.movementInput.moveForward < 0 && back.getValue()) {
            mc.thePlayer.setSprinting(true);
        }
        if (mc.thePlayer.movementInput.moveStrafe != 0 && lr.getValue()) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
