import day10.*
import java.io.File
import javax.imageio.ImageIO

fun main() {

    fun prepareGrid(input: List<String>): Grid {
        val grid = Grid(140, 140, input)
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val cell = grid.cellAt(x, y)

                val north = grid.cellAt(x, y - 1)
                if (cell.connectsNorth() && north.connectsSouth()) {
                    grid.link(cell, north)
                }

                val south = grid.cellAt(x, y + 1)
                if (cell.connectsSouth() && south.connectsNorth()) {
                    grid.link(cell, south)
                }

                val west = grid.cellAt(x - 1, y)
                if (cell.connectsWest() && west.connectsEast()) {
                    grid.link(cell, west)
                }

                val east = grid.cellAt(x + 1, y)
                if (cell.connectsEast() && east.connectsWest()) {
                    grid.link(cell, east)
                }
            }
        }
        return grid
    }

    fun findStart(input: List<String>, grid: Grid): Cell {
        val y = input.indexOfFirst { it.contains('S') }
        val x = input[y].indexOfFirst { it == 'S' }
        return grid.cellAt(x, y)
    }

    fun part1(input: List<String>): Int {
        val grid = prepareGrid(input)
        val start = findStart(input, grid)
        val distances = DijkstraDistances(grid, start)

        return distances.maxDistance()
    }

    fun part2(input: List<String>): Int {
        val grid = prepareGrid(input)
        val start = findStart(input, grid)
        val distances = DijkstraDistances(grid, start)

        val image = RectangularGridRenderer(20, distances, 2).toPng(grid)
        ImageIO.write(image, "png", File("rendering.png"))

        // Select outer separating black line with magic wand.
        // Fill with red.
        // Select red with magic wand.
        // Fill with black.
        // Count inner white squares (363)

        return 363
    }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
