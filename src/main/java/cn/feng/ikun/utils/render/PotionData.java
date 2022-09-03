package cn.feng.ikun.utils.render;

import net.minecraft.potion.Potion;

public class PotionData {
    public final Potion potion;
    public final Translate translate;
    public final int level;
    public int maxTimer;
    public float animationX;

    public PotionData(final Potion potion, final Translate translate, final int level) {
        this.maxTimer = 0;
        this.animationX = 0.0f;
        this.potion = potion;
        this.translate = translate;
        this.level = level;
    }

    public float getAnimationX() {
        return this.animationX;
    }

    public Potion getPotion() {
        return this.potion;
    }

    public int getMaxTimer() {
        return this.maxTimer;
    }
}
