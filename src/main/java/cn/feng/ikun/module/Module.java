package cn.feng.ikun.module;

import cn.feng.ikun.Client;
import cn.feng.ikun.ui.hud.Notification;
import cn.feng.ikun.utils.misc.ClassUtils;
import cn.feng.ikun.utils.misc.MC;
import cn.feng.ikun.value.Value;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class Module extends MC {
    private final String name, desc;
    private int key;
    private boolean toggle, visible;
    private Type type;

    public Module(String name, String desc, Type type) {
        this.name = name;
        this.desc = desc;
        this.key = Keyboard.KEY_NONE;
        this.toggle = false;
        this.visible = true;
        this.type = type;
    }

    public Module(String name, String desc, Type type, boolean defaultOn) {
        this.name = name;
        this.desc = desc;
        this.key = Keyboard.KEY_NONE;
        this.toggle = defaultOn;
        this.visible = true;
        this.type = type;
    }

    public Module(String name, String desc, Type type, int key) {
        this.name = name;
        this.desc = desc;
        this.key = key;
        this.toggle = false;
        this.visible = true;
        this.type = type;
    }

    public void onToggle() {
        this.toggle = !this.toggle;
        Client.instance.hudManager.showNotice(new Notification((toggle? "Enabled " : "Disabled ") + name, toggle? Notification.Type.SUCCESS : Notification.Type.ERROR));
        if (toggle) {
            Client.instance.eventManager.register(this);
            onEnable();
        } else {
            onDisable();
            Client.instance.eventManager.unregister(this);
        }
        Client.instance.configManager.save();
    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getSuffix() {
        return "";
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
        if (!Client.instance.isLoading) {
            Client.instance.configManager.save();
        }
    }

    public List<Value> getValues() {
        return ClassUtils.getValues(this.getClass(), this);
    }

    public boolean isToggle() {
        return toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (!Client.instance.isLoading) {
            Client.instance.configManager.save();
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
