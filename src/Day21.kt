import day21.Grid
import day21.DijkstraDistances
import kotlin.math.abs
import kotlin.system.exitProcess


fun main() {

    fun part1(input: List<String>): Long {
        val grid = Grid(input[0].length, input.size, input)
        val startY = input.indexOfFirst { it.contains('S') }
        val startX = input[startY].indexOfFirst { it == 'S' }
        val distances = DijkstraDistances(grid, grid.cellAt(startX, startY))
        return distances.values.count { it % 2 == 0 && it <= 64 }.toLong()
    }

    data class Tile(val tileX: Int, val tileY: Int, val startX: Int, val startY: Int)

    fun minDistanceFor(tileX: Int, tileY: Int): Long {
        if (tileX == 0 && tileY == 0) {
            return 0
        }
        if (tileX == 0 || tileY == 0) {
            return (131 * abs(tileX + tileY) - 65).toLong()
        }
        return (131 * abs(tileX) - 65 + 131 * abs(tileY) - 65).toLong()
    }

    fun maxDistanceFor(tileX: Int, tileY: Int): Long {
        if (tileX == 0 && tileY == 0) {
            return 65 + 65
        }
        if (tileX == 0 || tileY == 0) {
            return minDistanceFor(abs(tileX + tileY) + 1, 0) - 1 + 65
        }
        return minDistanceFor(abs(tileX) + 1, abs(tileY) + 1) - 1
    }

    fun part2(input: List<String>): Long {
        // Each first/last row/column is empty
        // The row/column of starting point is empty
        // The total distance from a corner to its opposite corner is 260

        // The min distance for "tile" (0, 0) (the start) is 0
        // The min distance for "tile" (i, 0) or (0, i) (horizontal/vertical from the start) is i * 130 - 65
        // The min distance for "tile" (i, j) is (i + j) * 130 - 130

        println("" + minDistanceFor(0, 0) + " - " + maxDistanceFor(0, 0))
        println("" + minDistanceFor(1, 0) + " - " + maxDistanceFor(1, 0))
        println("" + minDistanceFor(2, 0) + " - " + maxDistanceFor(2, 0))
        println("" + minDistanceFor(3, 0) + " - " + maxDistanceFor(3, 0))
        println("" + minDistanceFor(4, 0) + " - " + maxDistanceFor(4, 0))

        println()
        println("" + minDistanceFor(0, 1) + " - " + maxDistanceFor(0, 1))
        println("" + minDistanceFor(0, 2) + " - " + maxDistanceFor(0, 2))
        println("" + minDistanceFor(0, 3) + " - " + maxDistanceFor(0, 3))
        println("" + minDistanceFor(0, 4) + " - " + maxDistanceFor(0, 4))

        println()
        println("" + minDistanceFor(1, 1) + " - " + maxDistanceFor(1, 1))
        println("" + minDistanceFor(2, 2) + " - " + maxDistanceFor(2, 2))
        println("" + minDistanceFor(3, 3) + " - " + maxDistanceFor(3, 3))
        println("" + minDistanceFor(4, 4) + " - " + maxDistanceFor(4, 4))

        println()
        println("" + minDistanceFor(1, 1) + " - " + maxDistanceFor(1, 1))
        println("" + minDistanceFor(1, 2) + " - " + maxDistanceFor(1, 2))
        println("" + minDistanceFor(1, 3) + " - " + maxDistanceFor(1, 3))
        println("" + minDistanceFor(1, 4) + " - " + maxDistanceFor(1, 4))

        val maxSteps = 26501365
        var perpendicularCoveredCount = 0
        while (minDistanceFor(perpendicularCoveredCount, 0) < maxSteps) {
            perpendicularCoveredCount++
        }
        perpendicularCoveredCount--
        println(perpendicularCoveredCount)
        println("" + minDistanceFor(perpendicularCoveredCount, 0) + " - " + maxDistanceFor(perpendicularCoveredCount, 0))

        println()
        println()
        println()
        println()
        println()
        println()

        val grid = Grid(131, 131, input)

        var total = 0L

        // Center (covered)
        val distancesCenter = DijkstraDistances(grid, grid.cellAt(65, 65))
        total += distancesCenter.values.count { it % 2 == 1 }

        // Perpendicular covered
        val tileCount = distancesCenter.count()
        total += 4 * 101_150 * tileCount

        // Top Right quadrant
        var currentTileY = 202301
        val distancesTopRight = DijkstraDistances(grid, grid.cellAt(0, 130))
        val distancesBottomRight = DijkstraDistances(grid, grid.cellAt(0, 0))
        val distancesBottomLeft = DijkstraDistances(grid, grid.cellAt(130, 0))
        val distancesTopLeft = DijkstraDistances(grid, grid.cellAt(130, 130))

        val evenTopRightCount = distancesTopRight.values.count { it % 2 == 0 }
        val oddTopRightCount = distancesTopRight.values.count { it % 2 == 1 }
        println(evenTopRightCount)
        println(oddTopRightCount)
        val startDistance = minDistanceFor(1, 202300)

        // Top right quadrant
        total += (202300L / 2) * (202300L / 2) * evenTopRightCount
        total += ((202300L / 2) * (202300L / 2) - (202300L / 2)) * oddTopRightCount
//        total += (4 * (202300L * 202299) / 2) * tileCount

        println(startDistance)

        total += (2 * 202301 - 3) * distancesTopRight.values.count { it + startDistance <= maxSteps && (it + startDistance) % 2 == 1L }
        total += (2 * 202301 - 3) * distancesBottomRight.values.count { it + startDistance <= maxSteps && (it + startDistance) % 2 == 1L }
        total += (2 * 202301 - 3) * distancesBottomLeft.values.count { it + startDistance <= maxSteps && (it + startDistance) % 2 == 1L }
        total += (2 * 202301 - 3) * distancesTopLeft.values.count { it + startDistance <= maxSteps && (it + startDistance) % 2 == 1L }

        val startDistancePerpendicular = minDistanceFor(0, 202300)

        // Top partially covered
        val distancesTop = DijkstraDistances(grid, grid.cellAt(65, 130))
        total += distancesTop.values.count { it + startDistancePerpendicular <= maxSteps && (it + startDistancePerpendicular) % 2 == 1L }

        // Right partially covered
        val distancesRight = DijkstraDistances(grid, grid.cellAt(0, 65))
        total += distancesRight.values.count { it + startDistancePerpendicular <= maxSteps && (it + startDistancePerpendicular) % 2 == 1L }

        // Bottom partially covered
        val distancesBottom = DijkstraDistances(grid, grid.cellAt(65, 0))
        total += distancesBottom.values.count { it + startDistancePerpendicular <= maxSteps && (it + startDistancePerpendicular) % 2 == 1L }

        // Left partially covered
        val distancesLeft = DijkstraDistances(grid, grid.cellAt(130, 65))
        total += distancesLeft.values.count { it + startDistancePerpendicular <= maxSteps && (it + startDistancePerpendicular) % 2 == 1L }

        return total
    }

    val input = readInput("Day21")

    part1(input).println()
    part2(input).println()
}

// 54 660 394 887 012 too low
// 1 291 169 954 397 818 too high
// 1 236 513 971 187 726 too high