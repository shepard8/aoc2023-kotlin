enum class Direction { Up, Right, Left, Down }
enum class BeamGridContent { Empty, TopLeftMirror, TopRightMirror, VerticalSplitter, HorizontalSplitter, Outside }

fun main() {

    class BeamGrid(val input: List<String>) {
        val width = input[0].length
        val height = input.size
        val grid: List<BeamGridContent> = input.flatMap {it.map {
            if (it == '.') BeamGridContent.Empty
            else if (it == '\\') BeamGridContent.TopLeftMirror
            else if (it == '/') BeamGridContent.TopRightMirror
            else if (it == '|') BeamGridContent.VerticalSplitter
            else BeamGridContent.HorizontalSplitter
        } }
        fun at(x: Int, y: Int): BeamGridContent {
            return if (x in 0..<width && y in 0..<height)
                grid[y * width + x]
            else BeamGridContent.Outside
        }
    }

    data class BeamPosition(val x: Int, val y: Int, val direction: Direction) {

        fun nextPositions(grid: BeamGrid): List<BeamPosition> {
            return (
                    if (grid.at(x, y) == BeamGridContent.Empty) {
                        when (direction) {
                            Direction.Right -> listOf(BeamPosition(x + 1, y, direction))
                            Direction.Up -> listOf(BeamPosition(x, y - 1, direction))
                            Direction.Left -> listOf(BeamPosition(x - 1, y, direction))
                            Direction.Down -> listOf(BeamPosition(x, y + 1, direction))
                        }
                    }
                    else if (grid.at(x, y) == BeamGridContent.HorizontalSplitter) {
                        when (direction) {
                            Direction.Up -> listOf(BeamPosition(x - 1, y, Direction.Left), BeamPosition(x + 1, y, Direction.Right))
                            Direction.Down -> listOf(BeamPosition(x - 1, y, Direction.Left), BeamPosition(x + 1, y, Direction.Right))
                            Direction.Left -> listOf(BeamPosition(x - 1, y, direction))
                            Direction.Right -> listOf(BeamPosition(x + 1, y, direction))
                        }
                    }
                    else if (grid.at(x, y) == BeamGridContent.VerticalSplitter) {
                        when (direction) {
                            Direction.Up -> listOf(BeamPosition(x, y - 1, direction))
                            Direction.Down -> listOf(BeamPosition(x, y + 1, direction))
                            Direction.Left -> listOf(BeamPosition(x, y - 1, Direction.Up), BeamPosition(x, y + 1, Direction.Down))
                            Direction.Right -> listOf(BeamPosition(x, y - 1, Direction.Up), BeamPosition(x, y + 1, Direction.Down))
                        }
                    }
                    else if (grid.at(x, y) == BeamGridContent.TopLeftMirror) {
                        when (direction) {
                            Direction.Up -> listOf(BeamPosition(x - 1, y, Direction.Left))
                            Direction.Down -> listOf(BeamPosition(x + 1, y, Direction.Right))
                            Direction.Left -> listOf(BeamPosition(x, y - 1, Direction.Up))
                            Direction.Right -> listOf(BeamPosition(x, y + 1, Direction.Down))
                        }
                    }
                    else if (grid.at(x, y) == BeamGridContent.TopRightMirror) {
                        when (direction) {
                            Direction.Up -> listOf(BeamPosition(x + 1, y, Direction.Right))
                            Direction.Down -> listOf(BeamPosition(x - 1, y, Direction.Left))
                            Direction.Left -> listOf(BeamPosition(x, y + 1, Direction.Down))
                            Direction.Right -> listOf(BeamPosition(x, y - 1, Direction.Up))
                        }
                    }
                    else
                        error("")
                    ).filter { grid.at(it.x, it.y) != BeamGridContent.Outside}
        }
    }

    fun part1(input: List<String>): Long {
        val grid = BeamGrid(input)
        val positionsVisited = HashSet<BeamPosition>()
        val positionsToAdd = HashSet<BeamPosition>()
        positionsToAdd.add(BeamPosition(0, 0, Direction.Right))
        while (positionsToAdd.isNotEmpty()) {
            val positionToAdd = positionsToAdd.first()
            positionsToAdd.remove(positionToAdd)
            if (positionsVisited.contains(positionToAdd)) {
                continue
            }
            positionsVisited.add(positionToAdd)
            positionsToAdd.addAll(positionToAdd.nextPositions(grid))
        }
        return positionsVisited.map { Pair(it.x, it.y) }.toSet().count().toLong()
    }

    fun part2(input: List<String>): Long {
        val grid = BeamGrid(input)
        val initialPositions = mutableListOf<BeamPosition>()
        initialPositions.addAll((0..<grid.width).map { BeamPosition(it, 0, Direction.Down) })
        initialPositions.addAll((0..<grid.width).map { BeamPosition(it, grid.height - 1, Direction.Up) })
        initialPositions.addAll((0..<grid.height).map { BeamPosition(0, it, Direction.Right) })
        initialPositions.addAll((0..<grid.height).map { BeamPosition(grid.width - 1, it, Direction.Left) })
        return initialPositions.maxOf { initialPosition ->
            val positionsVisited = HashSet<BeamPosition>()
            val positionsToAdd = HashSet<BeamPosition>()
            positionsToAdd.add(initialPosition)
            while (positionsToAdd.isNotEmpty()) {
                val positionToAdd = positionsToAdd.first()
                positionsToAdd.remove(positionToAdd)
                if (positionsVisited.contains(positionToAdd)) {
                    continue
                }
                positionsVisited.add(positionToAdd)
                positionsToAdd.addAll(positionToAdd.nextPositions(grid))
            }
            positionsVisited.map { Pair(it.x, it.y) }.toSet().count().toLong()
        }

    }

    val input = readInput("Day16")

    part1(input).println()
    part2(input).println()
}

