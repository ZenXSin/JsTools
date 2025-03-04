package jsTools

import jsTools.blocks.BaseBlock

val b = BaseBlock()

b.text[0].value = b.text[0].elective[0].first

println(b.toString())

