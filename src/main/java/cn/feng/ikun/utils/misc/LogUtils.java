package cn.feng.ikun.utils.misc;

import cn.feng.ikun.Client;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LogUtils extends MC {
    private static final Logger logger = LogManager.getLogger("IKUN");
    public static void info(String message) {
        logger.info(message);
    }
    public static void warn(String message) {
        logger.warn(message);
    }
    public static void print(String message) {
        mc.thePlayer.addChatMessage(new ChatComponentText("§b[" + Client.instance.name + "]§f " + message));
    }
}
