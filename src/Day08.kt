fun main() {

    class Instruction(line: String) {
        val position = line.split(" = ")[0]
        val left = line.split(" = ")[1].drop(1).take(3)
        val right = line.dropLast(1).takeLast(3)
    }

    fun part1(input: List<String>): Int {
        val directions = input[0]
        val map = HashMap<String, Instruction>()
        input.drop(2).forEach { line ->
            val instruction = Instruction(line)
            map[instruction.position] = instruction
        }

        var position = "AAA"
        var i = 0
        while (true) {
            directions.forEach { c ->
                if (position == "ZZZ") {
                    return i
                }
                i++
                position = if (c == 'L') {
                    map[position]?.left ?: error("")
                } else {
                    map[position]?.right ?: error("")
                }
            }
        }
    }

    fun part2(input: List<String>): Long {
        val directions = input[0]
        val map = HashMap<String, Instruction>()
        input.drop(2).forEach { line ->
            val instruction = Instruction(line)
            map[instruction.position] = instruction
        }

        map.keys.filter { it.endsWith("A") }.forEach {start ->
            var current = start
            var i = 0
            val positions = mutableListOf<Pair<Int, String>>()
            while (!positions.contains(Pair(i % directions.length, current))) {
                if (current.endsWith("Z")) {
                    print("$start reaches $current after $i steps and every ")
                }
                positions.add(Pair(i % directions.length, current))
                if (directions[i % directions.length] == 'L') {
                    current = map[current]?.left ?: error("")
                }
                else {
                    current = map[current]?.right ?: error("")
                }
                i++
            }
            val x = positions.size - positions.indexOf(Pair(i % directions.length, current))
            println("$x steps")
        }

        return lcm(16697L, lcm(17263L, lcm(20093L, lcm(13301L, lcm(20659L, 12169L)))))
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
