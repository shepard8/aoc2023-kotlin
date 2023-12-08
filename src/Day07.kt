fun main() {

    fun makeHand(hand: String): Map<Char, Int> {
        val ret = HashMap<Char, Int>()
        for (c in hand) {
            ret[c] = hand.count { it == c }
        }
        return ret
    }

    open class Hand(val handString: String, val bid: Int): Comparable<Hand> {

        val hand = makeHand(handString)

        open fun handTypeScore(): Int {
            if (hand.any { it.value == 5 }) return 10
            if (hand.any { it.value == 4 }) return 9
            if (hand.any { it.value == 3 } && hand.any { it.value == 2 }) return 8
            if (hand.any { it.value == 3 }) return 7
            if (hand.count { it.value == 2 } == 2) return 6
            if (hand.any { it.value == 2 }) return 5
            return 4
        }

        open fun value(c: Char): Int {
            return when(c) {
                'A' -> 13
                'K' -> 12
                'Q' -> 11
                'J' -> 10
                'T' -> 9
                else -> c.digitToInt() - 1
            }
        }

        override fun compareTo(other: Hand): Int {
            if (handTypeScore() > other.handTypeScore()) return 1
            if (handTypeScore() < other.handTypeScore()) return -1
            repeat(5) {
                if (value(handString[it]) > value(other.handString[it])) return 1
                if (value(handString[it]) < value(other.handString[it])) return -1
            }
            return 0
        }
    }

    fun part1(input: List<String>): Int {
        return input.map { Hand(it.substringBefore(" "), it.substringAfter(" ").toInt()) }.sorted().mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
    }

    class Hand2(handString: String, bid: Int): Hand(handString, bid) {

        override fun handTypeScore(): Int {
            val jCount = hand['J'] ?: 0
            if (jCount == 5) return 10

            val hand2 = HashMap(hand)
            hand2['J'] = 0
            val max = hand2.values.max()
            val key = hand2.entries.first { it.value == max }.key
            hand2[key] = hand2[key]!! + jCount

            if (hand2.any { it.value == 5 }) return 10
            if (hand2.any { it.value == 4 }) return 9
            if (hand2.any { it.value == 3 } && hand2.any { it.value == 2 }) return 8
            if (hand2.any { it.value == 3 }) return 7
            if (hand2.count { it.value == 2 } == 2) return 6
            if (hand2.any { it.value == 2 }) return 5
            return 4
        }

        override fun value(c: Char): Int {
            return if (c == 'J') 0 else super.value(c)
        }

    }

    fun part2(input: List<String>): Int {
        return input.map { Hand2(it.substringBefore(" "), it.substringAfter(" ").toInt()) }.sorted().mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
    }

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
