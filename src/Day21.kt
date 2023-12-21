import day21.Grid
import day21.DijkstraDistances
import kotlin.math.abs


fun main() {

    fun part1(input: List<String>): Long {
        val grid = Grid(input[0].length, input.size, input)
        val startY = input.indexOfFirst { it.contains('S') }
        val startX = input[startY].indexOfFirst { it == 'S' }
        val distances = DijkstraDistances(grid, grid.cellAt(startX, startY))
        return distances.values.count { it % 2 == 0 && it <= 64 }.toLong()
    }

    fun minDistanceFor(tileX: Int, tileY: Int): Int {
        if (tileX == 0 && tileY == 0) {
            return 0
        }
        if (tileX == 0 || tileY == 0) {
            return (131 * abs(tileX + tileY) - 65)
        }
        return (131 * abs(tileX) - 65 + 131 * abs(tileY) - 65)
    }

    fun maxDistanceFor(tileX: Int, tileY: Int): Int {
        if (tileX == 0 && tileY == 0) {
            return 65 + 65
        }
        if (tileX == 0 || tileY == 0) {
            return minDistanceFor(abs(tileX + tileY) + 1, 0) - 1 + 65
        }
        return minDistanceFor(abs(tileX) + 1, abs(tileY) + 1) - 1
    }

    val maxSteps = 26501365

    fun count(grid: Grid, startX: Int, startY: Int, initDistance: Int, even: Int): Int {
        val distances = DijkstraDistances(grid, grid.cellAt(startX, startY))
        return distances.values.count { it + initDistance <= maxSteps && (it + initDistance) % 2 == even }
    }

    fun part2(input: List<String>): Long {
        val grid = Grid(131, 131, input)

        val perpendicularLast = 202300
        val minDistancePerpendicular = minDistanceFor(0, perpendicularLast)
        println(minDistancePerpendicular)
        println(maxDistanceFor(0, perpendicularLast))
        println()

        var total = 0L

        // Top partially covered
        val topCount = count(grid, 65, 130, minDistancePerpendicular, 1)
        println("topCount $topCount")
        total += topCount

        // Right partially covered
        val rightCount = count(grid, 0, 65, minDistancePerpendicular, 1)
        println("rightCount $rightCount")
        total += rightCount

        // Bottom partially covered
        val bottomCount = count(grid, 65, 0, minDistancePerpendicular, 1)
        println("bottomCount $bottomCount")
        total += bottomCount

        // Left partially covered
        val leftCount = count(grid, 130, 65, minDistancePerpendicular, 1)
        println("leftCount $leftCount")
        total += leftCount

        // Inside cells
        val evenCount = count(grid, 0, 0, 0, 0)
        println("evenCount $evenCount")
        val oddCount = count(grid, 0, 0, 0, 1)
        println("oddCount $oddCount")
        total += evenCount // Center tile
        (1..<perpendicularLast).forEach { d ->
            if (d % 2 == 0)
                total += evenCount * (4 * d - 4) + 4 * oddCount
            else
                total += oddCount * (4 * d - 4) + 4 * evenCount
        }

        // TopRight border
        total += perpendicularLast * count(grid, 0, 130, minDistancePerpendicular + 66, 1).also { println("topRightSmall $it") }
        total += (perpendicularLast - 1) * count(grid, 0, 130, minDistancePerpendicular - 65, 0).also { println("topRightBig $it") }

        // BottomRight border
        total += perpendicularLast * count(grid, 0, 0, minDistancePerpendicular + 66, 1).also { println("bottomRightSmall $it") }
        total += (perpendicularLast - 1) * count(grid, 0, 0, minDistancePerpendicular - 65, 0).also { println("bottomRightBig $it") }

        // BottomLeft border
        total += perpendicularLast * count(grid, 130, 0, minDistancePerpendicular + 66, 1).also { println("bottomLeftSmall $it") }
        total += (perpendicularLast - 1) * count(grid, 130, 0, minDistancePerpendicular - 65, 0).also { println("bottomLeftBig $it") }

        // TopLeft border
        total += perpendicularLast * count(grid, 130, 130, minDistancePerpendicular + 66, 1).also { println("topLeftSmall $it") }
        total += (perpendicularLast - 1) * count(grid, 130, 130, minDistancePerpendicular - 65, 0).also { println("topLeftBig $it") }

        return total
    }

    val input = readInput("Day21")

    part1(input).println()
    part2(input).println()
}

// 54 660 394 887 012 too low
// 1 291 169 954 397 818 too high
// 1 236 513 971 187 726 too high
// 54 662 951 064 921 incorrect
// 54 662 926 384 565 incorrect
// 54 662 897 657 965 incorrect