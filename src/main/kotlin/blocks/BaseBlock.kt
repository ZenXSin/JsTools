package jsTools.blocks

open class BaseBlock {
    var name: String = ""
    var desc: String = ""
    var type: BlockType = BlockTypes.other
    var text: MutableList<BlockParameter> = mutableListOf()
}