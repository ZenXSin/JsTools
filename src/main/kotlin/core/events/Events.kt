package jsTools.core.events

import mindustry.game.EventType

class Events {
    val events: MutableList<Pair<String, String>> = mutableListOf()
    fun load() {
        try {
            EventType::class.java.classes.forEach { field ->
                if (field.simpleName != "Trigger") {
                    events.add(field.simpleName to field.simpleName)
                } else {
                    for (i in field.fields) {
                        events.add(i.name to i.name)
                    }
                }
            }
            events.add("模组被加载" to "ModLoadEvent")
            println("事件生成: OK")
        } catch (e: Exception) {
            e.printStackTrace()
            println("事件生成: Failed")
        }
    }
}