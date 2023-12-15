class State(values: List<String>) {
    companion object {
        enum class T { Round, Square, Empty }
    }

    val width = values[0].length
    val height = values.size
    val state = Array(height * width) { T.Empty }

    init {
        values.forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                state[y * width + x] = charToT(c)
            }
        }
    }

    private fun charToT(c: Char): T {
        if (c == '#') return T.Square
        else if (c == 'O') return T.Round
        else return T.Empty
    }

    fun tiltNorth() {
        for (x in 0..<width) {
            for (y in 0..<height) {
                if (state[y * width + x] == T.Round) {
                    var newY = y
                    while (newY > 0 && state[(newY - 1) * width + x] == T.Empty) { newY -= 1 }
                    if (newY != y) {
                        state[newY * width + x] = T.Round
                        state[y * width + x] = T.Empty
                    }
                }
            }
        }
    }

    fun tiltWest() {
        for (x in 0..<width) {
            for (y in 0..<height) {
                if (state[y * width + x] == T.Round) {
                    var newX = x
                    while (newX > 0 && state[y * width + (newX - 1)] == T.Empty) { newX -= 1 }
                    if (newX != x) {
                        state[y * width + newX] = T.Round
                        state[y * width + x] = T.Empty
                    }
                }
            }
        }
    }

    fun tiltSouth() {
        for (x in 0..<width) {
            for (y in height-1 downTo 0) {
                if (state[y * width + x] == T.Round) {
                    var newY = y
                    while (newY < height - 1 && state[(newY + 1) * width + x] == T.Empty) { newY += 1 }
                    if (newY != y) {
                        state[newY * width + x] = T.Round
                        state[y * width + x] = T.Empty
                    }
                }
            }
        }
    }

    fun tiltEast() {
        for (x in width-1 downTo 0) {
            for (y in 0..<height) {
                if (state[y * width + x] == T.Round) {
                    var newX = x
                    while (newX < width - 1 && state[y * width + (newX + 1)] == T.Empty) { newX += 1 }
                    if (newX != x) {
                        state[y * width + newX] = T.Round
                        state[y * width + x] = T.Empty
                    }
                }
            }
        }
    }

    fun cycle() {
        tiltNorth()
//        showState()
        tiltWest()
//        showState()
        tiltSouth()
//        showState()
        tiltEast()
//        showState()
    }

    fun showState() {
        println(state.toList().chunked(width).joinToString("\n") { it.joinToString("") { tToChar(it).toString() } })
        println()
        println()
    }

    private fun tToChar(t: T): Char {
        if (t == T.Empty) return '.'
        else if (t == T.Round) return 'O'
        else return '#'
    }

    fun weightNorth(): Long {
        var total = 0L
        for (x in 0..<width) {
            for (y in 0..<height) {
                if (state[y * width + x] == T.Round) {
                    total += height - y
                }
            }
        }
        return total
    }

}

fun main() {

    fun part1(input: List<String>): Long {
        val state = State(input)
        state.tiltNorth()
        return state.weightNorth()
    }

    fun part2(input: List<String>): Long {
        val state = State(input)
        val history = mutableListOf(state.state.contentDeepHashCode())
        state.cycle()
        while (state.state.contentDeepHashCode() !in history) {
            history.add(state.state.contentDeepHashCode())
            state.cycle()
            println("${history.size - 1} - ${state.weightNorth()}")
        }
        println("state at index ${history.indexOf(state.state.contentDeepHashCode())} is the same as state at index ${history.size}.")
        val cycleCycle = history.size - history.indexOf(state.state.contentDeepHashCode())
        val correctCycle = 1000000000 % cycleCycle
        println(correctCycle)
        println(cycleCycle)
        return 0
    }

    val input = readInput("Day14")

    part1(input).println()
    part2(input).println()

}
