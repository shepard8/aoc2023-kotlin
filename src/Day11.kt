import kotlin.math.abs

fun main() {

    data class Galaxy(val x: Long, val y: Long) {
        fun distanceTo(other: Galaxy): Long {
            return abs(x - other.x) + abs(y - other.y)
        }
    }

    fun part1(input: List<String>): Long {
        val emptyXs = ((0..<input.first().length).filter { col -> input.all { line -> line.get(col) == '.' } })
        println(emptyXs)
        val emptyYs = input.mapIndexed { y, line -> y.takeIf { !line.contains('#') } }.filterNotNull()
        println(emptyYs)
        val galaxies = input.flatMapIndexed { y, line -> line.mapIndexed { x, c -> c.takeIf { c == '#' }?.let {
            Galaxy(x + 1L * emptyXs.count { it < x }, y + 1L * emptyYs.count { it < y })
        } } }.filterNotNull()

        return galaxies.sumOf { g1 -> galaxies.sumOf { g2 -> g1.distanceTo(g2) } } / 2L
    }

    fun part2(input: List<String>): Long {
        val emptyXs = ((0..<input.first().length).filter { col -> input.all { line -> line.get(col) == '.' } })
        println(emptyXs)
        val emptyYs = input.mapIndexed { y, line -> y.takeIf { !line.contains('#') } }.filterNotNull()
        println(emptyYs)
        val galaxies = input.flatMapIndexed { y, line -> line.mapIndexed { x, c -> c.takeIf { c == '#' }?.let {
            Galaxy(x + 999999L * emptyXs.count { it < x }, y + 999999L * emptyYs.count { it < y })
        } } }.filterNotNull()

        return galaxies.sumOf { g1 -> galaxies.sumOf { g2 -> g1.distanceTo(g2) }} / 2
    }

    val input = readInput("Day11")

    part1(input).println()
    part2(input).println()
}
