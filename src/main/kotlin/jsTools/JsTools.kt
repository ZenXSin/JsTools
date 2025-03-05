package jsTools

import java.io.File
import java.util.jar.JarFile

abstract class JsTools {
    /**读取依赖库**/
    open fun readLib(): JarFile {
        return JarFile("")
    }

    /**读取配置**/
    open fun readJson(): Map<String, File> {
        return mapOf()
    }

    /**写入配置**/
    open fun writeJson(i: Map<String, File>) {

    }

    fun load() {
        Vars.load(readLib())
    }
}

fun main() {
    object : JsTools() {

    }
}