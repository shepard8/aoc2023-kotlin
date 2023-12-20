data class XmasPart(val x: Int, val m: Int, val a: Int, val s: Int)

data class XmasRange(val x1: Long, val x2: Long, val m1: Long, val m2: Long, val a1: Long, val a2: Long, val s1: Long, val s2: Long) {
    companion object {
        val empty = XmasRange(0, 0, 0, 0, 0, 0, 0, 0)
    }
    fun rangeCount(): Long = (x2 - x1 + 1) * (m2 - m1 + 1) * (a2 - a1 + 1) * (s2 - s1 + 1)

    fun splitRange(letter: Char, v: Long): List<XmasRange> {
        // Range1 is lower or equal to v, range2 is greater than v
        return when (letter) {
            'x' -> if (v < x1) listOf(empty, this) else if (v > x2) listOf(this, empty) else listOf(XmasRange(x1, v, m1, m2, a1, a2, s1, s2), XmasRange(v + 1, x2, m1, m2, a1, a2, s1, s2))
            'm' -> if (v < m1) listOf(empty, this) else if (v > m2) listOf(this, empty) else listOf(XmasRange(x1, x2, m1, v, a1, a2, s1, s2), XmasRange(x1, x2, v + 1, m2, a1, a2, s1, s2))
            'a' -> if (v < a1) listOf(empty, this) else if (v > a2) listOf(this, empty) else listOf(XmasRange(x1, x2, m1, m2, a1, v, s1, s2), XmasRange(x1, x2, m1, m2, v + 1, a2, s1, s2))
            's' -> if (v < s1) listOf(empty, this) else if (v > s2) listOf(this, empty) else listOf(XmasRange(x1, x2, m1, m2, a1, a2, s1, v), XmasRange(x1, x2, m1, m2, a1, a2, v + 1, s2))
            else -> error("")
        }
    }
}

class XmasWorkflow(input: String) {
    val label = input.split("{")[0]
    val conditionString = input.split("{")[1].split("}")[0]

    companion object {
        val workflows = mutableMapOf<String, XmasWorkflow>()
    }

    init {
        workflows[label] = this
    }

    fun apply(part: XmasPart): Boolean {
        for (spec in conditionString.split(",")) {
            val conditionMet = !spec.contains(":") || satisfies(part, spec.split(":")[0])
            val actionString = if (!spec.contains(":")) spec else spec.split(":")[1]
            if (conditionMet && actionString == "A")
                return true
            else if (conditionMet && actionString == "R")
                return false
            else if (conditionMet)
                return workflows[actionString]?.apply(part) ?: error("")
            else
                continue
        }
        error("")
    }

    private fun satisfies(part: XmasPart, cond: String): Boolean {
        val greater = cond.contains(">")
        val (partName, value) = cond.split(Regex("[<>]"))
        val partValue = when (partName) {
            "x" -> part.x
            "m" -> part.m
            "a" -> part.a
            "s" -> part.s
            else -> error("")
        }
        return greater && partValue > value.toInt() || !greater && partValue < value.toInt()
    }

    fun apply(range: XmasRange): List<XmasRange> {
        val acceptedRanges = mutableListOf<XmasRange>()
        var currentRange = range
        for (spec in conditionString.split(",")) {
            val (conditionMetRange, conditionNotMetRange) = if (!spec.contains(":")) {
                listOf(currentRange, XmasRange.empty)
            }
            else if (spec.contains("<")) {
                val (letter, cut) = spec.substringBefore(":").split("<")
                currentRange.splitRange(letter.first(), cut.toLong() - 1)
            }
            else {
                val (letter, cut) = spec.substringBefore(":").split(">")
                currentRange.splitRange(letter.first(), cut.toLong()).reversed()
            }
            println(spec)
            println(currentRange)
            println(conditionMetRange)
            println(conditionNotMetRange)

            currentRange = conditionNotMetRange
            val actionString = if (!spec.contains(":")) spec else spec.split(":")[1]
            if (actionString == "A")
                acceptedRanges.add(conditionMetRange)
            else if (actionString == "R")
                continue
            else
                acceptedRanges.addAll(workflows[actionString]?.apply(conditionMetRange) ?: error(""))
        }
        return acceptedRanges
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        input.filter { it.firstOrNull()?.isLetter() ?: false }.forEach { XmasWorkflow(it) }
        val parts = input.filter { it.startsWith("{") }.map {
            val (x, m, a, s) = it.split(Regex("[a-z=,{}]+")).filter { it.isNotEmpty() }.map { it.toInt() }
            XmasPart(x, m, a, s)
        }
        return parts.filter { XmasWorkflow.workflows["in"]?.apply(it) ?: error("") }.also { println(it) }.sumOf { it.x + it.m + it.a + it.s }
    }

    fun part2(input: List<String>): Long {
        val range = XmasRange(1, 4000, 1, 4000, 1, 4000, 1, 4000)
        return XmasWorkflow.workflows["in"]?.apply(range)?.also { println(it) }?.sumOf { it.rangeCount() } ?: error("")
    }

    val input = readInput("Day19")

    part1(input).println()
    part2(input).println()
}

// 1453717354941971 too high


// 505781950197174
// 167409079868000