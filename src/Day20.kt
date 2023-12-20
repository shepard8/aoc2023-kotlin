abstract class Module {

    companion object {
        val waitingPulses: MutableList<Pulse> = mutableListOf()
        var lowPulsesCount: Long = 0
        var highPulsesCount: Long = 0

        fun addPulse(pulse: Pulse) {
            waitingPulses.add(pulse)
            if (pulse.high) {
                highPulsesCount++
            }
            else {
                lowPulsesCount++
            }
        }
    }

    private val incomingModules: MutableList<Module> = mutableListOf()
    val outgoingModules: MutableList<Module> = mutableListOf()

    open fun addIncomingModule(module: Module) {
        incomingModules.add(module)
    }

    open fun clearIncomingModules() {
        incomingModules.clear()
    }

    abstract fun receive(from: Module, high: Boolean)

}

class ButtonModule: Module() {

    var pushes = 0

    override fun receive(from: Module, high: Boolean) {
        error("button should not receive pulses")
    }

    fun push() {
        pushes++
        Module.lowPulsesCount++
        outgoingModules.map { Pulse(false, this, it) }.forEach { Module.addPulse(it) }
    }
}

class FlipFlopModule: Module() {

    var state = false

    override fun receive(from: Module, high: Boolean) {
        if (!high) {
            state = !state
            outgoingModules.map { Pulse(state, this, it) }.forEach { Module.addPulse(it) }
        }
    }

}

class ConjunctionModule: Module() {

    val states = hashMapOf<Module, Boolean>()

    override fun addIncomingModule(module: Module) {
        super.addIncomingModule(module)
        states[module] = false
    }

    override fun clearIncomingModules() {
        super.clearIncomingModules()
        states.clear()
    }

    override fun receive(from: Module, high: Boolean) {
        states[from] = high
        outgoingModules.map { to -> Pulse(states.values.any { !it }, this, to) }.forEach { Module.addPulse(it) }
    }

}

class EndModule : Module() {

    var countLow = 0
    var countHigh = 0

    override fun receive(from: Module, high: Boolean) {
        if (!high) {
            countLow++
        }
        else {
            countHigh++
        }
    }

}

data class Pulse(val high: Boolean, val from: Module, val to: Module) {
    fun process() {
//        println("Processing $from --> ${if (high) "high" else "low"} --> $to")
        to.receive(from, high)
    }
}

fun main() {

    fun part1(input: List<String>): Long {
        val modules = input.associateBy({
            it.dropWhile { it == '%' || it == '&' }.split(" -> ")[0]
        }) {
            when (it.first()) {
                '&' -> ConjunctionModule()
                '%' -> FlipFlopModule()
                else -> ButtonModule()
            }
        }.toMutableMap()

        input.forEach {
            val (from, tos) = it.dropWhile { it == '%' || it == '&' }.split(" -> ")
            tos.split(", ").forEach { to ->
                println("$from $to")
                if (!modules.contains(to)) {
                    modules[to] = EndModule()
                }
                modules[from]?.outgoingModules?.add(modules[to] ?: error("")) ?: error("")
                modules[to]?.addIncomingModule(modules[from] ?: error("")) ?: error("")
            }
        }

        repeat(1000) {
            (modules["broadcaster"] as ButtonModule).push()
            while (Module.waitingPulses.isNotEmpty()) {
                Module.waitingPulses.removeFirst().process()
            }
        }

        return Module.lowPulsesCount * Module.highPulsesCount
    }

    fun subpart2(input: List<String>, start: String, end: String): Int {
        val modules = input.associateBy({
            it.dropWhile { it == '%' || it == '&' }.split(" -> ")[0]
        }) {
            when (it.first()) {
                '&' -> ConjunctionModule()
                '%' -> FlipFlopModule()
                else -> ButtonModule()
            }
        }.toMutableMap()

        input.forEach {
            val (from, tos) = it.dropWhile { it == '%' || it == '&' }.split(" -> ")
            tos.split(", ").forEach { to ->
                println("$from $to")
                if (!modules.contains(to)) {
                    modules[to] = EndModule()
                }
                modules[from]?.outgoingModules?.add(modules[to] ?: error("")) ?: error("")
                modules[to]?.addIncomingModule(modules[from] ?: error("")) ?: error("")
            }
        }

        modules["broadcaster"]!!.outgoingModules.clear()
        modules["broadcaster"]!!.outgoingModules.add(modules[start]!!)
        modules["lg"]!!.clearIncomingModules()
        modules["lg"]!!.addIncomingModule(modules[end]!!)

        while ((modules["rx"] as EndModule).countLow != 1) {
            (modules["rx"] as EndModule).countLow = 0
            (modules["broadcaster"] as ButtonModule).push()
            while (Module.waitingPulses.isNotEmpty()) {
                Module.waitingPulses.removeFirst().process()
            }
        }
        println((modules["broadcaster"] as ButtonModule).pushes)
        (modules["rx"] as EndModule).countLow = 0

        while ((modules["rx"] as EndModule).countLow != 1) {
            (modules["broadcaster"] as ButtonModule).push()
            while (Module.waitingPulses.isNotEmpty()) {
                Module.waitingPulses.removeFirst().process()
            }
        }
        println((modules["broadcaster"] as ButtonModule).pushes)

        return (modules["broadcaster"] as ButtonModule).pushes
    }

    fun part2(input: List<String>): Long {
        return lcm(3881L, lcm(3931L, lcm(3943L, 3851L)))
    }

    val input = readInput("Day20")

    part1(input).println()
    part2(input).println()
//    subpart2(input, "sj", "vc").println()
//    subpart2(input, "nk", "vg").println()
//    subpart2(input, "tp", "ls").println()
//    subpart2(input, "sr", "nb").println()
}
