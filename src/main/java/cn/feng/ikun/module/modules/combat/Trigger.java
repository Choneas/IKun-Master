package cn.feng.ikun.module.modules.combat;

import cn.feng.ikun.Client;
import cn.feng.ikun.event.EventUpdate;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.utils.time.TimeHelper;
import cn.feng.ikun.value.FloatValue;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Trigger extends Module {
    public static FloatValue cps = new FloatValue("APS", 20.0f, 1.0f, 10.0f);

    private final TimeHelper timer = new TimeHelper();

    public Trigger() {
        super("Trigger", "Auto attack with the target which you selected.", Type.COMBAT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.theWorld == null || mc.objectMouseOver.entityHit == null)
            return;

        if (Client.instance.friendManager.isFriend(mc.objectMouseOver.entityHit.getName())) return;
        if (mc.objectMouseOver.entityHit instanceof EntityLivingBase && ((AntiBots) Client.instance.moduleManager.getModule(AntiBots.class)).isNPC((EntityLivingBase) mc.objectMouseOver.entityHit)) return;

        if (timer.timePassed((long) (1000.0000f / cps.getValue()))) {
            if (mc.thePlayer.isBlocking()) {
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.UP));
            }
            mc.thePlayer.swingItem();
            mc.playerController.attackEntity(mc.thePlayer, mc.objectMouseOver.entityHit);
            timer.reset();
        }
    }
}
