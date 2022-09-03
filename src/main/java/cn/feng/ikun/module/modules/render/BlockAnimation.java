package cn.feng.ikun.module.modules.render;

import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.value.BoolValue;
import cn.feng.ikun.value.FloatValue;
import cn.feng.ikun.value.ListValue;

public class BlockAnimation extends Module {

    public static final ListValue swordAnimation = new ListValue("Sword Animation",
            new String[]{"1.7", "Slide", "Sigma", "Push", "Push1", "Fixed", "Reverse", "Reverse1", "Vanilla", "Strange", "Spin", "Screw", "Poke", "Swong", "Exhi 1.7", "Exhi Swang", "Exhi Swong", "Exhi Swank", "E"}, "1.7");

    public static final FloatValue x = new FloatValue("Blocking X", 2.5f, -2.5f, 0f);
    public static final FloatValue y = new FloatValue("Blocking Y", 2.5f, -2.5f, 0f);
    public static final FloatValue z = new FloatValue("Blocking Z", 2.5f, -2.5f, 0f);

    public static final FloatValue xArm = new FloatValue("Arm X", 2.5f, -2.5f, 0f);
    public static final FloatValue yArm = new FloatValue("Arm Y", 2.5f, -2.5f, 0f);
    public static final FloatValue zArm = new FloatValue("Arm Z", 2.5f, -2.5f, 0f);

    public static final FloatValue swingSpeed = new FloatValue("Swing Slowdown", 3.0f, 0.5f, 1.0f);
    public static final BoolValue everything = new BoolValue("Everything Block", false);

    public static final BoolValue swingAnimation = new BoolValue("Swing Animation", true);

    public BlockAnimation() {
        super("BlockAnimation", "Block animations.", Type.RENDER);
    }
}
