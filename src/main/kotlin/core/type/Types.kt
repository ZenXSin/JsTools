package jsTools.core.type

import java.net.URL
import java.net.URLClassLoader
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile

object Types {
    val types: MutableList<Pair<String, String>> = mutableListOf()
    fun load() {
        try {
            types.add(Pair("Int", "整数"))
            types.add(Pair("Float", "浮点数"))
            types.add(Pair("String", "字符串"))
            types.add(Pair("Boolean", "布尔值"))

            types.add(Pair("List", "列表"))

            types.add(Pair("Function", "函数"))

            val jarFilePath = "libs/host.jar"
            val classes = getClassesFromJar(jarFilePath)
            for (clazz in classes) {
                if (clazz.name.contains("mindustry") && clazz.simpleName != "") {
                    types.add(Pair(clazz.simpleName, clazz.simpleName))
                }
            }
            println("类型生成: OK")
        } catch (e: Exception) {
            e.printStackTrace()
            println("类型生成: Failed")
        }

    }
}

fun getClassesFromJar(jarFilePath: String): List<Class<*>> {
    val classes = mutableListOf<Class<*>>()
    val jarFile = JarFile(jarFilePath)
    val entries = jarFile.entries()
    val classLoader = URLClassLoader(arrayOf(URL("file:$jarFilePath")))

    while (entries.hasMoreElements()) {
        val entry: JarEntry = entries.nextElement()
        if (entry.name.endsWith(".class") && !entry.name.contains("module-info")) {
            val className = entry.name
                .replace('/', '.')
                .substring(0, entry.name.length - 6)
            try {
                val clazz = classLoader.loadClass(className)
                if (clazz.name.contains("mindustry")) {
                    classes.add(clazz)
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
    }
    jarFile.close()
    return classes
}

fun main() {
    Types.load()
}