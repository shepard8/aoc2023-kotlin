import java.time.LocalDateTime

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

        if (s.startsWith('#')) {
            if (s.getOrNull(l.first()) == '#' || s.take(l.first()).contains('.')) return 0
            return countOrganizations(s.drop(l.first() + 1), l.drop(1))
        }
        if (s.endsWith('#')) {
            if (s.getOrNull(s.length - 1 - l.last()) == '#' || s.takeLast(l.last()).contains('.')) return 0
            return countOrganizations(s.dropLast(l.last() + 1), l.dropLast(1))
        }

        val nextSpring = l.first()
        val asShar = if (s.getOrNull(nextSpring) ?: '.' == '#') 0 else countOrganizations(s.drop(nextSpring + 1), l.drop(1))
        val asDot = countOrganizations(s.drop(1), l)
        return asShar + asDot
    }

    var i = 0

    fun countInGroups(groups: List<String>, org: List<Int>): Long {
        if (org.isEmpty() && groups.all { !it.contains('#') }) return 1
        if (groups.isEmpty() && org.isNotEmpty()) return 0
        var sum = 0L
        var n = 0
        while (org.size >= n && org.take(n).sumOf { it + 1 } - 1 <= groups[0].length) {
            sum += countOrganizations(groups[0], org.take(n)) * countInGroups(groups.drop(1), org.drop(n))
            ++n
        }
        return sum
    }

    fun countUnfoldedPossibilities(line: String): Long {
        val repeats = 1

        val (foldedSprings, foldedOrg) = line.split(" ")
        val springs = (foldedSprings + "?").repeat(repeats).dropLast(1)
        val springGroups = springs.split(Regex("\\.+")).filter { it.isNotEmpty() }
        val org = (foldedOrg + ",").repeat(repeats).dropLast(1).split(",").map { it.toInt() }
//        val cntNew = countOrganizations(springs, org)
        val cntNew = countInGroups(springGroups, org)
        ++i
        println("$i: $springs - $org - $cntNew")
//        val cntOld = countPossibilities(line)
//        if (cntNew != cntOld) {
//            println("$line incorrect:$cntNew correct:$cntOld")
//        }
        return cntNew
    }

    fun part2(input: List<String>): Long {
//        return countUnfoldedPossibilities("????#??#?.#???? 1,1,1,4")
        return input.sumOf { countUnfoldedPossibilities(it) }
    }

    val input = readInput("Day12")

    part1(input).println()
    part2(input).println()
}
