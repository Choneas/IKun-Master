package cn.feng.ikun.command;

import cn.feng.ikun.utils.misc.LogUtils;

public abstract class Command {
    private final String name;
    public Command(String name) {
        this.name = name;
    }

    public abstract void execute(String[] args);
    protected void alert(String message) {
        LogUtils.print("[Command] " + message);
    }
    public String getName() {
        return name;
    }
}
