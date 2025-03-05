package jsTools.blocks

class BlockParameter(val name:String,val desc:String,val required: Boolean,var value: String, val elective:MutableList<Pair<String,String>> = mutableListOf() )

class BlockParameters(val parameters:MutableList<Pair<String,MutableList<BlockParameter>>> = mutableListOf(), val tip:String = "")