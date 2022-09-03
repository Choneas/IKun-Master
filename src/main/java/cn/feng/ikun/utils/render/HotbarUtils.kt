package cn.feng.ikun.utils.render

object HotbarUtils {
    private var easeAnimation: Animation? = null
    private var easingValue = 0
        get() {
            if (easeAnimation != null) {
                field = easeAnimation!!.value.toInt()
                if (easeAnimation!!.state == Animation.EnumAnimationState.STOPPED) {
                    easeAnimation = null
                }
            }
            return field
        }
        set(value) {
            if (easeAnimation == null || (easeAnimation != null && easeAnimation!!.to != value.toDouble())) {
                easeAnimation = Animation(
                    EaseUtils.EnumEasingType.CUBIC,
                    EaseUtils.EnumEasingOrder.FAST_AT_START,
                    field.toDouble(),
                    value.toDouble(),
                    10 * 30L
                ).start()
            }
        }

    @JvmStatic
    fun getHotbarEasePos(x: Int): Int {
        easingValue = x
        return easingValue
    }
}