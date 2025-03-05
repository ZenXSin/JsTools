package jsTools.blocks

class BlockType(val name: String,val desc: String = "")

class BlockTypes {
    val assign = jsTools.blocks.BlockType("assign", "赋值")
    val event = jsTools.blocks.BlockType("event", "事件")
    val other = jsTools.blocks.BlockType("other", "其他")

    fun load() {

    }
}