package day10

open class Cell(val position: CartesianCoordinate) {

    private val links = mutableListOf<Cell>()

    fun link(cell: Cell) = links.add(cell)

    fun isLinkedTo(position: CartesianCoordinate): Boolean = links.any { it.position == position }

}
