package cn.feng.ikun.command.commands;

import cn.feng.ikun.command.Command;
import org.lwjgl.opengl.Display;

public class CommandTitle extends Command {
    public CommandTitle() {
        super("title");
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 1) {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (String s : args) {
                sb.append(s);
                if (i != args.length - 1) {
                    sb.append(" ");
                }
                i++;
            }
            Display.setTitle(sb.toString());
        } else {
            alert(".title <NewTitle...>");
        }
    }
}
