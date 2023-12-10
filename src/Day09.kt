fun main() {

    fun sequenceIsZeroes(seq: List<Int>): Boolean {
        return seq.all { it == 0 }
    }

    fun nextSequence(seq: List<Int>): List<Int> {
        return seq.windowed(2, 1).map { it[1] - it[0] }
    }

    fun completeSequence(seq: List<Int>): Int {
        val subsequences = mutableListOf(seq)
        while (!sequenceIsZeroes(subsequences.last())) {
            subsequences.add(nextSequence(subsequences.last()))
        }
        return subsequences.sumOf { it.last() }
    }

    fun part1(input: List<String>): Int {
        return input.map { it.split(" ").map { it.toInt() } }.sumOf { completeSequence(it) }
    }

    fun completeSequenceBackwards(seq: List<Int>): Int {
        val subsequences = mutableListOf(seq)
        while (!sequenceIsZeroes(subsequences.last())) {
            subsequences.add(nextSequence(subsequences.last()))
        }
        return subsequences.mapIndexed { i, v -> (if (i % 2 == 0) 1 else -1) * v.first() }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.map { it.split(" ").map { it.toInt() } }.sumOf { completeSequenceBackwards(it) }
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
