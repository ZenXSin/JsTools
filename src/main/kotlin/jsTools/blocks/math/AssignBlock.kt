package jsTools.blocks.math

import jsTools.Vars
import jsTools.blocks.BlockParameter
import jsTools.blocks.BlockParameters

class AssignBlock : jsTools.blocks.BaseBlock() {
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

    class AssignBuild(block: AssignBlock, scope: String) : BaseBuild(block, scope) {

        override fun toJsText(): String {
            val nowText = getNowText()
            return nowText[0].value + " " + nowText[1].value + " = " + nowText[2].value + ";"
        }

        override fun setNowText(n: MutableList<BlockParameter>) {
            super.setNowText(n)
            Vars.VarsManagement.remove("$scope-${getNowText()[1].name}")
            Vars.VarsManagement.add(
                getNowText()[1].value,
                scope,
                getNowText()[2].value,
            )


        }

        override fun isTurn(): Pair<Boolean, String> {
            if (Vars.VarsManagement.contains("$scope-${getNowText()[1].name}")) {
                return Pair(true, "变量已存在")
            }
            return super.isTurn()
        }
    }

    override fun build(block: jsTools.blocks.BaseBlock, scope: String): AssignBuild {
        return AssignBuild(block as AssignBlock, scope)
    }
}

fun main() {
    Vars.load()
    val ab = AssignBlock()
    val abBuild = ab.build(ab, "Main")
    abBuild.setNowText(ab.text.parameters[0].second)

    println(abBuild.toJsText())
}