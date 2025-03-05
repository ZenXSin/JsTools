package jsTools.blocks

import kotlin.reflect.typeOf

class BlockType(val name: String,val desc: String = "")

class BlockTypes {
    val assign = BlockType("assign","赋值")
    val event = BlockType("event","事件")
    val other = BlockType("other","其他")

    fun load() {

    }
}