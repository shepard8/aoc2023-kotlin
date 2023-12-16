fun main() {

    fun hash(s: String): Int {
        var currentValue = 0
        s.forEach { c ->
            currentValue += c.code
            currentValue *= 17
            currentValue = currentValue % 256
        }
        return currentValue
    }

    fun part1(input: List<String>): Long {
        val parts = input.joinToString("").split(",")
        return parts.sumOf { hash(it).toLong() }
    }

    data class Lens(val label: String, var focalLength: Int)

    fun focusingPower(indexBox: Int, box: List<Lens>): Long {
        return box.mapIndexed { index, lens -> (indexBox + 1).toLong() * (index + 1) * lens.focalLength }.sum()
    }

    fun part2(input: List<String>): Long {
        val parts = input.joinToString("").split(",")
        val boxes = List(256) { mutableListOf<Lens>() }
        parts.forEach { part ->
            val label = part.substringBefore('=').substringBefore('-')
            val box = hash(label)
            if (part.contains('=')) {
                val focalLength = part.substringAfter('=').toInt()
                val lens = Lens(label, focalLength)
                val existingLens = boxes[box].find { it.label == label }
                if (existingLens == null) {
                    boxes[box].add(lens)
                }
                else {
                    existingLens.focalLength = focalLength
                }
            }
            else {
                boxes[box].removeAll { it.label == label }
            }
        }

        return boxes.mapIndexed { index, box -> focusingPower(index, box) }.sum()
    }

    val input = readInput("Day15")

    part1(input).println()
    part2(input).println()
}

