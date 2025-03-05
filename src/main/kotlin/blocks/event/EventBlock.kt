package jsTools.blocks.event

import arc.Events
import jsTools.Vars
import jsTools.blocks.BaseBlock
import jsTools.blocks.BlockTypes
import jsTools.blocks.math.AssignBlock

class EventBlock(val event: String): BaseBlock() {
    val blocks: MutableList<BaseBlock> = mutableListOf()

    init {
        name = "注册事件"
        type = Vars.BlockTypes.event
        desc = "注册一个事件，当此事件被触发时代码块将被运行"
    }

    fun addBlock(block: BaseBlock) {
        blocks.add(block)
    }

    override fun toString(): String {
        var sb = ""
        if (event == "ModLoadEvent") {
            for (block in blocks) {
                sb += "$block\n"
            }
            return sb
        }
        sb += "Events.on(EventType.$event, () => {\n"
        for (block in blocks) {
            sb += "$block\n"
        }
        sb += "});\n"
        return sb
    }

}

fun main() {
    val eb = EventBlock("ModLoadEvent")
    eb.addBlock(AssignBlock())
    eb.addBlock(AssignBlock())
    eb.addBlock(AssignBlock())
    println(eb.toString())
}