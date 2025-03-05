package jsTools

import jsTools.blocks.BlockTypes
import jsTools.core.VarsManagement
import jsTools.core.FunctionManagement
import jsTools.core.events.Events
import jsTools.core.type.Types

object Vars {
    val VarsManagement = VarsManagement()
    val FunctionManagement = FunctionManagement()
    val Events = Events()
    val BlockTypes = BlockTypes()
    val Types = Types()

    fun load() {
        VarsManagement.load()
        FunctionManagement.load()
        Events.load()
        Types.load()
    }
}