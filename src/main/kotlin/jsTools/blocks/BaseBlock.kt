package jsTools.blocks

import jsTools.Vars

open class BaseBlock {
    var name: String = ""
    var desc: String = ""
    var type: jsTools.blocks.BlockType = Vars.BlockTypes.other
    var text: jsTools.blocks.BlockParameters = jsTools.blocks.BlockParameters()

    open class BaseBuild(val block: jsTools.blocks.BaseBlock, val scope: String) {
        private var nowText: MutableList<jsTools.blocks.BlockParameter> = mutableListOf()

        open fun update() {

        }

        open fun isTurn(): Pair<Boolean, String> {
            return true to "无问题"
        }

        open fun use() {
        }

        open fun unUse() {

        }


        open fun setNowText(n: MutableList<jsTools.blocks.BlockParameter>) {
            nowText = n
        }

        open fun getNowText(): MutableList<jsTools.blocks.BlockParameter> {
            return nowText
        }

        open fun toJsText(): String  {
            return ""
        }

    }


    open fun build(block: jsTools.blocks.BaseBlock, scope: String): jsTools.blocks.BaseBlock.BaseBuild {
        return jsTools.blocks.BaseBlock.BaseBuild(block, scope)
    }

}