package cn.feng.ikun.command.commands;

import cn.feng.ikun.Client;
import cn.feng.ikun.command.Command;
import cn.feng.ikun.utils.misc.LogUtils;

public class CommandFriend extends Command {
    public CommandFriend() {
        super("friend");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                Client.instance.friendManager.addFriend(args[1]);
            } else if (args[0].equalsIgnoreCase("remove")) {
                Client.instance.friendManager.removeFriend(args[1]);
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            for (String s : Client.instance.friendManager.friends) {
                alert(s);
            }
        } else {
            alert(".friend <add/remove> <FriendName>");
            alert(".friend list");
        }
    }
}
