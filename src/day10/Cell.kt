package day10

open class Cell(val x: Int, val y: Int, private val letter: Char) {

    fun connectsNorth() = letter in listOf('|', 'L', 'J', 'S')
    fun connectsSouth() = letter in listOf('|', '7', 'F')
    fun connectsWest() = letter in listOf('-', '7', 'J')
    fun connectsEast() = letter in listOf('-', 'L', 'F', 'S')

    private val links = mutableListOf<Cell>()

    fun link(cell: Cell) = links.add(cell)

    fun isLinkedTo(cell: Cell): Boolean = links.any { it.x == cell.x && it.y == cell.y }

}
