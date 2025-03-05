package jsTools.core

class VarsManagement {
    val vars: MutableList<Pair<String,Var>> = mutableListOf()

    class Var(default: String) {
        var name: String = ""
        var scope: String = ""
        var value: String = default

        override fun toString(): String {
            return "name:$name,scope:$scope,value:$value"
        }
    }

    fun load() {

    }

    fun add(name: String,scope: String,default: String) {
        val addVar = Var(default)
        addVar.name = name
        addVar.scope = scope

        vars.add(Pair("$scope-$name",addVar))
    }

    fun get(name: String): Var? {
        for (vars in vars) {
            if (vars.first == name) {
                return vars.second
            }
        }
        return null
    }

    fun remove(name: String) {
        for (vas in vars) {
            if (vas.second.name == name) {
                vars.remove(vas)
            }
        }
    }

    fun contains(name: String): Boolean {
        for (vas in vars) {
            if (vas.first == name) {
                return true
            }
        }
        return false
    }
}