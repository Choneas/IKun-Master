package cn.feng.ikun.module.modules.combat;

import cn.feng.ikun.event.EventUpdate;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.utils.time.TimeHelper;
import cn.feng.ikun.value.BoolValue;
import cn.feng.ikun.value.FloatValue;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.ItemSword;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Mouse;

public class AutoClicker extends Module {
    public static final FloatValue minCPS = new FloatValue("Min CPS", 20.0f, 1.0f, 8.0f);
    public static final FloatValue maxCPS = new FloatValue("Max CPS", 20.0f, 1.0f, 12.0f);
    public static final BoolValue autoBlock = new BoolValue("Auto Block", false);
    private final TimeHelper timer = new TimeHelper();
    private final TimeHelper blocktimer = new TimeHelper();
    private int delay;
    public AutoClicker() {
        super("AutoClicker", "Auto click.", Type.COMBAT);
    }

    @Override
    public void onEnable() {
        setDelay();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (minCPS.getValue() > maxCPS.getValue()) {
            minCPS.setValue(maxCPS.getValue());
        }
        if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
            return;
        }
        if (mc.playerController.getCurBlockDamageMP() != 0F) {
            return;
        }
        if (timer.delay(delay) && mc.gameSettings.keyBindPickBlock.isKeyDown()) {
            // autoblock
            if (autoBlock.getValue() && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit.isEntityAlive()) {
                if (mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && blocktimer.delay(100)) {
                    mc.thePlayer.getCurrentEquippedItem().useItemRightClick(mc.theWorld, mc.thePlayer);
                    blocktimer.reset();
                }
            }
            mc.gameSettings.keyBindAttack.setPressed(false);
            mc.gameSettings.keyBindAttack.setPressed(true);
            mc.setLeftClickCounter(0);
            mc.clickMouse();
            setDelay();
            timer.reset();
        }
    }

    private void setDelay() {
        delay = (int) RandomUtils.nextFloat(1000.0F / maxCPS.getValue(), 1000.0F / minCPS.getValue());
    }
}
