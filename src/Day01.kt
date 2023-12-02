fun main() {

    val numbersMap = mapOf(
        "0" to 0,
        "1" to 1,
        "2" to 2,
        "3" to 3,
        "4" to 4,
        "5" to 5,
        "6" to 6,
        "7" to 7,
        "8" to 8,
        "9" to 9,
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
    )
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            line.first { it.isDigit() }.digitToInt() * 10 + line.last { it.isDigit() }.digitToInt()
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            var first: Int? = null
            var last = 0
            for (i in line.indices) {
                for (key in numbersMap.keys) {
                    if (line.substring(i).startsWith(key)) {
                        first = first ?: numbersMap[key]
                        last = numbersMap[key] ?: 0
                    }
                }
            }
            (first ?: 0) * 10 + last
        }
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
