package cn.feng.ikun.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class MoveUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float getSpeed() {
        return (float) getSpeed(mc.thePlayer.motionX, mc.thePlayer.motionZ);
    }

    public static double getSpeed(double motionX, double motionZ) {
        return Math.sqrt(motionX * motionX + motionZ * motionZ);
    }

    public static void strafe() {
        strafe(getSpeed());
    }

    public static void strafe(final float speed) {
        if(!isMoving())
            return;

        final double yaw = getDirection();
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }

    public static double getDirection() {
        return getDirectionRotation(mc.thePlayer.rotationYaw, mc.thePlayer.moveStrafing, mc.thePlayer.moveForward);
    }
    public static double getDirectionRotation(float yaw, float pStrafe, float pForward) {
        float rotationYaw = yaw;

        if(pForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if(pForward < 0F)
            forward = -0.5F;
        else if(pForward > 0F)
            forward = 0.5F;

        if(pStrafe > 0F)
            rotationYaw -= 90F * forward;

        if(pStrafe < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public static float getRawDirectionRotation(float yaw, float pStrafe, float pForward) {
        float rotationYaw = yaw;

        if(pForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if(pForward < 0F)
            forward = -0.5F;
        else if(pForward > 0F)
            forward = 0.5F;

        if(pStrafe > 0F)
            rotationYaw -= 90F * forward;

        if(pStrafe < 0F)
            rotationYaw += 90F * forward;

        return rotationYaw;
    }
    public static double getBaseMovementSpeed() {
        double base = 0.2873D;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            base += 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return base;
    }

    public static boolean isMoving() {
        return mc.thePlayer.movementInput.moveForward != 0.0f || mc.thePlayer.movementInput.moveStrafe != 0.0f;
    }

    public static int getSpeedEffect() {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }

    public static double getJumpEffect() {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            return mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }

    public static boolean isOnGround(double height) {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty();
    }

    public static boolean isPressingMoving() {
        return mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindJump.isKeyDown();
    }
}
