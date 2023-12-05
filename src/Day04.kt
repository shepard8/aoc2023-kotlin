fun main() {

    fun part1(input: List<String>): Int {
        val cards = input.map { parseInput(it) }
        return cards.sumOf { card ->
            exp(card.picked.count { card.winning.contains(it) })
        }
    }

    fun part2(input: List<String>): Int {
        val counts = mutableMapOf<Int, Int>()
        input.map { parseInput(it) }.forEach { card ->
            counts[card.number] = counts[card.number] ?: 1
            val copies = counts[card.number]!!
            val cardScore = card.picked.count { card.winning.contains(it) }
            println("Card ${card.number} ($copies copies) scores $cardScore")
            for (i in 1..cardScore) {
                counts[card.number + i] = (counts[card.number + i] ?: 1) + copies
            }
        }
        return counts.values.sum()
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

fun exp(n: Int): Int {
    return if (n == 0) 0 else if (n == 1) 1 else 2 * exp(n - 1)
}

data class Card(val number: Int, val winning: List<Int>, val picked: List<Int>)

fun parseInput(input: String): Card {
    val (game, y) = input.split(Regex(": +"))
    val (winning, picked) = y.split(Regex(" +\\| +"))

    return Card(
        game.substring(4).trim().toInt(),
        winning.split(Regex(" +")).map { it.toInt() },
        picked.split(Regex(" +")).map { it.toInt() }
    )
}