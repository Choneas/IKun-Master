package cn.feng.ikun.module.modules.player;

import cn.feng.ikun.event.EventUpdate;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.utils.time.TimeHelper;
import cn.feng.ikun.value.BoolValue;
import cn.feng.ikun.value.FloatValue;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AutoFill extends Module {

    public static FloatValue delay = new FloatValue("Delay", 1000f, 50f, 100f);
    public static BoolValue Soup = new BoolValue("Soup", false);
    public static BoolValue Pot = new BoolValue("Pot", false);
    public static BoolValue onInv = new BoolValue("Only Inv", false);
    TimeHelper time = new TimeHelper();
    Item value;


    public AutoFill() {
        super("AutoFill", "Auto fill soups and pots.", Type.PLAYER);
    }

    public static boolean isHotbarFull() {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemstack = mc.thePlayer.inventory.getStackInSlot(i);

            if (itemstack == null) {
                return false;
            }
        }

        return true;
    }

    public static void fill(Item value) {
        for (int i = 9; i < 37; ++i) {
            ItemStack itemstack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (itemstack != null && itemstack.getItem() == value) {
                mc.playerController.windowClick(0, i, 0, 1, mc.thePlayer);
                break;
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Soup.getValue()) {
            value = Items.mushroom_stew;
        } else if (Pot.getValue()) {
            value = Item.getItemById(373);
        }

        autoFill();
    }

    private void autoFill() {
        if (!onInv.getValue() || mc.currentScreen instanceof GuiInventory) {
            if (!isHotbarFull() && time.timePassed((long) delay.getValue())) {
                fill(value);
                time.reset();
            }
        }
    }
}
