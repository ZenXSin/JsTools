package jsTools.core

import jsTools.blocks.BaseBlock

class FunctionManagement {
    val functions: MutableList<Pair<BaseBlock,String>> = mutableListOf()

    fun add(block: BaseBlock,code: String) {
        functions.add(Pair(block,code))
    }

    fun load() {

    }
}