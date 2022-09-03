package cn.feng.ikun.module.modules.render;

import cn.feng.ikun.event.Event2D;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FullBright extends Module {
    public FullBright() {
        super("FullBright", "Edit gamma.", Type.RENDER);
    }

    @EventTarget
    public void on2D(Event2D e) {
        mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 4000, 1));
    }
}
