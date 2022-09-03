package cn.feng.ikun.event;

import cn.feng.ikun.utils.RotationUtils;
import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class EventPacketSend extends EventCancellable {
    public Packet<?> packet;

    public EventPacketSend(Packet<?> packet) {
        this.packet = packet;
        if (packet instanceof C03PacketPlayer.C05PacketPlayerLook || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
            float yaw = ((C03PacketPlayer) packet).getYaw();
            float pitch = ((C03PacketPlayer) packet).getPitch();
            RotationUtils.serverRotation.setYaw(yaw);
            RotationUtils.serverRotation.setPitch(pitch);
        }
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }
}
