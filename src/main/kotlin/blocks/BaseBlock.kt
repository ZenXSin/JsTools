package jsTools.blocks

import jsTools.Vars

open class BaseBlock {
    var name: String = ""
    var desc: String = ""
    var type: BlockType = Vars.BlockTypes.other
    var text: BlockParameters = BlockParameters()

    open class BaseBuild(val block: BaseBlock, val scope: String) {
        private var nowText: MutableList<BlockParameter> = mutableListOf()

        open fun update() {

        }

        open fun isTurn(): Pair<Boolean, String> {
            return true to "无问题"
        }

        open fun use() {
        }

        open fun unUse() {

        }


        open fun setNowText(n: MutableList<BlockParameter>) {
            nowText = n
        }

        open fun getNowText(): MutableList<BlockParameter> {
            return nowText
        }

        open fun toJsText(): String  {
            return ""
        }

    }


    open fun build(block: BaseBlock, scope: String): BaseBuild {
        return BaseBuild(block, scope)
    }

}