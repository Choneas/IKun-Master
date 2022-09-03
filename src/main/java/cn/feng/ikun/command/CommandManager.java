package cn.feng.ikun.command;

import cn.feng.ikun.command.commands.*;
import cn.feng.ikun.utils.misc.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    public CommandManager() {
        init();
    }
    public final List<Command> commands = new ArrayList<>();
    private void init() {
        commands.add(new CommandHelp());
        commands.add(new CommandTitle());
        commands.add(new CommandBind());
        commands.add(new CommandToggle());
        commands.add(new CommandBinds());
        commands.add(new CommandFriend());
    }
    public void executeCommands(String arg) {
        String str = arg.substring(1);
        LogUtils.info(str);
        String command = str.split(" ")[0];
        for (Command c : commands) {
            if (c.getName().equalsIgnoreCase(command)) {
                if (str.equalsIgnoreCase(command)) {
                    c.execute(new String[]{});
                } else {
                    c.execute(str.substring(command.length() + 1).split(" "));
                }
                return;
            }
        }
        LogUtils.print("Command " + command + " not found. Use .help to view helps.");
    }
}
