package jsTools.blocks.math

import jsTools.Vars
import jsTools.blocks.BaseBlock
import jsTools.blocks.BlockParameter
import jsTools.blocks.BlockParameters

class AssignBlock : BaseBlock() {
    init {
        type = Vars.BlockTypes.assign
        name = "赋值"
        desc = "新建变量"
        text = BlockParameters(
            mutableListOf(
                "基础类型" to mutableListOf(
                    BlockParameter(
                        "类型",
                        "变量类型",
                        true,
                        "let",
                        mutableListOf(
                            Pair("let", "可变"),
                            Pair("const", "不可变")
                        )
                    ),
                    BlockParameter("名称", "变量名称", true, "名字"),
                    BlockParameter("默认值", "变量默认值", true, "null"),
                    BlockParameter("默认值类型", "变量默认值类型", true, "null", Vars.Types.jsTypes)
                ),
                "其他类型" to mutableListOf(
                    BlockParameter(
                        "类型",
                        "变量类型",
                        true,
                        "let",
                        mutableListOf(
                            Pair("let", "可变"),
                            Pair("const", "不可变")
                        )
                    ),
                    BlockParameter("名称", "变量名称", true, "名字"),
                    BlockParameter("默认值", "变量默认值", true, "null"),
                    BlockParameter(
                        "默认值类型",
                        "变量默认值类型",
                        true,
                        Vars.Types.mindustryType[0].first,
                        Vars.Types.mindustryType
                    )
                )
            ), "变量类型"
        )
    }

    override fun toString(): String {
        return nowText[0].value + " " + nowText[1].value + " = " + nowText[2].value + ";"
    }

    class AssignBlockBuild(block: AssignBlock, scope: String) : BaseBlockBuild(block, scope) {
        override fun use() {
            Vars.VarsManagement.add(block.nowText[1].name, scope, block.nowText[1].value)
        }

        override fun unUse() {
            Vars.VarsManagement.remove(block.nowText[1].name)
        }

        override fun isTurn(): Pair<Boolean, String> {
            if (Vars.VarsManagement.contains("$scope-${block.nowText[1].name}")) {
                return Pair(true, "变量已存在")
            }
            return super.isTurn()
        }
    }
}