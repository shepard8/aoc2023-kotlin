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

        fun nextPositionsUltra(grid: LavaGrid): List<LavaGridPosition> {
            if (countInDirection < 4) {
                return listOf(
                        if (direction == Direction.Right) LavaGridPosition(x + 1, y, direction, countInDirection + 1)
                        else if (direction == Direction.Left) LavaGridPosition(x - 1, y, direction, countInDirection + 1)
                        else if (direction == Direction.Down) LavaGridPosition(x, y + 1, direction, countInDirection + 1)
                        else if (direction == Direction.Up) LavaGridPosition(x, y - 1, direction, countInDirection + 1)
                        else error("")
                ).filter { it.x != grid.width - 1 || it.y != grid.height - 1 || it.countInDirection > 3 }
            }
            return listOf(
                LavaGridPosition(x + 1, y, Direction.Right, if (direction == Direction.Right) countInDirection + 1 else if (direction == Direction.Left) 11 else 1),
                LavaGridPosition(x - 1, y, Direction.Left, if (direction == Direction.Left) countInDirection + 1 else if (direction == Direction.Right) 11 else 1),
                LavaGridPosition(x, y + 1, Direction.Down, if (direction == Direction.Down) countInDirection + 1 else if (direction == Direction.Up) 11 else 1),
                LavaGridPosition(x, y - 1, Direction.Up, if (direction == Direction.Up) countInDirection + 1 else if (direction == Direction.Down) 11 else 1),
            ).filter { it.countInDirection < 11 && grid.contains(it.x, it.y) }.filter { it.x != grid.width - 1 || it.y != grid.height - 1 || it.countInDirection > 3 }
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

            if ((bestCostTo[p] ?: Long.MAX_VALUE) < cost) {
                continue
            }
            bestCostTo[p] = cost
            p.nextPositions(grid).forEach { p2 ->
                val p2cost = grid.at(p2.x, p2.y) + cost
                val p3 = nextPositionsToExplore.find { it.first == p2 }
                if (p3 != null && p3.second > p2cost) {
                    nextPositionsToExplore.remove(p3)
                    nextPositionsToExplore.offer(Pair(p2, p2cost))
                }
                else if (p3 != null) {

                }
                else {
                    nextPositionsToExplore.offer(Pair(p2, p2cost))
                }
            }
        }
        return bestCostTo.entries.filter { it.key.x == grid.width - 1 && it.key.y == grid.height - 1 }.map { it.value }.min()
    }

    fun part2(input: List<String>): Long {
        val grid = LavaGrid(input)
        val start = LavaGridPosition(0, 0, Direction.Left, 5)
        val bestCostTo = HashMap<LavaGridPosition, Long>()
        bestCostTo[start] = 0
        val nextPositionsToExplore = PriorityQueue<Pair<LavaGridPosition, Long>>(Comparator.comparing<Pair<LavaGridPosition, Long>?, Long?> { it.second })
        nextPositionsToExplore.add(Pair(start, 0))
        while (nextPositionsToExplore.isNotEmpty()) {
            val (p, cost) = nextPositionsToExplore.poll()

            if ((bestCostTo[p] ?: Long.MAX_VALUE) < cost) {
                continue
            }
            bestCostTo[p] = cost
            p.nextPositionsUltra(grid).forEach { p2 ->
                val p2cost = grid.at(p2.x, p2.y) + cost
                val p3 = nextPositionsToExplore.find { it.first == p2 }
                if (p3 != null && p3.second > p2cost) {
                    nextPositionsToExplore.remove(p3)
                    nextPositionsToExplore.offer(Pair(p2, p2cost))
                }
                else if (p3 != null) {

                }
                else {
                    nextPositionsToExplore.offer(Pair(p2, p2cost))
                }
            }
        }
        return bestCostTo.entries.filter { it.key.x == grid.width - 1 && it.key.y == grid.height - 1 }.map { it.value }.min()

    }

    val input = readInput("Day17")

    part1(input).println()
    part2(input).println()
}

