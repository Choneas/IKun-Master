package cn.feng.ikun.module.modules.combat;

import cn.feng.ikun.event.EventUpdate;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.utils.time.TimeHelper;
import cn.feng.ikun.value.IntValue;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class AutoSword extends Module {
    public static TimeHelper publicItemTimer = new TimeHelper();
    private static final IntValue slot = new IntValue("Sort", 9, 1, 1);
    TimeHelper time = new TimeHelper();
    public AutoSword() {
        super("AutoSword", "Auto select the best weapon for you.", Type.COMBAT);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (!publicItemTimer.timePassed(300)) return;
        if (!time.timePassed(1000L) || (mc.currentScreen != null && !(mc.currentScreen instanceof GuiInventory)))
            return;

        int best = -1;
        float swordDamage = 0;
        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemSword) {
                    float swordD = getSharpnessLevel(is);
                    if (swordD > swordDamage) {
                        swordDamage = swordD;
                        best = i;
                    }
                }
            }
        }
        final ItemStack current = mc.thePlayer.inventoryContainer.getSlot(slot.getValue() + 35).getStack();
        if (best != -1 && (current == null || !(current.getItem() instanceof ItemSword) || swordDamage > getSharpnessLevel(current))) {

            /*
             * try { if
             * (!Hanabi.AES_UTILS.decrypt(Hanabi.HWID_VERIFY).contains(Wrapper.getHWID())) {
             * FMLCommonHandler.instance().exitJava(0, true); Client.sleep = true; } } catch
             * (Exception e) { FMLCommonHandler.instance().exitJava(0, true); Client.sleep =
             * true; }
             *
             */
            publicItemTimer.reset();
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, best, slot.getValue() - 1, 2, mc.thePlayer);
            time.reset();
        }
    }

    public boolean isBestWeapon(ItemStack stack) {
        float damage = getDamage(stack);
        for (int i = 9; i < 36; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getDamage(is) > damage && (is.getItem() instanceof ItemSword))
                    return false;
            }
        }
        return stack.getItem() instanceof ItemSword;

    }

    private float getDamage(ItemStack stack) {
        float damage = 0;
        Item item = stack.getItem();
        if (item instanceof ItemSword) {
            ItemSword sword = (ItemSword) item;
            damage += sword.getDamageVsEntity();
        }
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f +
                EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
        return damage;
    }

    public void getBestWeapon(int slot) {
        for (int i = 9; i < 36; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestWeapon(is) && getDamage(is) > 0 && (is.getItem() instanceof ItemSword)) {
                    swap(i, slot - 36);
                    break;
                }
            }
        }
    }

    protected void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
    }

    private float getSharpnessLevel(ItemStack stack) {
        float damage = ((ItemSword) stack.getItem()).getDamageVsEntity();
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
        return damage;
    }
}
