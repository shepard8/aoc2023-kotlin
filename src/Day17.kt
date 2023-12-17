import java.util.PriorityQueue

fun main() {


    class LavaGrid(val input: List<String>) {
        val width = input[0].length
        val height = input.size
        val grid: List<Int> = input.flatMap {it.map {
            it.digitToInt()
        } }
        fun at(x: Int, y: Int): Int {
            return if (x in 0..<width && y in 0..<height)
                grid[y * width + x]
            else 1_000_000_000
        }

        fun contains(x: Int, y: Int): Boolean {
            return x in 0..<width && y in 0..<height
        }
    }

    data class LavaGridPosition(val x: Int, val y: Int, val direction: Direction, val countInDirection: Int) {

        fun nextPositions(grid: LavaGrid): List<LavaGridPosition> {
            return listOf(
                LavaGridPosition(x + 1, y, Direction.Right, if (direction == Direction.Right) countInDirection + 1 else if (direction == Direction.Left) 4 else 1),
                LavaGridPosition(x - 1, y, Direction.Left, if (direction == Direction.Left) countInDirection + 1 else if (direction == Direction.Right) 4 else 1),
                LavaGridPosition(x, y + 1, Direction.Down, if (direction == Direction.Down) countInDirection + 1 else if (direction == Direction.Up) 4 else 1),
                LavaGridPosition(x, y - 1, Direction.Up, if (direction == Direction.Up) countInDirection + 1 else if (direction == Direction.Down) 4 else 1),
            ).filter { it.countInDirection < 4 && grid.contains(it.x, it.y) }
        }
    }


    fun part1(input: List<String>): Long {
        val grid = LavaGrid(input)
        val start = LavaGridPosition(0, 0, Direction.Right, 0)
        val bestCostTo = HashMap<LavaGridPosition, Long>()
        bestCostTo[start] = 0
        val nextPositionsToExplore = PriorityQueue<Pair<LavaGridPosition, Long>>(Comparator.comparing<Pair<LavaGridPosition, Long>?, Long?> { it.second })
        nextPositionsToExplore.add(Pair(start, 0))
        while (nextPositionsToExplore.isNotEmpty()) {
            val (p, cost) = nextPositionsToExplore.poll()
            println("${nextPositionsToExplore.size}, $cost")

            if ((bestCostTo[p] ?: Long.MAX_VALUE) < cost) {
                continue
            }
            bestCostTo[p] = cost
            p.nextPositions(grid).forEach {
                nextPositionsToExplore.offer(Pair(it, grid.at(it.x, it.y) + cost))
            }
        }
//        for (y in 0..<grid.height) {
//            for (x in 0..<grid.width) {
//                print(bestCostTo[Pair(x, y)]!!.cost)
//                print(", ")
//            }
//            println()
//        }
//
//        println()
//        println()
//        var p = bestCostTo[Pair(grid.width - 1, grid.height - 1)]
//        while (p != null) {
//            println(p)
//            p = p.previous
//        }
        return bestCostTo.entries.filter { it.key.x == grid.width - 1 && it.key.y == grid.height - 1 }.map { it.value }.min()
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    val input = readInput("Day17")

    part1(input).println()
    part2(input).println()
}

