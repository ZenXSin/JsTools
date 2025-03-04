package jsTools.core

class VarsManagement {
    class Var<i>(default: i? = null) {
        val name: String = ""
        val packageName: String = ""
        var value: i? = default
    }
}