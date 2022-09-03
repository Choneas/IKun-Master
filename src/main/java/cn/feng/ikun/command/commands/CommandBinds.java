package cn.feng.ikun.command.commands;

import cn.feng.ikun.Client;
import cn.feng.ikun.command.Command;
import cn.feng.ikun.module.Module;
import org.lwjgl.input.Keyboard;

public class CommandBinds extends Command {
    public CommandBinds() {
        super("binds");
    }
    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Binds: \n");
            for (Module m : Client.instance.moduleManager.modules) {
                if (m.getKey() != 0) {
                    sb.append("§b[" + m.getName() + "] §f- §e" + Keyboard.getKeyName(m.getKey()) + "\n");
                }
            }
            alert(sb.substring(0, sb.length() - 2));
        } else {
            alert(".binds");
        }
    }
}
