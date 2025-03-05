package jsTools

import java.util.Date

fun main() {
    val t = Date().time
    Vars.load()
    println("加载时间: ${Date().time - t}ms")
}