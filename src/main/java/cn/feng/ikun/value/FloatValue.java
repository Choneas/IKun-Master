package cn.feng.ikun.value;

import cn.feng.ikun.Client;

public class FloatValue extends Value {
    private float max, min, value;
    public FloatValue(String name, float max, float min, float value) {
        super(name);
        this.max = max;
        this.min = min;
        this.value = value;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
        if (!Client.instance.isLoading) {
            Client.instance.configManager.save();
        }
    }
}
