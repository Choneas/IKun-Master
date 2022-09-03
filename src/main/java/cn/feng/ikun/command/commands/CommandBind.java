package cn.feng.ikun.command.commands;

import cn.feng.ikun.Client;
import cn.feng.ikun.command.Command;
import cn.feng.ikun.module.Module;
import org.lwjgl.input.Keyboard;

public class CommandBind extends Command {
    public CommandBind() {
        super("bind");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            Module m = Client.instance.moduleManager.getModule(args[0]);
            if (m != null) {
                m.setKey(Keyboard.getKeyIndex(args[1].toUpperCase()));
                alert("Module " + m.getName() + "'s key bind was set to " + (m.getKey() == 0? "None" : args[1]) + ".");
            } else {
                alert("Module " + args[0] + " not found.");
            }
        } else {
            alert(".bind <Module> <Key>");
        }
    }
}
