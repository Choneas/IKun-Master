package cn.feng.ikun.module.modules.movement;

import cn.feng.ikun.event.EventUpdate;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.utils.MoveUtil;
import cn.feng.ikun.value.ListValue;
import com.darkmagician6.eventapi.EventTarget;

public class Speed extends Module {
    public static ListValue mode = new ListValue("Mode", new String[]{"NCP", "HypixelLegit"}, "NCP");

    public Speed() {
        super("Speed", "Make you fast.", Type.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mode.isCurrentMode("NCP")) {
            if (MoveUtil.isMoving()) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionX *= 1.01D;
                    mc.thePlayer.motionZ *= 1.01D;
                    mc.thePlayer.speedInAir = 0.0223F;
                }

                mc.thePlayer.motionY -= 0.00099999D;

                MoveUtil.strafe();
            } else {
                mc.thePlayer.motionX = 0D;
                mc.thePlayer.motionZ = 0D;
            }
        }
        if (mode.isCurrentMode("HypixelLegit")) {
            mc.thePlayer.jumpTicks = 0;
            if (MoveUtil.isMoving()) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                } else {
                    mc.thePlayer.speedInAir = 0.0209F;
                }
            }
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}
