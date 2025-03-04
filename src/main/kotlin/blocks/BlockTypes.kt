package jsTools.blocks

import kotlin.reflect.typeOf

class BlockType(val name: String,val desc: String = "")

object BlockTypes {
    val assign = BlockType("assign","赋值")
    val other = BlockType("other","其他")
    init {
    }
}