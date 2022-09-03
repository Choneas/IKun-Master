package cn.feng.ikun.module.modules.render;

import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.value.BoolValue;
import cn.feng.ikun.value.IntValue;

public class HUD extends Module {
    public HUD() {
        super("HUD", "Some visual settings", Type.RENDER, true);
    }
    public static final BoolValue useHotbar = new BoolValue("UseHotbar", true);
    public static final BoolValue useArray = new BoolValue("UseArray", true);
    public static final BoolValue useNotify = new BoolValue("UseNotify", true);
    public static final IntValue hotbarAlpha = new IntValue("HotbarAlpha", 255, 0, 100);
    public static final IntValue infoAlpha = new IntValue("InfoAlpha", 255, 0, 100);
    public static final IntValue musicPosX = new IntValue("MusicPosX", sr.getScaledWidth(), 0, 100);
    public static final IntValue musicPosY = new IntValue("MusicPosY", sr.getScaledWidth(), 0, 100);
    public static final IntValue musicPosYlyr = new IntValue("MusicPosYlyr", sr.getScaledWidth(), 0, 100);
}
