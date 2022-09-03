package cn.feng.ikun.utils

import cn.feng.ikun.module.Module

object KtUtils {
    fun sortModulesByName(modules: List<Module>) : List<Module> {
        return modules.sortedBy { it.name }
    }
}