package cn.feng.ikun.command.commands;

import cn.feng.ikun.Client;
import cn.feng.ikun.command.Command;
import cn.feng.ikun.module.Module;

public class CommandToggle extends Command {
    public CommandToggle() {
        super("t");
    }
    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            Module m = Client.instance.moduleManager.getModule(args[0]);
            if (m != null) {
                m.onToggle();
            } else {
                alert("Module " + args[0] + " not found.");
            }
        } else {
            alert(".t <Module>");
        }
    }
}
