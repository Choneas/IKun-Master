package cn.feng.ikun.module.modules.combat;

import cn.feng.ikun.Client;
import cn.feng.ikun.event.EventTick;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.utils.EntityUtils;
import cn.feng.ikun.value.BoolValue;
import cn.feng.ikun.value.FloatValue;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;

import java.util.List;

import static java.lang.Math.sqrt;
import static net.minecraft.realms.RealmsMth.wrapDegrees;

public class AimBot extends Module {
    public static final BoolValue magnetism = new BoolValue("Magnetism", true);
    private static final FloatValue reach = new FloatValue("Reach", 8.0f, 2.0f, 3.3f);
    private static final FloatValue yaw = new FloatValue("YawSpeed", 50.0f, 1.0f, 15.0f);
    private static final FloatValue pitch = new FloatValue("PitchSpeed", 50.0f, 1.0f, 15.0f);
    public EntityLivingBase target;

    public AimBot() {
        super("Aimbot", "Automatically aims at other players.", Type.COMBAT);
    }

    public static void assistFaceEntity(Entity entity, float yaw, float pitch) {
        double yDifference;
        if (entity == null) {
            return;
        }
        double diffX = entity.posX - mc.thePlayer.posX;
        double diffZ = entity.posZ - mc.thePlayer.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            yDifference = entityLivingBase.posY + (double) entityLivingBase.getEyeHeight() - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
        } else {
            yDifference = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
        }
        double dist = sqrt(diffX * diffX + diffZ * diffZ);
        float rotationYaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float rotationPitch = (float) (-(Math.atan2(yDifference, dist) * 180.0 / Math.PI));
        if (yaw > 0.0f) {
            mc.thePlayer.rotationYaw = updateRotation(mc.thePlayer.rotationYaw, rotationYaw, yaw / 4.0f);
        }
        if (pitch > 0.0f) {
            mc.thePlayer.rotationPitch = updateRotation(mc.thePlayer.rotationPitch, rotationPitch, pitch / 4.0f);
        }
    }

    public static float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
        float var4 = wrapDegrees(p_70663_2_ - p_70663_1_);
        if (var4 > p_70663_3_) {
            var4 = p_70663_3_;
        }
        if (var4 < -p_70663_3_) {
            var4 = -p_70663_3_;
        }
        return p_70663_1_ + var4;
    }

    public static List<Entity> getEntityList() {
        return mc.theWorld.getLoadedEntityList();
    }

    @Override
    public void onDisable() {
        target = null;
    }

    @EventTarget
    public void onTick(EventTick e) {
        if (magnetism.getValue()) {
            if (mc.gameSettings.keyBindPickBlock.isKeyDown()) {
                updateTarget();
                assistFaceEntity(target, yaw.getValue(), pitch.getValue());
                target = null;
            }
        } else {
            updateTarget();
            assistFaceEntity(target, yaw.getValue(), pitch.getValue());
            target = null;
        }
    }

    void updateTarget() {
        try {
            for (Entity object : getEntityList()) {
                EntityLivingBase entity;
                if (!(object instanceof EntityLivingBase) || !this.check(entity = (EntityLivingBase) object) || !EntityUtils.isSelected(object, true)) continue;
                this.target = entity;
            }
        } catch (NullPointerException Ex) {
//            Helper.sendMessage("傻逼吧你");
        }
    }

    public boolean check(EntityLivingBase entity) {
        if (entity instanceof EntityArmorStand) {
            return false;
        }
        if (entity == mc.thePlayer) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (((AntiBots) Client.instance.moduleManager.getModule(AntiBots.class)).isNPC(entity)) {
            return false;
        }
        if (entity.getDistanceToEntity(mc.thePlayer) > reach.getValue()) {
            return false;
        }
        return mc.thePlayer.canEntityBeSeen(entity);
    }
}
