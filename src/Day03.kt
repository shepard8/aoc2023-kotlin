fun main() {

    fun part1(input: List<String>): Int {
        val symbols = getSymbols(input)
        val parts = getParts(input).filter { part -> symbols.any { symbol -> isAdjacentTo(part, symbol) } }

        return parts.sumOf { it.number }
    }

    fun part2(input: List<String>): Int {
        val symbols = getSymbols(input)
        val parts = getParts(input).filter { part -> symbols.any { symbol -> isAdjacentTo(part, symbol) } }

        return symbols
            .filter { it.symbol == '*' }
            .filter { gear -> parts.count { isAdjacentTo(it, gear) } == 2 }
            .sumOf { gear -> parts.filter { part -> isAdjacentTo(part, gear) }.fold(1) { acc: Int, part -> acc * part.number } }
    }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

data class Part(val x: IntRange, val y: Int, val number: Int)
data class Symbol(val x: Int, val y: Int, val symbol: Char)

fun getParts(input: List<String>): List<Part> {
    return input.flatMapIndexed { i: Int, line: String -> getParts(i, line) }
}

fun getParts(y: Int, line: String): List<Part> {
    return Regex("[0-9]+").findAll(line).map {
        Part(it.range, y, it.value.toInt())
    }.toList()
}

fun getSymbols(input: List<String>): List<Symbol> {
    return input.flatMapIndexed { i: Int, line: String -> getSymbols(i, line) }
}

fun getSymbols(y: Int, line: String): List<Symbol> {
    return Regex("[^0-9.]").findAll(line).map {
        Symbol(it.range.first, y, it.value.first())
    }.toList()
}

fun isAdjacentTo(part: Part, symbol: Symbol): Boolean {
    return (-1 .. 1).contains(part.y - symbol.y) && (part.x.first - 1 .. part.x.last + 1).contains(symbol.x)
}
