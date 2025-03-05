package jsTools.core

class FunctionManagement {
    val functions: MutableList<Pair<jsTools.blocks.BaseBlock,String>> = mutableListOf()

    fun add(block: jsTools.blocks.BaseBlock, code: String) {
        functions.add(Pair(block,code))
    }

    fun load() {

    }
}