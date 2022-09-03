package cn.feng.ikun.utils.misc

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import java.io.File

object FileUtils {
    fun write(file : File, text : JsonElement) {
        file.writeText(GsonBuilder().setPrettyPrinting().create().toJson(text), Charsets.UTF_8)
    }
}