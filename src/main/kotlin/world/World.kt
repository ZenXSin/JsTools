package jsTools.world

import jsTools.Vars
import jsTools.blocks.BaseBlock
import jsTools.blocks.event.EventBlock
import jsTools.blocks.math.AssignBlock

class World {
    var name = "New World"
    var description = ""
    private var blocks: MutableList<Pair<BaseBlock, BaseBlock.BaseBlockBuild>> = mutableListOf()

    fun add(block: BaseBlock,scope: String) {
        blocks.add(Pair(block, block.build(block, scope)))
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
            ret += block.first.toString() + "\n"
        }

        return ret
    }
}

fun main() {
    Vars.load() //加载Vars

    val world = World() //新建一个项目

    val eb = EventBlock("ModLoadEvent") //新建一个Mod加载事件

    val ab = AssignBlock() //新建一个赋值块

    ab.nowText = ab.text.parameters[0].second //设置赋值块的变量类型为基础类型

    ab.nowText[1].value = "a" //设置赋值块的变量名称为a

    ab.nowText[2].value = "1" //设置赋值块的变量默认值为1

    ab.nowText[3].value = "number" //设置赋值块的变量默认值类型为number
    eb.addBlock(ab) //将赋值块添加到事件块中

    world.add(eb,"Main") //将事件块添加到项目中

    println(Vars.VarsManagement.vars)

    println(world.toString()) //打印项目

}