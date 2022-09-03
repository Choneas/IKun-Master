package cn.feng.ikun.utils;

import cn.feng.ikun.utils.misc.MC;
import net.minecraft.client.multiplayer.ServerData;

public class ServerUtils extends MC {
    public static boolean isOnHypixel() {
        ServerData data = mc.getCurrentServerData();
        String motd = data.serverMOTD;
        return motd.contains("Hypixel");
    }
}
