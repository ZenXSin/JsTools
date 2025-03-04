package jsTools

import jsTools.core.events.Events
import jsTools.core.type.TypeOf
import jsTools.core.type.Types
import java.util.Date
import javax.xml.crypto.Data

fun main() {
    val t = Date().time
    Types.load()
    Events.load()
    println("加载时间: ${Date().time - t}ms")
}