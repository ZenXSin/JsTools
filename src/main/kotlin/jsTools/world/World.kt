package jsTools.world

import jsTools.Vars
import jsTools.blocks.event.EventBlock
import jsTools.blocks.math.AssignBlock

class World {
    var name = "New World"
    var description = ""
    private var blocks: MutableList<Pair<jsTools.blocks.BaseBlock, jsTools.blocks.BaseBlock.BaseBuild>> = mutableListOf()

    fun add(block: jsTools.blocks.BaseBlock, build: jsTools.blocks.BaseBlock.BaseBuild) {
        blocks.add(Pair(block, build))
    }

    fun remove(block: jsTools.blocks.BaseBlock) {
        blocks.forEach {
            if (it.first == block) {
                blocks.remove(it)
                return
            }
        }
    }

    override fun toString(): String {
        var ret = ""

        for (block in blocks) {
            ret += block.second.toJsText() + "\n"
        }

        return ret
    }
}
