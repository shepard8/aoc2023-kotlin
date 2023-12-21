package day21

open class Cell(val grid: Grid, val x: Int, val y: Int, letter: Char) {

    val type = if (letter == '#') "rock" else "ground"

    fun neighbours() = listOf(grid.cellAt(x + 1, y), grid.cellAt(x - 1, y), grid.cellAt(x, y - 1), grid.cellAt(x, y + 1)).filter { it.type == "ground" }

}
