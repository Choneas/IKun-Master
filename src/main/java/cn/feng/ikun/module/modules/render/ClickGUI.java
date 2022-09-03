package cn.feng.ikun.module.modules.render;

import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.ui.gui.clickgui.AzlipsClickGUI;
import cn.feng.ikun.value.IntValue;
import org.lwjgl.input.Keyboard;

public class ClickGUI extends Module {
    public static final IntValue colorRedValue = new IntValue("Red", 0, 255, 255);
    public static final IntValue colorGreenValue = new IntValue("Green", 0, 255, 255);
    public static final IntValue colorBlueValue = new IntValue("Blue", 0, 255, 255);
    public ClickGUI() {
        super("ClickGUI", "Module control.", Type.RENDER, Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new AzlipsClickGUI());
        onToggle();
    }
}
