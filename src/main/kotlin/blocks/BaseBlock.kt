package jsTools.blocks

import jsTools.Vars

open class BaseBlock {
    var name: String = ""
    var desc: String = ""
    var type: BlockType = Vars.BlockTypes.other
    var text: BlockParameters = BlockParameters()
    var nowText: MutableList<BlockParameter> = mutableListOf()
    open class BaseBlockBuild(val block: BaseBlock, val scope: String) {
        open fun update() {

        }

        open fun isTurn():Pair<Boolean, String> {
            return true to "无问题"
        }

        open fun use() {
        }

        open fun unUse() {

        }

        open fun set(n: MutableList<BlockParameter>) {

        }
    }

    open fun build(block: BaseBlock, scope: String): BaseBlockBuild {
        return BaseBlockBuild(block, scope)
    }

}