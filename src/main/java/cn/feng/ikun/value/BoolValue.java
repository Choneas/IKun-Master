package cn.feng.ikun.value;

import cn.feng.ikun.Client;

public class BoolValue extends Value {
    private boolean value;
    private float anim = 0f;
    public BoolValue(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
        if (!Client.instance.isLoading) {
            Client.instance.configManager.save();
        }
    }

    public float getAnim() {
        return anim;
    }

    public void setAnim(float anim) {
        this.anim = anim;
    }
}
