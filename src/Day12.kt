fun main() {

    fun countPossibilities(line: String): Long {
        val (springs, org) = line.split(" ")
        val possibleArrangements = springs.fold(listOf("")) { acc, c -> if (c == '?') acc.flatMap { listOf(it + '.', it + '#') } else acc.map { it + c } }
        return possibleArrangements.count { arr -> arr.split('.').map { it.length }.filter { it > 0 }.joinToString(",") == org }.toLong()
    }

    val memo = HashMap<Pair<String, List<Int>>, Long>()

    fun countOrganizations(s: String, l: List<Int>): Long {
        return memo.getOrPut(Pair(s, l)) {
            if (l.isEmpty()) {
                return if (s.contains('#')) 0 else 1
            }
            if (s.isEmpty()) {
                return 0
            }

            var result = 0L
            if (s.startsWith('.') || s.startsWith('?')) {
                result += countOrganizations(s.drop(1), l)
            }
            if ((s.startsWith('?') || s.startsWith('#')) &&
                l.isNotEmpty() &&
                s.length >= l.first() &&
                !s.take(l.first()).contains('.') &&
                ((s.getOrNull(l.first()) ?: '.') != '#')
            ) {
                result += countOrganizations(s.drop(l.first() + 1), l.drop(1))
            }
            result
        }
//
//            if (l.isEmpty() && !s.contains('#')) {
//                return 1
//            }
//            if (s.isEmpty() || l.isEmpty() || l.sumOf { it + 1 } - 1 > s.length) {
//                return 0
//            }
//
//            if (s.all { it == '?' }) {
//                val availableSpaces = s.length - l.sumOf { it + 1 } + 1
//                return binomial(availableSpaces + l.size, availableSpaces)
//            }
//
//            if (s.startsWith('#')) {
//                if (s.getOrNull(l.first()) == '#') return 0
//                return countOrganizations(s.drop(l.first() + 1), l.drop(1))
//            }
//            if (s.endsWith('#')) {
//                if (s.getOrNull(s.length - 1 - l.last()) == '#') return 0
//                return countOrganizations(s.dropLast(l.last() + 1), l.dropLast(1))
//            }
//
//            if (s.getOrNull(l.first()) == '#') return countOrganizations(s.drop(1), l)
//
//            val asShar = if ((s.getOrNull(l.first()) ?: '.') == '#') 0 else countOrganizations(s.drop(l.first() + 1), l.drop(1))
//            val asDot = countOrganizations(s.drop(1), l)
//            return asShar + asDot
//        }
    }

    var i = 0


    fun countUnfoldedPossibilities(repeats: Int, line: String): Long {
        val (foldedSprings, foldedOrg) = line.split(" ")
        val springs = (foldedSprings + "?").repeat(repeats).dropLast(1)
        val org = (foldedOrg + ",").repeat(repeats).dropLast(1).split(",").map { it.toInt() }
        ++i
        print("$i: $springs - $org - ")
        val cntNew = countOrganizations(springs, org)

        println(cntNew)
//        val cntOld = countPossibilities(line)
//        if (cntNew != cntOld) {
//            println("$line incorrect:$cntNew correct:$cntOld")
//        }
        return cntNew
    }


    fun part1(input: List<String>): Long {
        return input.sumOf { countUnfoldedPossibilities(1, it) }
//        return 7718
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

