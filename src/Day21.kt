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
        error("")
    }

    val maxSteps = 26501365

    fun count(grid: Grid, startX: Int, startY: Int, initDistance: Int): Long {
        val distances = DijkstraDistances(grid, grid.cellAt(startX, startY))
        return distances.values.count { it + initDistance <= maxSteps && (it + initDistance) % 2 == 1 }.toLong()
    }

    fun part2(input: List<String>): Long {
        val grid = Grid(131, 131, input)

        val perpendicularLast = 202300
        val minDistancePerpendicular = minDistanceFor(0, perpendicularLast)
        println("minDistancePerpendicular $minDistancePerpendicular")
        println()

        var total = 0L

        // Top partially covered
        val topCount = count(grid, 65, 130, minDistancePerpendicular)
        println("topCount $topCount")
        total += topCount

        // Right partially covered
        val rightCount = count(grid, 0, 65, minDistancePerpendicular)
        println("rightCount $rightCount")
        total += rightCount

        // Bottom partially covered
        val bottomCount = count(grid, 65, 0, minDistancePerpendicular)
        println("bottomCount $bottomCount")
        total += bottomCount

        // Left partially covered
        val leftCount = count(grid, 130, 65, minDistancePerpendicular)
        println("leftCount $leftCount")
        total += leftCount

        println()

        // Inside cells
        val congruentStartCount = count(grid, 65, 65, 0)
        println("evenCount $congruentStartCount")
        val nonCongruentStartCount = count(grid, 65, 65, 1)
        println("oddCount $nonCongruentStartCount")
        total += congruentStartCount // Center tile
        total += (1..<perpendicularLast).sumOf { d ->
            (if (d % 2 == 0) congruentStartCount else nonCongruentStartCount) * 4 * d
        }

        println()

        // TopRight border
        total += perpendicularLast * count(grid, 0, 130, minDistancePerpendicular + 66).also { println("topRightSmall $it") }
        total += (perpendicularLast - 1) * count(grid, 0, 130, minDistancePerpendicular - 65).also { println("topRightBig $it") }

        // BottomRight border
        total += perpendicularLast * count(grid, 0, 0, minDistancePerpendicular + 66).also { println("bottomRightSmall $it") }
        total += (perpendicularLast - 1) * count(grid, 0, 0, minDistancePerpendicular - 65).also { println("bottomRightBig $it") }

        // BottomLeft border
        total += perpendicularLast * count(grid, 130, 0, minDistancePerpendicular + 66).also { println("bottomLeftSmall $it") }
        total += (perpendicularLast - 1) * count(grid, 130, 0, minDistancePerpendicular - 65).also { println("bottomLeftBig $it") }

        // TopLeft border
        total += perpendicularLast * count(grid, 130, 130, minDistancePerpendicular + 66).also { println("topLeftSmall $it") }
        total += (perpendicularLast - 1) * count(grid, 130, 130, minDistancePerpendicular - 65).also { println("topLeftBig $it") }

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
// 54 662 931 037 465 incorrect
// 54 662 906 356 614 incorrect
// 54 662 881 676 014
// 54 662 881 675 953
// 54 662 939 736 139
//618 261 433 219 147