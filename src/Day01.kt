fun main() {

    val numbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            line.first { it.isDigit() }.digitToInt() * 10 + line.last { it.isDigit() }.digitToInt()
        }
    }

    fun part2(input: List<String>): Int {
        return part1(
            input
                .map { numbers.fold(it) { s, number -> s.replace(number, number + number.last()) } }
                .map { numbers.foldIndexed(it) { i, s, number -> s.replace(number, "${i + 1}") } }
        )
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
