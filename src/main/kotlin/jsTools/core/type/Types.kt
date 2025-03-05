package jsTools.core.type

import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarEntry
import java.util.jar.JarFile

class Types {
    var mindustryType: MutableList<Pair<String, String>> = mutableListOf()
    var jsTypes :MutableList<Pair<String, String>> = mutableListOf()

    fun load(p:JarFile) {
        try {
            mindustryType = getMindustryTypes(p)
            jsTypes = fetchJsTypes() // 修改方法调用名称
            println("类型生成: OK")
        } catch (e: Exception) {
            e.printStackTrace()
            println("类型生成: Failed")
        }
    }

    // 修改方法名称
    private fun fetchJsTypes(): MutableList<Pair<String, String>> {
        val types: MutableList<Pair<String, String>> = mutableListOf()

        types.add(Pair("Int", "整数"))
        types.add(Pair("Float", "浮点数"))
        types.add(Pair("String", "字符串"))
        types.add(Pair("Boolean", "布尔值"))

        types.add(Pair("List", "列表"))

        types.add(Pair("Function", "函数"))

        return types
    }

    private fun getMindustryTypes(p:JarFile): MutableList<Pair<String, String>> {
        val types: MutableList<Pair<String, String>> = mutableListOf()
        val classes = getClassesFromJar(p)
        for (clazz in classes) {
            if (clazz.name.contains("mindustry") && clazz.simpleName != "") {
                types.add(Pair(clazz.simpleName, clazz.simpleName))
            }
        }
        return types
    }
}

fun getClassesFromJar(jarFile: JarFile): List<Class<*>> {
    val classes = mutableListOf<Class<*>>()
    val entries = jarFile.entries()
    // 根据 JarFile 的路径创建 URLClassLoader
    val classLoader = URLClassLoader(arrayOf(URL("file:${jarFile.name}")))

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
    // 关闭传入的 JarFile 对象
    jarFile.close()
    return classes
}