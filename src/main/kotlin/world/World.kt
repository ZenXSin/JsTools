package jsTools.world

import jsTools.Vars
import jsTools.blocks.BaseBlock
import jsTools.blocks.event.EventBlock
import jsTools.blocks.math.AssignBlock

class World {
    var name = "New World"
    var description = ""
    private var blocks: MutableList<Pair<BaseBlock, BaseBlock.BaseBuild>> = mutableListOf()

    fun add(block: BaseBlock,build: BaseBlock.BaseBuild) {
        blocks.add(Pair(block, build))
    }

    fun remove(block: BaseBlock) {
        blocks.forEach {
            if (it.first == block) {
                blocks.remove(it)
                return
            }
        }
    }

    override fun toString(): String {
        var ret = ""

        for (block in blocks) {
            ret += block.second.toJsText() + "\n"
        }

        return ret
    }
}

fun main() {
    Vars.load() //加载Vars

    val world = World() //新建一个项目

    val ab = AssignBlock()//新建一个赋值块
    val abBuild = ab.build(ab, "Main")//创建一个赋值块的实例
    abBuild.setNowText(ab.text.parameters[0].second)//设置赋值块的参数
    world.add(ab, abBuild)//添加赋值块到项目

    val eb = EventBlock()//新建一个事件块
    val ebBuild = eb.build(eb, "Main")//创建一个事件块的实例
    ebBuild.setNowText(eb.text.parameters[0].second)//设置事件块的参数

    val ab1 = AssignBlock()//新建一个赋值块
    val abBuild1 = ab.build(ab, ebBuild.scope + "-" + ebBuild.getNowText()[0].value)//创建一个赋值块的实例
    abBuild1.setNowText(ab1.text.parameters[0].second)//设置赋值块的参数

    ebBuild.addBlock(abBuild1)//添加赋值块到事件块
    world.add(eb, ebBuild)//添加事件块到项目


    Vars.VarsManagement.vars.forEach {
        println(it.first + "     " + it.second.toString())
    }

    println(world.toString()) //打印项目

}