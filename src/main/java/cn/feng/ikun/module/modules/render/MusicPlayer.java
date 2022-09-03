package cn.feng.ikun.module.modules.render;

import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.ui.gui.cloudmusic.ui.MusicPlayerUI;

public class MusicPlayer extends Module {
    public MusicPlayer() {
        super("MusicPlayer", "163 music.", Type.RENDER);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new MusicPlayerUI());
        onToggle();
    }
}
