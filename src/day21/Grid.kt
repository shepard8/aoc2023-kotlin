package day21

open class Grid(val width: Int, val height: Int, input: List<String>) {

    val outerCell = Cell(this, -1, -1, '#')

    val cells = List(width * height) { Cell(this, it % width, it / width, input[it / width][it % width]) }

    fun cellAt(x: Int, y: Int): Cell {
        return (if (hasCellAt(x, y)) cells[y * width + x] else null) ?: outerCell
    }

    private fun hasCellAt(x: Int, y : Int): Boolean {
        return x in 0..<width && y in 0..<height
    }

}
