package cn.feng.ikun.module.modules.movement;

import cn.feng.ikun.event.EventPacketReceive;
import cn.feng.ikun.event.EventUpdate;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.utils.PlayerUtils;
import cn.feng.ikun.value.ListValue;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * @author: Thr1c0/s
 * @date: 2022/8/3 19:27
 */


public class NoSlow extends Module {
    public static ListValue mode = new ListValue("Mode", new String[]{"Hypixel", "Vanilla", "NCP"}, "Hypixel");
    private ItemStack[] itemStacks;

    public NoSlow() {
        super("NoSlow", "No slowdown.", Type.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate a) {
        if (mode.getValue().equals("NCP") && mc.thePlayer.isBlocking() && PlayerUtils.isMoving() && mc.thePlayer.onGround) {
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
        }

        if (mode.getValue().equals("Hypixel") && this.Method713()) {
            //  mc.getNetHandler().sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem < 8 ? mc.thePlayer.inventory.currentItem + 1 : mc.thePlayer.inventory.currentItem - 1));
            mc.getNetHandler().sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
    }

    @EventTarget
    public void onPacket(EventPacketReceive a) {
        if (mode.getValue().equals("Hypixel") && a.getPacket() instanceof S30PacketWindowItems && ((S30PacketWindowItems) a.getPacket()).func_148911_c() == 0) {
            this.itemStacks = ((S30PacketWindowItems) a.getPacket()).getItemStacks();
            if (this.itemStacks != null && this.itemStacks == ((S30PacketWindowItems) a.getPacket()).getItemStacks()) {
                if (this.Method713()) {
                    mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                    a.setCancelled(true);
                }

                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
            }
        }
    }

    public boolean Method713() {
        if (mc.thePlayer.getItemInUseCount() >= 520.0F) {
            return false;
        } else if (mc.thePlayer.getItemInUseCount() > 0.0F && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            return true;
        } else {
            return mc.thePlayer.getItemInUseCount() > 0.0F && (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion);
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}
