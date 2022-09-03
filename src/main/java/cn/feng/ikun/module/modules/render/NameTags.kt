package cn.feng.ikun.module.modules.render

import cn.feng.ikun.Client
import cn.feng.ikun.event.Event3D
import cn.feng.ikun.module.Module
import cn.feng.ikun.module.Type
import cn.feng.ikun.module.modules.combat.AntiBots
import cn.feng.ikun.utils.EntityUtils
import cn.feng.ikun.utils.render.GLUtil
import cn.feng.ikun.value.BoolValue
import cn.feng.ikun.value.FloatValue
import com.darkmagician6.eventapi.EventTarget
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager.*
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.*
import org.lwjgl.opengl.GL11.*

class NameTags : Module("NameTags", "Render name tags", Type.RENDER) {
    val armorValue = BoolValue("Armor", true)
    val scaleValue = FloatValue("Scale", 2.0F, 0.5F, 1.0F)

    @EventTarget
    fun on3D(e: Event3D) {
        for (o in mc.theWorld.loadedEntityList) {
            if (o is EntityLivingBase) {
                if (o.isEntityAlive && (o != mc.thePlayer)) {
                    if (!EntityUtils.isSelected(o, false))
                        continue
                    val renderManager = mc.renderManager
                    val pX: Double =
                        o.lastTickPosX + (o.posX - o.lastTickPosX) * mc.timer.renderPartialTicks - renderManager.renderPosX
                    val pY: Double =
                        o.lastTickPosY + (o.posY - o.lastTickPosY) * mc.timer.renderPartialTicks - renderManager.renderPosY
                    val pZ: Double =
                        o.lastTickPosZ + (o.posZ - o.lastTickPosZ) * mc.timer.renderPartialTicks - renderManager.renderPosZ
                    renderNameTag(o, pX, pY, pZ)
                }
            }
        }
    }

