fun main() {

    fun countPossibilities(line: String): Long {
        val (springs, org) = line.split(" ")
        val possibleArrangements = springs.fold(listOf("")) { acc, c -> if (c == '?') acc.flatMap { listOf(it + '.', it + '#') } else acc.map { it + c } }
        return possibleArrangements.count { arr -> arr.split('.').map { it.length }.filter { it > 0 }.joinToString(",") == org }.toLong()
    }

    fun part1(input: List<String>): Long {
//        return input.sumOf { countPossibilities(it) }
        return 7718
    }

    fun countOrganizations(s: String, l: List<Int>): Long {
        if (l.isEmpty() && !s.contains('#')) {
            return 1
        }
        if (s.isEmpty() || l.isEmpty() || l.sumOf { it + 1 } - 1 > s.length) {
            return 0
        }

        if (s.all { it == '?' }) {
            val availableSpaces = s.length - l.sumOf { it + 1 } + 1
            return binomial(availableSpaces + l.size, availableSpaces)
        }

        if (s.startsWith('#')) {
            if (s.getOrNull(l.first()) == '#') return 0
            return countOrganizations(s.drop(l.first() + 1), l.drop(1))
        }
        if (s.endsWith('#')) {
            if (s.getOrNull(s.length - 1 - l.last()) == '#') return 0
            return countOrganizations(s.dropLast(l.last() + 1), l.dropLast(1))
        }

        if (s.getOrNull(l.first()) == '#') return countOrganizations(s.drop(1), l)

        val asShar = if ((s.getOrNull(l.first()) ?: '.') == '#') 0 else countOrganizations(s.drop(l.first() + 1), l.drop(1))
        val asDot = countOrganizations(s.drop(1), l)
        return asShar + asDot
    }

    var i = 0

    fun countInGroups(groups: List<String>, org: List<Int>): Long {
        val mandatoryGroup = groups.count { it.contains('#') }
        if (mandatoryGroup > org.count()) return 0
        if (org.isEmpty() && groups.all { !it.contains('#') }) return 1
        if (groups.isEmpty() && org.isNotEmpty()) return 0
        var sum = 0L
        var n = 0
        if (groups.size == 1) {
            return countOrganizations(groups[0], org)
        }
        while (org.size >= n && org.take(n).sumOf { it + 1 } - 1 <= groups[0].length) {
            sum += countOrganizations(groups[0], org.take(n)) * countInGroups(groups.drop(1), org.drop(n))
            ++n
        }
        return sum
    }

    fun countUnfoldedPossibilities(repeats: Int, line: String): Long {
        val (foldedSprings, foldedOrg) = line.split(" ")
        val springs = (foldedSprings + "?").repeat(repeats).dropLast(1)
        val springGroups = springs.split(Regex("\\.+")).filter { it.isNotEmpty() }
        val org = (foldedOrg + ",").repeat(repeats).dropLast(1).split(",").map { it.toInt() }
//        val cntNew = countOrganizations(springs, org)
        ++i
        print("$i: $springs - $org - ")
        val cntNew = countInGroups(springGroups, org)

        println(cntNew)
//        val cntOld = countPossibilities(line)
//        if (cntNew != cntOld) {
//            println("$line incorrect:$cntNew correct:$cntOld")
//        }
        return cntNew
    }

    fun part2(input: List<String>): Long {
        println("158 : " + binomial(45, 35))
        println("429 : " + "?")
        return input.sumOf { countUnfoldedPossibilities(5, it) }
    }

    val input = readInput("Day12")

    part1(input).println()
    part2(input).println()
}

private fun binomial(n: Int, k: Int): Long {
    var k = k
    if (k > n - k) k = n - k
    var b: Long = 1
    var i = 1
    var m = n
    while (i <= k) {
        b = b * m / i
        i++
        m--
    }
    return b
}