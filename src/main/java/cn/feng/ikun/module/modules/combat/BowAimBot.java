package cn.feng.ikun.module.modules.combat;

import cn.feng.ikun.Client;
import cn.feng.ikun.event.EventTick;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

public class BowAimBot extends Module {
    public EntityLivingBase target;
    public float rangeAimVelocity = 0.0f;

    public BowAimBot() {
        super("BowAimBot", "AutoAim Target when you using bow.", Type.COMBAT);
    }

    @EventTarget
    public void onTick(EventTick event) {
        if (target == null)
            return;
        if (((AntiBots) Client.instance.moduleManager.getModule(AntiBots.class)).isNPC(target))
            return;
        if (Client.instance.friendManager.isFriend(target.getName()))
            return;
        if (target.getHealth() == 0)
            return;

        ItemStack itemStack = mc.thePlayer.inventory.getCurrentItem();
        if (itemStack == null || !(itemStack.getItem() instanceof ItemBow)) {
            return;
        }
        if (!mc.gameSettings.keyBindUseItem.isKeyDown()) {
            return;
        }
        this.target = this.getClosestEntity();
        if (this.target == null) {
            return;
        }
        int rangeCharge = mc.thePlayer.getItemInUseCount();
        this.rangeAimVelocity = rangeCharge / 20;
        this.rangeAimVelocity = (this.rangeAimVelocity * this.rangeAimVelocity + this.rangeAimVelocity * 2.0f) / 3.0f;
        this.rangeAimVelocity = 1.0f;
        if (this.rangeAimVelocity > 1.0f) {
            this.rangeAimVelocity = 1.0f;
        }
        double posX = this.target.posX - mc.thePlayer.posX;
        double posY = this.target.posY + (double) this.target.getEyeHeight() - 0.15 - mc.thePlayer.posY - (double) mc.thePlayer.getEyeHeight();
        double posZ = this.target.posZ - mc.thePlayer.posZ;
        double y2 = Math.sqrt(posX * posX + posZ * posZ);
        float g = 0.006f;
        float tmp = (float) ((double) (this.rangeAimVelocity * this.rangeAimVelocity * this.rangeAimVelocity * this.rangeAimVelocity) - (double) g * ((double) g * (y2 * y2) + 2.0 * posY * (double) (this.rangeAimVelocity * this.rangeAimVelocity)));
        float pitch = (float) (-Math.toDegrees(Math.atan(((double) (this.rangeAimVelocity * this.rangeAimVelocity) - Math.sqrt(tmp)) / ((double) g * y2))));
        AimBot.assistFaceEntity(this.target, 22.0F, 0.0f);
        mc.thePlayer.rotationPitch = pitch;
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
        return mc.thePlayer.canEntityBeSeen(entity);
    }

    EntityLivingBase getClosestEntity() {
        EntityLivingBase closestEntity = null;
        for (Entity o : AimBot.getEntityList()) {
            EntityLivingBase entity;
            if (!(o instanceof EntityLivingBase) || o instanceof EntityArmorStand || !this.check(entity = (EntityLivingBase) o) || closestEntity != null && !(mc.thePlayer.getDistanceToEntity(entity) < mc.thePlayer.getDistanceToEntity(closestEntity)))
                continue;
            closestEntity = entity;
        }
        return closestEntity;
    }
}
