package cn.feng.ikun.value;

import cn.feng.ikun.Client;

public class IntValue extends Value {
    private int max, min, value;
    public IntValue(String name, int max, int min, int value) {
        super(name);
        this.max = max;
        this.min = min;
        this.value = value;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        if (!Client.instance.isLoading) {
            Client.instance.configManager.save();
        }
    }
}
