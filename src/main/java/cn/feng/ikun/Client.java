package cn.feng.ikun;

import cn.feng.ikun.command.CommandManager;
import cn.feng.ikun.config.ConfigManager;
import cn.feng.ikun.friend.FriendManager;
import cn.feng.ikun.module.ModuleManager;
import cn.feng.ikun.ui.font.FontLoaders;
import cn.feng.ikun.ui.gui.altmanager.AltManager;
import cn.feng.ikun.ui.hud.HudManager;
import cn.feng.ikun.utils.misc.MC;
import com.darkmagician6.eventapi.EventManager;
import oh.yalan.NativeClass;
import org.lwjgl.opengl.Display;

@NativeClass
public enum Client {
    instance;

    public final String ver = "2.0";
    public String name = "IKun";
    public FontLoaders fontLoaders;
    public EventManager eventManager;
    public ModuleManager moduleManager;
    public HudManager hudManager;
    public ConfigManager configManager;
    public CommandManager commandManager;
    public AltManager altManager;
    public FriendManager friendManager;
    public boolean isLoading;
    public final String[] logs = new String[]{
            "Update Logs:",
            "Added some modules",
            "Fixed Fullbright"
    };
    public void run() {
        Display.setTitle(name + " Loading...");
        isLoading = true;
        fontLoaders = new FontLoaders();
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        hudManager = new HudManager();
        commandManager = new CommandManager();
        altManager = new AltManager();
        friendManager = new FriendManager();
        configManager = new ConfigManager();
        configManager.save();
        eventManager.register(new MC());
        isLoading = false;
        Display.setTitle(name + " " + ver);
    }

    public void stop() {
        configManager.save();
    }
}
