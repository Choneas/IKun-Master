package cn.feng.ikun.command.commands;

import cn.feng.ikun.Client;
import cn.feng.ikun.command.Command;
import cn.feng.ikun.utils.misc.LogUtils;

public class CommandHelp extends Command {
    public CommandHelp() {
        super("help");
    }
    @Override
    public void execute(String[] args) {
        for (Command c : Client.instance.commandManager.commands) {
            alert("." + c.getName());
        }
        alert("§e§lTip§r§e: §6You can join our §6community to find more §6help. §6https://ikunclient.github.io§r");
    }
        }