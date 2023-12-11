package day10

open class Grid(val width: Int, val height: Int, input: List<String>) {

    val cells = List(width * height) { Cell(it % width, it / width, input[it / width][it % width]) }

    fun cellAt(x: Int, y: Int): Cell {
        return cells.getOrNull(y * width + x) ?: Cell(-1, -1, '.')
    }

    fun link(cell1: Cell, cell2: Cell) {
        cell1.link(cell2)
        cell2.link(cell1)
    }

}
