package jsTools.core.events

import mindustry.game.EventType

class Events {
    val events: MutableList<String> = mutableListOf()
    fun load() {
        try {
            EventType::class.java.classes.forEach { field ->
                if (field.simpleName != "Trigger") {
                    events.add(field.simpleName)
                } else {
                    for (i in field.fields) {
                        events.add(i.name)
                    }
                }
            }
            events.add("ModLoadEvent")
            println("事件加载: OK")
        } catch (e: Exception) {
            e.printStackTrace()
            println("事件加载: Failed")
        }
    }
}