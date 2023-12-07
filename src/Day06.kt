import kotlin.math.sqrt

fun main() {

    fun part1(input: List<String>): Long {
        val times = input[0].split(Regex(" +")).drop(1).map { it.toInt() }
        val distances = input[1].split(Regex(" +")).drop(1).map { it.toInt() }

        return times.mapIndexed { index, time ->
            val distance = distances[index]
            (0..time).map { load ->
                (time - load) * load
            }.count { it > distance }
        }.fold(1) { acc, v -> acc * v }
    }

    fun part2(input: List<String>): Int {
        val time = input[0].split(Regex(":"))[1].replace(" ", "").toLong()
        val distance = input[1].split(Regex(":"))[1].replace(" ", "").toLong()

        println(2 * time - sqrt(time.toDouble() * time - 4 * distance))

        return (0..time).map { load ->
            (time - load) * load
        }.also {
            println(it.indexOfLast { it > distance } - it.indexOfFirst { it > distance } + 1)
        }.count { it > distance }
    }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
