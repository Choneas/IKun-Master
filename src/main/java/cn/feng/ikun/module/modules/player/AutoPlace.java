package cn.feng.ikun.module.modules.player;

import cn.feng.ikun.event.EventUpdate;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.ItemBlock;

public class AutoPlace extends Module {
    public AutoPlace() {
        super("Auto Place", "Auto place blocks.", Type.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (isHoldingBlock())
            mc.rightClickMouse();
    }

    private boolean isHoldingBlock() {
        return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().stackSize > 0 && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock;
    }

}
