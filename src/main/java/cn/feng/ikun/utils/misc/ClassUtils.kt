package cn.feng.ikun.utils.misc

import cn.feng.ikun.value.Value

object ClassUtils {
    @JvmStatic
    fun getValues(clazz: Class<*>, instance: Any) = clazz.declaredFields.map { valueField ->
        valueField.isAccessible = true
        valueField[instance]
    }.filterIsInstance<Value>()
}