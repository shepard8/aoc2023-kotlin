package day10

import java.lang.IllegalArgumentException

open class CellRectangularGrid(val width: Int, val height: Int) {

    private val cells: Map<CartesianCoordinate, Cell> = List(width * height) { Cell(CartesianCoordinate(it % width, it / width)) }.associateBy { it.position }

    fun getPositions() = cells.keys

    fun link(position1: CartesianCoordinate, position2: CartesianCoordinate) {
        val cell1 = cells[position1] ?: throw IllegalArgumentException("No cell at position $position1.")
        val cell2 = cells[position2] ?: throw IllegalArgumentException("No cell at position $position2.")
        cell1.link(cell2)
        cell2.link(cell1)
    }

    fun linked(position1: CartesianCoordinate, position2: CartesianCoordinate): Boolean {
        return cells[position1]?.isLinkedTo(position2) ?: false
    }

}
