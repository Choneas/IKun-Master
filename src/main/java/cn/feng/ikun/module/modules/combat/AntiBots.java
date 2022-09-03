package cn.feng.ikun.module.modules.combat;

import cn.feng.ikun.event.EventRespawn;
import cn.feng.ikun.event.EventTick;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.utils.EntityUtils;
import cn.feng.ikun.utils.ServerUtils;
import cn.feng.ikun.utils.WorldUtil;
import cn.feng.ikun.utils.misc.LogUtils;
import cn.feng.ikun.value.ListValue;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AntiBots extends Module {
    public static ListValue mode = new ListValue("Mode", new String[]{"Basic", "Advanced", "Hypixel", "BrokenID", "Invisible", "Enclose"}, "Basic");
    private final HashMap<String, Integer> TabTicks = new HashMap<>();
    private final HashMap<Integer, Integer> InvisTicks = new HashMap<>();
    private final List<Integer> Grounded = new ArrayList<>();

    private final List<Integer> blacklisted = new ArrayList<>();

    public AntiBots() {
        super("AntiBot", "No anticheat bots.", Type.COMBAT);
    }

    public static boolean isInTabList(EntityLivingBase entity) {
        for (NetworkPlayerInfo item : mc.getNetHandler().getPlayerInfoMap()) {

            if (item != null && item.getGameProfile() != null && item.getGameProfile().getName().contains(entity.getName())) {
                return true;
            }
        }

        return false;
    }

    public static List<EntityPlayer> getTabPlayerList() {
        NetHandlerPlayClient nhpc = mc.thePlayer.sendQueue;
        List<EntityPlayer> list = new ArrayList<>();
        List<NetworkPlayerInfo> players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(nhpc.getPlayerInfoMap());
        for (final NetworkPlayerInfo o : players) {
            if (o == null) {
                continue;
            }
            list.add(mc.theWorld.getPlayerEntityByName(o.getGameProfile().getName()));
        }
        return list;
    }

    public static boolean isHypixelNPC(Entity entity) {
        if (!mode.getValue().equals("Hypixel")) return false;

        String formattedName = entity.getDisplayName().getFormattedText();

        if (entity instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) entity);
            return player.tabTicks < 150;
        }

        if (!formattedName.startsWith("\247") && formattedName.endsWith("\247r")) {
            return true;
        }

        return formattedName.contains("[NPC]");
    }

    private boolean hasBadlionBots(final EntityPlayer parent) {
        if (parent.isInvisible())
            return false;

        for (EntityPlayer player : WorldUtil.getLivingPlayers()) {
            if (player != parent && player.isInvisible() && parent.getDistanceToEntity(player) < 3.0)
                return true;
        }

        return false;
    }

    @EventTarget
    public void onTick1(EventTick event) {
        for (EntityPlayer player : WorldUtil.getLivingPlayers()) {
            final String name = EnumChatFormatting.getTextWithoutFormattingCodes(player.getName());

            if (!TabTicks.containsKey(name)) {
                TabTicks.put(name, 0);
            }

            if (isInTabList(player)) {
                int before = TabTicks.get(name);
                TabTicks.remove(name);
                TabTicks.put(name, before + 1);
            }
        }
    }

    @EventTarget
    public void onRespawn(EventRespawn event) {
        this.TabTicks.clear();
        this.Grounded.clear();
        this.InvisTicks.clear();

        this.blacklisted.clear();
    }

    public boolean isNPC(EntityLivingBase entity) {
        if (!this.isToggle()) return false;

        if (entity == null) {
            return true;
        }

        if (entity == mc.thePlayer) {
            return true;
        }

        if (!(entity instanceof EntityPlayer)) {
            return false; // ALLOW ALL MOBS
        }

        if (ServerUtils.isOnHypixel() && entity.ticksExisted <= 10 * 20)
            return false;

        if (entity.isPlayerSleeping()) {
            return true;
        }

        if (mode.getValue().equals("BrokenID") && entity.getEntityId() > 1000000) {
            return true;
        }

        if ((mode.getValue().equals("Invisible") || mode.getValue().equals("Basic")) && !isInTabList(entity)) {
            return true;
        }

        if ((mode.getValue().equals("Basic") || mode.getValue().equals("Advanced"))
                && entity.ticksExisted <= 80) {
            return true;
        }

        if (mode.getValue().equals("Advanced") && !this.Grounded.contains(entity.getEntityId())) {
            return true;
        }

        if (mode.getValue().equals("Enclose") && hasBadlionBots((EntityPlayer) entity)) {
            return true;
        }

        return mode.getValue().equals("Invisible") && this.blacklisted.contains(entity.getEntityId());
    }

    @EventTarget
    public void onTick(EventTick event) {
        if (mode.getValue().equals("Invisible")) {
            for (EntityPlayer player : WorldUtil.getLivingPlayers()) {
                if (this.InvisTicks.containsKey(player.getEntityId()) && this.InvisTicks.get(player.getEntityId()) > 40
                        && EntityUtils.hasFakeInvisible(player)) {
                    this.blacklisted.add(player.getEntityId());

                    LogUtils.print("removed Invisible bot (name:" + player.getDisplayName().getFormattedText() + ")");
                }
            }
        }

        if (mode.getValue().equals("Hypixel")) {
            for (EntityPlayer entity : mc.theWorld.playerEntities) {
                if (entity != mc.thePlayer && entity != null) {
                    if (getTabPlayerList().contains(entity)) {
                        if (entity.tabTicks++ == 150) {
                            LogUtils.print(entity.getName() + " is not hypixel bot.");
                        }
                    } else if (!entity.getDisplayName().getFormattedText().toLowerCase().contains("[npc") && entity.getDisplayName().getFormattedText().startsWith("\u00a7") && entity.isEntityAlive()) {
                        if (!isHypixelNPC(entity) && entity.isInvisible()) {
                            LogUtils.print(entity.getName());
                            mc.theWorld.removeEntity(entity);
                        }
                    }
                }
            }
        }

        // grounded
        this.Grounded.addAll(WorldUtil.getLivingPlayers().stream()
                .filter(entityPlayer -> entityPlayer.onGround && !this.Grounded.contains(entityPlayer.getEntityId()))
                .map(EntityPlayer::getEntityId).collect(Collectors.toList()));

        // custom ticks
        for (EntityPlayer player : WorldUtil.getLivingPlayers()) {
            if (!this.InvisTicks.containsKey(player.getEntityId()))
                this.InvisTicks.put(player.getEntityId(), 0);

            if (player.isInvisible() && EntityUtils.hasFakeInvisible(player)) {
                this.InvisTicks.put(player.getEntityId(), this.InvisTicks.get(player.getEntityId()) + 1);
            } else {
                this.InvisTicks.put(player.getEntityId(), 0);
            }
        }
    }
}