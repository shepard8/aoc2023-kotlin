fun main() {

    data class SeedRange(val start: Long, val end: Long) {
        fun splitAt(stops: List<Long>): List<SeedRange> {
            return (stops + start + end).filter { (start..end).contains(it) }.toSortedSet().windowed(2).map { (a, b) -> SeedRange(a, b) }
        }
    }

    fun lengthRange(start: Long, length: Long) = SeedRange(start, start + length)
    fun endRange(start: Long, end: Long) = SeedRange(start, end)

    data class Mapping(val destinationStart: Long, val sourceStart: Long, val length: Long) {
        val shift = destinationStart - sourceStart
        val sourceEnd = sourceStart + length

        fun inRange(source: Long): Boolean = (sourceStart..<sourceEnd).contains(source)
        fun convert(source: Long): Long =
            if (inRange(source))
                source + shift
            else source

        fun splitSeedRange(seeds: SeedRange): List<SeedRange> {
            return if ((seeds.start..<seeds.end).contains(sourceStart) && (seeds.start..<seeds.end).contains(sourceEnd)) {
                listOf(endRange(seeds.start, sourceStart), endRange(sourceStart, sourceEnd), endRange(sourceEnd, seeds.end))
            } else if ((seeds.start..<seeds.end).contains(sourceStart)) {
                listOf(endRange(seeds.start, sourceStart), endRange(sourceStart, seeds.end))
            } else if ((seeds.start..<seeds.end).contains(sourceEnd)) {
                listOf(endRange(seeds.start, sourceEnd), endRange(sourceEnd, seeds.end))
            } else {
                listOf(seeds)
            }
        }

        fun convertRange(seeds: SeedRange): SeedRange {
            return if (inRange(seeds.start)) {
                endRange(seeds.start + shift, seeds.end + shift)
            }
            else seeds
        }
    }

    fun part1(input: List<String>): Long {
        val seeds = input.first().split(": ")[1].split(" ").map { it.toLong() }

        val maps = mutableListOf<MutableList<Mapping>>()
        input.drop(1).filter { it.isNotEmpty() }.forEach {
            if (it[0].isLetter()) {
                maps.add(mutableListOf())
            }
            else {
                val (a, b, c) = it.split(Regex(" "), 3)
                maps.last().add(Mapping(a.toLong(), b.toLong(), c.toLong()))
            }
        }
        return seeds.minOf { seed ->
            maps.fold(seed) { start, mappings ->
                mappings
                    .firstOrNull { mapping -> mapping.inRange(start) }
                    ?.convert(start) ?: start
            }
        }
    }

    fun part2(input: List<String>): Long {
        val ranges = input.first().split(": ")[1].split(" ").chunked(2) { (a, b) -> lengthRange(a.toLong(), b.toLong()) }.toMutableList()
        val maps = mutableListOf<MutableList<Mapping>>()
        input.drop(1).filter { it.isNotEmpty() }.forEach {
            if (it[0].isLetter()) {
                maps.add(mutableListOf())
            }
            else {
                val (a, b, c) = it.split(Regex(" "), 3)
                maps.last().add(Mapping(a.toLong(), b.toLong(), c.toLong()))
            }
        }

        maps.forEach { map ->
            val stops = map.flatMap { listOf(it.sourceStart, it.sourceEnd) }
            println(ranges)
            println(map)
            // Split ranges
            val n = ranges.count()
            repeat(n) {
                val seeds = ranges[0]
                ranges.removeFirst()
                ranges.addAll(seeds.splitAt(stops))
            }
            println(ranges)

            // Apply transformations
            ranges.replaceAll { seeds -> map.firstOrNull { mapping -> mapping.inRange(seeds.start) }?.convertRange(seeds) ?: seeds }
            println(ranges)
            println()
        }

        return ranges.minOf { it.start }
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
