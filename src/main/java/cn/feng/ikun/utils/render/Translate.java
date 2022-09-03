package cn.feng.ikun.utils.render;


public final class Translate {
    private float x;
    private float y;
    private float alpha;
    private boolean first = false;

    public Translate(float v, float v1) {
        this.x = x;
        this.y = y;
    }

    public final void SetAlpha(float alpha, double smoothing) {
        if (first) {
            this.alpha = AnimationUtil.animate(alpha, this.alpha, smoothing);
        } else {
            this.alpha = alpha;
            first = true;
        }
    }

    public final void interpolate(float targetX, float targetY, double smoothing) {
        if (first) {
            this.x = AnimationUtil.animate(targetX, this.x, smoothing);
            this.y = AnimationUtil.animate(targetY, this.y, smoothing);
        } else {
            this.x = targetX;
            this.y = targetY;
            first = true;
        }
    }

    public final void interpolate(float targetX, double smoothing) {
        if (first) {
            this.x = AnimationUtil.animate(targetX, this.x, smoothing);
        } else {
            this.x = targetX;
            first = true;
        }
    }

    public final void interpolate3(float targetX, float targetY, double smoothing) {
        this.x = AnimationUtil.animate(targetX, this.x, smoothing);
        this.y = AnimationUtil.animate(targetY, this.y, smoothing);
    }

    public final void interpolate2(float targetX, float targetY, double smoothing) {
        this.x = targetX;
        this.y = AnimationUtil.animate(targetY, this.y, smoothing);
    }

    public float getAlpha() {
        return this.alpha;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

