package cn.feng.ikun.module.modules.combat;

import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.value.FloatValue;

public class HitBox extends Module {
    public HitBox() {
        super("HitBox", "Edit hit box.", Type.COMBAT);
    }

    public static final FloatValue size = new FloatValue("Size", 1.0f, 0.1f, 0.1f);
}
