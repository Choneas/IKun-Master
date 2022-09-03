package cn.feng.ikun.module.modules.combat;

import cn.feng.ikun.event.EventAttack;
import cn.feng.ikun.event.EventUpdate;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.utils.MathUtils;
import cn.feng.ikun.utils.misc.LogUtils;
import cn.feng.ikun.utils.time.TimeHelper;
import cn.feng.ikun.value.ListValue;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.Random;

/**
 * @author: Thr1c0/s
 * @date: 2022/8/5 13:06
 */
public class Critical extends Module {
    public static ListValue mode = new ListValue("Mode", new String[]{"Unchecked", "Hypixel"}, "Hypixel");
    TimeHelper timeHelper = new TimeHelper();

    public Critical() {
        super("Critical", "Make each of your hits critical.", Type.COMBAT);
    }

    @EventTarget
    public void onAttack(EventAttack event) {
        if (mc.thePlayer.onGround) {
            if (timeHelper.delay(500)) {//神必数字
                if (mode.isCurrentMode("Hypixel")) {
                    onHypixelCrit();
                }
                if (mode.isCurrentMode("Unchecked")) {
                    onCrit();
                }
                mc.thePlayer.onCriticalHit(event.getEntity());
                timeHelper.reset();
            }
        }
    }

    public void onHypixelCrit() {
        for (double d : new double[]{0.00430602200102120014, 0.0410881200712020195, 0.00511681200107142817, 0.00231121200209234615}) {
            mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + d, mc.thePlayer.posZ, false));
        }
    }

    public void onCrit() {
        double j = MathUtils.getRandomInRange(4.0E-7, 4.0E-5);

        for (double d : new double[]{0.0425 + j, 0.00150000001304 + j, 0.01400000001304 + j}) {
            mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + d, mc.thePlayer.posZ, false));
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}
