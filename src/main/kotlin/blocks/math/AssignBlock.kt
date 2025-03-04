package jsTools.blocks.math

import jsTools.blocks.BaseBlock
import jsTools.blocks.BlockParameter

class AssignBlock : BaseBlock() {
    init {
        text = mutableListOf(BlockParameter("类型","变量类型",true,"let", mutableListOf(Pair("var","可变，可覆盖"),Pair("let","可变，不可覆盖"),Pair("const","不可变，不可覆盖"))),
            BlockParameter("名称","变量名称",true,"名字"),
            BlockParameter("名称","变量名称",true,1))
    }
    override fun toString(): String {
        return text[0].value.toString() + " " + text[1].value + " = " + text[2].value
    }
}