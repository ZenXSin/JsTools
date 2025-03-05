package jsTools.blocks.event

import arc.Events
import jsTools.Vars
import jsTools.blocks.BlockParameter
import jsTools.blocks.BlockParameters
import jsTools.blocks.BlockTypes

class EventBlock: jsTools.blocks.BaseBlock() {

    init {
        name = "注册事件"
        type = Vars.BlockTypes.event
        desc = "注册一个事件，当此事件被触发时内部代码块将被运行"
        text = BlockParameters(
            mutableListOf(
                "事件" to mutableListOf(BlockParameter("事件", "注册的事件", true, Vars.Events.events[0].second, Vars.Events.events))
            )
        )
    }

    class EventBuild(block: EventBlock,scope: String): BaseBuild(block, scope) {


        val blocks: MutableList<BaseBuild> = mutableListOf()

        fun addBlock(block: BaseBuild) {
            blocks.add(block)
        }

        fun removeBlock(block: BaseBuild) {
            blocks.remove(block)
        }

        override fun toJsText(): String {
            val event = getNowText()[0].value

            var sb = ""
            if (event == "ModLoadEvent") {
                for (block in blocks) {
                    sb += "${block.toJsText()}\n"
                }
                return sb
            }
            sb += "Events.on(EventType.$event, () => {\n"
            for (block in blocks) {
                sb += "${block.toJsText()}\n"
            }
            sb += "});\n"
            return sb
        }
    }

    override fun build(block: jsTools.blocks.BaseBlock, scope: String): EventBuild {
        return EventBuild(block as EventBlock, scope)
    }

}

fun main() {
}