    private fun renderNameTag(entity: EntityLivingBase, pX: Double, pY: Double, pZ: Double) {
        var pY = pY
        val fr: FontRenderer = mc.fontRendererObj
        var size = mc.thePlayer.getDistanceToEntity(entity) / 2.5f
        if (size < 4.0f) {
            size = 4.0f
        }
        pY += if (entity.isSneaking) 0.45 else 0.6
        var scale = (size * scaleValue.value)
        scale /= 200f

        val bot = if ((Client.instance.moduleManager.getModule(AntiBots::class.java) as AntiBots).isNPC(entity)) {
            "\u00a77[Bot] "
        } else {
            ""
        }

        val heal: Int = entity.health.toInt()
        val color1: String = if (heal > 20.0) {
            "\u00a79"
        } else if (heal >= 11.0) {
            "\u00a7a"
        } else if (heal >= 4.0) {
            "\u00a7e"
        } else {
            "\u00a74"
        }
        val hp = " [$color1$heal §c❤§f]"
        glPushMatrix()
        glTranslatef(pX.toFloat(), pY.toFloat() + 1.4f, pZ.toFloat())
        glNormal3f(0.0f, 1.0f, 0.0f)
        val renderManager = mc.renderManager
        glRotatef(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        glRotatef(renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        glScalef(-scale, -scale, scale)
        GLUtil.setGLCap(2896, false)
        GLUtil.setGLCap(2929, false)
        GLUtil.setGLCap(3042, true)
        glBlendFunc(770, 771)
        val lol = entity.displayName.formattedText
        val username = bot + lol + hp
        val strWidth = fr.getStringWidth(username) / 2
        Gui.drawRect((-strWidth - 1), -14, (strWidth + 1), -4, Integer.MIN_VALUE)

        fr.drawStringWithShadow(username, (-strWidth).toFloat(), (fr.FONT_HEIGHT - 22).toFloat(), 16777215)
        glColor3f(1f, 1f, 1f)
        glScaled(0.5, 0.5, 0.5)
        glScaled(1.0, 1.0, 1.0)
        glPushMatrix()
        glScaled(1.5, 1.5, 1.5)
        if (this.armorValue.value) {
            var xOffset = 0
            for (armourStack in (entity as EntityPlayer).inventory.armorInventory) {
                if (armourStack != null) xOffset -= 10
            }
            val renderStack: Any
            if (entity.heldItem != null) {
                xOffset -= 8
                renderStack = entity.heldItem.copy()
                if (renderStack.hasEffect()
                    && (renderStack.item is ItemTool
                            || renderStack.item is ItemArmor)
                ) renderStack.stackSize = 1
                renderItemStack(renderStack, xOffset, -34)
                xOffset += 20
            }
            for (armourStack in entity.inventory.armorInventory) if (armourStack != null) {
                val renderStack1 = armourStack.copy()
                if (renderStack1.hasEffect() && (renderStack1.item is ItemTool
                            || renderStack1.item is ItemArmor)
                ) renderStack1.stackSize = 1
                renderItemStack(renderStack1, xOffset, -33)
                xOffset += 20
            }
        }
        glPopMatrix()
        GLUtil.revertAllCaps()
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        glPopMatrix()
    }

    fun renderItemStack(stack: ItemStack, x: Int, y: Int) {
        glPushMatrix()
        glDepthMask(true)
        clear(256)
        RenderHelper.enableStandardItemLighting()
        mc.renderItem.zLevel = -150.0f
        whatTheFuckOpenGLThisFixesItemGlint()
        mc.renderItem.renderItemAndEffectIntoGUI(stack, x, y)
        mc.renderItem.renderItemOverlays(mc.fontRendererObj, stack, x, y)
        mc.renderItem.zLevel = 0.0f
        RenderHelper.disableStandardItemLighting()
        renderEnchantText(stack, x, y)
        disableCull()
        enableAlpha()
        disableBlend()
        disableLighting()
        scale(0.5, 0.5, 0.5)
        disableDepth()
        enableDepth()
        scale(2.0f, 2.0f, 2.0f)
        glPopMatrix()
    }

    private fun whatTheFuckOpenGLThisFixesItemGlint() {
        disableLighting()
        disableDepth()
        disableBlend()
        enableLighting()
        enableDepth()
        disableLighting()
        disableDepth()
        disableTexture2D()
        disableAlpha()
        disableBlend()
        enableBlend()
        enableAlpha()
        enableTexture2D()
        enableLighting()
        enableDepth()
    }

    private fun renderEnchantText(stack: ItemStack, x: Int, y: Int) {
        var unbreakingLevel2: Int
        var enchantmentY = y - 8
        if (stack.enchantmentTagList != null && stack.enchantmentTagList.tagCount() >= 6) {
            mc.fontRendererObj.drawStringWithShadow("god", x * 2.toFloat(), enchantmentY.toFloat(), 16711680)
            return
        }
        if (stack.item is ItemArmor) {
            val protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)
            val projectileProtectionLevel =
                EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack)
            val blastProtectionLevel =
                EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack)
            val fireProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack)
            val thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack)
            val unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack)
            if (protectionLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(
                    "pr$protectionLevel",
                    x * 1.toFloat(),
                    enchantmentY.toFloat(),
                    52479
                )
                enchantmentY += 8
            }
            if (projectileProtectionLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(
                    "pp$projectileProtectionLevel",
                    x * 1.toFloat(),
                    enchantmentY.toFloat(),
                    52479
                )
                enchantmentY += 8
            }
            if (blastProtectionLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(
                    "bp$blastProtectionLevel",
                    x * 1.toFloat(),
                    enchantmentY.toFloat(),
                    52479
                )
                enchantmentY += 8
            }
            if (fireProtectionLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(
                    "fp$fireProtectionLevel",
                    x * 1.toFloat(),
                    enchantmentY.toFloat(),
                    52479
                )
                enchantmentY += 8
            }
            if (thornsLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("t$thornsLevel", x * 1.toFloat(), enchantmentY.toFloat(), 52479)
                enchantmentY += 8
            }
            if (unbreakingLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(
                    "u$unbreakingLevel",
                    x * 1.toFloat(),
                    enchantmentY.toFloat(),
                    52479
                )
                enchantmentY += 8
            }
        }
        if (stack.item is ItemBow) {
            val powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack)
            val punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack)
            val flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack)
            unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack)
            if (powerLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("po$powerLevel", x * 1.toFloat(), enchantmentY.toFloat(), 65535)
                enchantmentY += 8
            }
            if (punchLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("pu$punchLevel", x * 1.toFloat(), enchantmentY.toFloat(), 65535)
                enchantmentY += 8
            }
            if (flameLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("f$flameLevel", x * 1.toFloat(), enchantmentY.toFloat(), 65535)
                enchantmentY += 8
            }
            if (unbreakingLevel2 > 0) {
                mc.fontRendererObj.drawStringWithShadow(
                    "u$unbreakingLevel2",
                    x * 1.toFloat(),
                    enchantmentY.toFloat(),
                    65535
                )
                enchantmentY += 8
            }
        }
        if (stack.item is ItemSword) {
            val sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack)
            val knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack)
            val fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack)
            unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack)
            if (sharpnessLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(
                    "sh$sharpnessLevel",
                    x * 1.toFloat(),
                    enchantmentY.toFloat(),
                    65535
                )
                enchantmentY += 8
            }
            if (knockbackLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(
                    "kn$knockbackLevel",
                    x * 1.toFloat(),
                    enchantmentY.toFloat(),
                    65535
                )
                enchantmentY += 8
            }
            if (fireAspectLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(
                    "f$fireAspectLevel",
                    x * 1.toFloat(),
                    enchantmentY.toFloat(),
                    65535
                )
                enchantmentY += 8
            }
            if (unbreakingLevel2 > 0) {
                mc.fontRendererObj.drawStringWithShadow(
                    "ub$unbreakingLevel2",
                    x * 1.toFloat(),
                    enchantmentY.toFloat(),
                    65535
                )
            }
        }
        if (stack.item is ItemTool) {
            val unbreakingLevel22 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack)
            val efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack)
            val fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack)
            val silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack)
            if (efficiencyLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(
                    "eff$efficiencyLevel",
                    x * 1.toFloat(),
                    enchantmentY.toFloat(),
                    65535
                )
                enchantmentY += 8
            }
            if (fortuneLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(
                    "fo$fortuneLevel",
                    x * 1.toFloat(),
                    enchantmentY.toFloat(),
                    65535
                )
                enchantmentY += 8
            }
            if (silkTouch > 0) {
                mc.fontRendererObj.drawStringWithShadow("st$silkTouch", x * 1.toFloat(), enchantmentY.toFloat(), 65535)
                enchantmentY += 8
            }
            if (unbreakingLevel22 > 0) {
                mc.fontRendererObj.drawStringWithShadow(
                    "ub$unbreakingLevel22",
                    x * 1.toFloat(),
                    enchantmentY.toFloat(),
                    65535
                )
            }
        }
        if (stack.item === Items.golden_apple && stack.hasEffect()) {
            mc.fontRendererObj.drawStringWithShadow("god", x * 2.toFloat(), enchantmentY.toFloat(), 52479)
        }
    }
}