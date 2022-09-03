package cn.feng.ikun.module.modules.combat;

import cn.feng.ikun.event.EventPacketReceive;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.value.FloatValue;
import cn.feng.ikun.value.ListValue;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Module {
    private static final ListValue mode = new ListValue("Mode", new String[]{"Cancel", "Packet", "Legit"}, "Legit");
    public static FloatValue horizontal = new FloatValue("Horizontal", 100.0f, -100.0f, 0.0f);
    public static FloatValue vertical = new FloatValue("Vertical", 100.0f, 0.0f, 0.0f);
    public static FloatValue chance = new FloatValue("Chance", 100.0f, 0.0f, 0.0f);

    public Velocity() {
        super("Velocity", "Edit velocity when you hurt.", Type.COMBAT);
    }

    @EventTarget
    public void onPacketReceive(EventPacketReceive event) {
        if (mode.getValue().equals("Cancel")) event.setCancelled(true);
        if (mode.getValue().equals("Packet")) {
            if (event.getPacket() instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();

                if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                    packet.setMotionX((int) (packet.getMotionX() * horizontal.getValue() / 100.0));
                    packet.setMotionY((int) (packet.getMotionY() * vertical.getValue() / 100.0));
                    packet.setMotionZ((int) (packet.getMotionZ() * horizontal.getValue() / 100.0));
                }
            }

            //hypixel fucker
            if (event.getPacket() instanceof S27PacketExplosion) {
                handle(event);
            }
        }
        if (mode.isCurrentMode("Legit")) {
            if (event.getPacket() instanceof S12PacketEntityVelocity) {
                if (mc.thePlayer.maxHurtResistantTime != mc.thePlayer.hurtResistantTime || mc.thePlayer.maxHurtResistantTime == 0) {
                    return;
                }

                double random = Math.random();
                random *= 100.0;

                if (random < chance.getValue()) {
                    float hori = horizontal.getValue();
                    hori /= 100.0f;
                    float verti = vertical.getValue();
                    verti /= 100.0f;
                    mc.thePlayer.motionX *= hori;
                    mc.thePlayer.motionZ *= hori;
                    mc.thePlayer.motionY *= verti;
                } else {
                    mc.thePlayer.motionX *= 1.0f;
                    mc.thePlayer.motionY *= 1.0f;
                    mc.thePlayer.motionZ *= 1.0f;
                }
            }
        }
    }


    public void handle(EventPacketReceive event) {
        S27PacketExplosion packet = (S27PacketExplosion) event.getPacket();

        if (horizontal.getValue() == 0f && vertical.getValue() == 0f) {
            event.setCancelled(true);
        } else {
            packet.setMotionX(packet.getMotionX() * horizontal.getValue() / 100.0f);
            packet.setMotionY(packet.getMotionY() * vertical.getValue() / 100.0f);
            packet.setMotionZ(packet.getMotionZ() * horizontal.getValue() / 100.0f);
        }
    }
}
