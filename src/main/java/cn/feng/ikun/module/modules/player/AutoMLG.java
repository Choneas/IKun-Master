package cn.feng.ikun.module.modules.player;

import cn.feng.ikun.event.EventUpdate;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.utils.misc.BlockUtils;
import cn.feng.ikun.value.IntValue;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldSettings;

public class AutoMLG extends Module {

    public AutoMLG() {
        super("AutoMLG", "Auto place water when you falling down.", Type.PLAYER);
    }

    private static final IntValue delay = new IntValue("Delay", 1000, 1, 100);

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.fallDistance > 4 && getSlotWaterBucket() != -1 && isMLGNeeded()) {
            mc.thePlayer.rotationPitch = 90f;
            swapToWaterBucket(getSlotWaterBucket());
        }

        if (mc.thePlayer.fallDistance > 4 && isMLGNeeded() && !mc.thePlayer.isOnLadder() && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - BlockUtils.getDistanceToFall() - 1, mc.thePlayer.posZ);
            this.placeWater(pos, EnumFacing.UP);


            if (mc.thePlayer.getHeldItem().getItem() == Items.bucket) {
                Thread thr = new Thread(() -> {
                    try {
                        Thread.sleep(delay.getValue());
                    } catch (Exception ignored) {
                    }
                    mc.rightClickMouse();
                });
                thr.start();
            }

            mc.thePlayer.fallDistance = 0;
        }
    }

    private void swapToWaterBucket(int blockSlot) {
        mc.thePlayer.inventory.currentItem = blockSlot;
        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C09PacketHeldItemChange(blockSlot));
    }

    private int getSlotWaterBucket() {
        for (int i = 0; i < 8; i++) {
            if (mc.thePlayer.inventory.mainInventory[i] != null && mc.thePlayer.inventory.mainInventory[i].getItem().getUnlocalizedName().contains("bucketWater"))
                return i;
        }
        return -1;
    }

    private void placeWater(BlockPos pos, EnumFacing facing) {
        ItemStack heldItem = mc.thePlayer.inventory.getCurrentItem();
        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), pos, facing, new Vec3((double) pos.getX() + 0.5, (double) pos.getY() + 1, (double) pos.getZ() + 0.5));
        if (heldItem != null) {
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, heldItem);
            mc.entityRenderer.itemRenderer.resetEquippedProgress2();
        }
    }

    private boolean isMLGNeeded() {
        if (mc.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE || mc.playerController.getCurrentGameType() == WorldSettings.GameType.SPECTATOR || mc.thePlayer.capabilities.isFlying || mc.thePlayer.capabilities.allowFlying)
            return false;

        for (double y = mc.thePlayer.posY; y > 0.0; --y) {
            final Block block = BlockUtils.getBlock(new BlockPos(mc.thePlayer.posX, y, mc.thePlayer.posZ));
            if (block.getMaterial() == Material.water) {
                return false;
            }

            if (block.getMaterial() != Material.air)
                return true;

            if (y < 0.0) {
                break;
            }
        }

        return true;
    }
}
