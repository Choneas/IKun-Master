package cn.feng.ikun.module.modules.render

import cn.feng.ikun.Client
import cn.feng.ikun.event.Event3D
import cn.feng.ikun.module.Module
import cn.feng.ikun.module.Type
import cn.feng.ikun.utils.EntityUtils
import cn.feng.ikun.utils.RotationUtils
import cn.feng.ikun.utils.render.RenderUtils
import cn.feng.ikun.value.BoolValue
import cn.feng.ikun.value.FloatValue
import cn.feng.ikun.value.IntValue
import cn.feng.ikun.value.ListValue
import com.darkmagician6.eventapi.EventTarget
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.util.Vec3
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.*

class Tracers : Module("Tracers", "Draw some lines to entities.", Type.RENDER) {

    private val colorMode = ListValue("Color", arrayOf("Custom", "DistanceColor", "Rainbow"), "Custom")

    private val thicknessValue = FloatValue("Thickness", 5F, 1F, 2F)

    private val colorRedValue = IntValue("R", 255, 0, 255)
    private val colorGreenValue = IntValue("G", 255, 0, 255)
    private val colorBlueValue = IntValue("B", 255, 0, 255)

    private val directLineValue = BoolValue("Directline", false)
    private val fovModeValue = ListValue("FOV-Mode", arrayOf("All", "Back", "Front"), "All")
    private val fovValue = FloatValue("FOV", 180F, 0F, 180F)

    @EventTarget
    fun onRender3D(event: Event3D) {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glLineWidth(thicknessValue.value)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glDepthMask(false)

        GL11.glBegin(GL11.GL_LINES)

        for (entity in if (fovModeValue.isCurrentMode("all")) mc.theWorld.loadedEntityList else mc.theWorld.loadedEntityList.filter {
            if (fovModeValue.isCurrentMode(
                    "back"
                )
            ) RotationUtils.getRotationBackDifference(it) <= fovValue.value else RotationUtils.getRotationDifference(it) <= fovValue.value
        }) {
            if (entity != null && entity != mc.thePlayer && EntityUtils.isSelected(entity, false)) {
                var dist = (mc.thePlayer.getDistanceToEntity(entity) * 2).toInt()

                if (dist > 255) dist = 255

                val colorMode = colorMode.value.lowercase(Locale.getDefault())
                val color = when {
                    Client.instance.friendManager.isFriend(entity.name) -> Color(0, 0, 255, 150)
                    colorMode == "custom" -> Color(
                        colorRedValue.value,
                        colorGreenValue.value,
                        colorBlueValue.value,
                        150
                    )
                    colorMode == "distancecolor" -> Color(255 - dist, dist, 0, 150)
                    else -> Color(255, 255, 255, 150)
                }

                drawTraces(entity, color, !directLineValue.value)
            }
        }

        GL11.glEnd()

        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glDepthMask(true)
        GL11.glDisable(GL11.GL_BLEND)
        GlStateManager.resetColor()
    }

    private fun drawTraces(entity: Entity, color: Color, drawHeight: Boolean) {
        val x = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks
                - mc.renderManager.renderPosX)
        val y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks
                - mc.renderManager.renderPosY)
        val z = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks
                - mc.renderManager.renderPosZ)

        val eyeVector = Vec3(0.0, 0.0, 1.0)
            .rotatePitch((-Math.toRadians(mc.thePlayer.rotationPitch.toDouble())).toFloat())
            .rotateYaw((-Math.toRadians(mc.thePlayer.rotationYaw.toDouble())).toFloat())

        RenderUtils.glColor(color)

        GL11.glVertex3d(eyeVector.xCoord, mc.thePlayer.eyeHeight.toDouble() + eyeVector.yCoord, eyeVector.zCoord)
        if (drawHeight) {
            GL11.glVertex3d(x, y, z)
            GL11.glVertex3d(x, y, z)
            GL11.glVertex3d(x, y + entity.height, z)
        } else {
            GL11.glVertex3d(x, y + entity.height / 2.0, z)
        }

    }
}
