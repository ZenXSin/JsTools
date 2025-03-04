package jsTools.blocks

class BlockParameter(val name:String,val desc:String,val required: Boolean,var value: Any, val elective:MutableList<Pair<Any,String>> = mutableListOf() )