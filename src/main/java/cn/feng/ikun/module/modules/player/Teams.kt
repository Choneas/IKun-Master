package cn.feng.ikun.module.modules.player

import cn.feng.ikun.module.Module
import cn.feng.ikun.module.Type
import cn.feng.ikun.value.BoolValue
import cn.feng.ikun.value.IntValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemArmor

class Teams : Module("Teams", "Teams", Type.PLAYER) {

    private val scoreboardValue = BoolValue("ScoreboardTeam", true)
    private val colorValue = BoolValue("Color", true)
    private val gommeSWValue = BoolValue("GommeSW", false)
    private val armorColorValue = BoolValue("ArmorColor", false)
    private val armorIndexValue = IntValue("ArmorIndex", 3, 0, 3)

    fun isInYourTeam(entity: EntityLivingBase): Boolean {
        mc.thePlayer ?: return false

        if (scoreboardValue.value && mc.thePlayer.team != null && entity.team != null &&
            mc.thePlayer.team.isSameTeam(entity.team)
        )
            return true

        if (armorColorValue.value) {
            val entityPlayer = entity as EntityPlayer
            if (mc.thePlayer.inventory.armorInventory[armorIndexValue.value] != null && entityPlayer.inventory.armorInventory[armorIndexValue.value] != null) {
                val myHead = mc.thePlayer.inventory.armorInventory[armorIndexValue.value]
                val myItemArmor = myHead!!.item!! as ItemArmor


                val entityHead = entityPlayer.inventory.armorInventory[armorIndexValue.value]
                var entityItemArmor = myHead.item!! as ItemArmor

                if (myItemArmor.getColor(myHead) == entityItemArmor.getColor(entityHead!!)) {
                    return true
                }
            }
        }

        if (gommeSWValue.value && mc.thePlayer.displayName != null && entity.displayName != null) {
            val targetName = entity.displayName.formattedText.replace("§r", "")
            val clientName = mc.thePlayer.displayName.formattedText.replace("§r", "")
            if (targetName.startsWith("T") && clientName.startsWith("T"))
                if (targetName[1].isDigit() && clientName[1].isDigit())
                    return targetName[1] == clientName[1]
        }

        if (colorValue.value && mc.thePlayer.displayName != null && entity.displayName != null) {
            val targetName = entity.displayName.formattedText.replace("§r", "")
            val clientName = mc.thePlayer.displayName.formattedText.replace("§r", "")
            return targetName.startsWith("§${clientName[1]}")
        }

        return false
    }
}