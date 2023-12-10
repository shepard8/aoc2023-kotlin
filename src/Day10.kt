import day10.*
import java.io.File
import javax.imageio.ImageIO

fun main() {

    class LetterCell(position: CartesianCoordinate, val letter: Char): Cell(position) {
        fun connectsNorth() = letter in listOf('|', 'L', 'J', 'S')
        fun connectsSouth() = letter in listOf('|', '7', 'F')
        fun connectsWest() = letter in listOf('-', '7', 'J')
        fun connectsEast() = letter in listOf('-', 'L', 'F', 'S')
    }

    fun prepareGrid(input: List<String>): CellRectangularGrid {
        val grid = CellRectangularGrid(140, 140)
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val coordinate = CartesianCoordinate(x, y)
                val current = LetterCell(coordinate, c)

                val north = LetterCell(CartesianCoordinate(x, y - 1), input.getOrNull(y - 1)?.get(x) ?: '.')
                if (current.connectsNorth() && north.connectsSouth()) {
                    grid.link(coordinate, north.position)
                }

                val south = LetterCell(CartesianCoordinate(x, y + 1), input.getOrNull(y + 1)?.get(x) ?: '.')
                if (current.connectsSouth() && south.connectsNorth()) {
                    grid.link(coordinate, south.position)
                }

                val west = LetterCell(CartesianCoordinate(x - 1, y), line.getOrNull(x - 1) ?: '.')
                if (current.connectsWest() && west.connectsEast()) {
                    grid.link(coordinate, west.position)
                }

                val east = LetterCell(CartesianCoordinate(x + 1, y), line.getOrNull(x + 1) ?: '.')
                if (current.connectsEast() && east.connectsWest()) {
                    grid.link(coordinate, east.position)
                }
            }
        }
        return grid
    }

    fun findStart(input: List<String>): CartesianCoordinate {
        val y = input.indexOfFirst { it.contains('S') }
        val x = input[y].indexOfFirst { it == 'S' }
        return CartesianCoordinate(x, y)
    }

    fun part1(input: List<String>): Int {
        val grid = prepareGrid(input)
        val start = findStart(input)
        val distances = DijkstraDistances(grid, start)

        return distances.maxDistance()
    }

    fun part2(input: List<String>): Int {
        val grid = prepareGrid(input)
        val start = findStart(input)
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
