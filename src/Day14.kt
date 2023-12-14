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
        return 0
    }

    val input = readInput("Day14")

    part1(input).println()
    part2(input).println()

}
