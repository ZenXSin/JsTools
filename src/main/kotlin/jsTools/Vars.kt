package jsTools

import jsTools.core.VarsManagement
import jsTools.core.events.Events
import jsTools.core.type.Types
import java.util.jar.JarFile

object Vars {
    val VarsManagement = VarsManagement()
    val FunctionManagement = jsTools.core.FunctionManagement()
    val Events = Events()
    val BlockTypes = jsTools.blocks.BlockTypes()
    val Types = Types()

    fun load(p:JarFile) {
        VarsManagement.load()
        FunctionManagement.load()
        Events.load()
        Types.load(p)
    }
}