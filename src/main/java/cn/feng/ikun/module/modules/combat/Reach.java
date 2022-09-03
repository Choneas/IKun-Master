package cn.feng.ikun.module.modules.combat;

import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.value.FloatValue;

public class Reach extends Module {
    public Reach() {
        super("Reach", "Make you can reach long than other player.", Type.COMBAT);
    }

    public static final FloatValue rangeValue = new FloatValue("Range", 5.0f, -5.0f, 3.0f);
}
