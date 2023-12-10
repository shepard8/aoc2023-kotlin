package day10

data class CartesianCoordinate(val x: Int, val y: Int) {

    fun neighbours() = listOf(south(), north(), west(), east())

    fun south() = CartesianCoordinate(x, y - 1)
    fun north() = CartesianCoordinate(x, y + 1)
    fun west() = CartesianCoordinate(x - 1, y)
    fun east() = CartesianCoordinate(x + 1, y)

}
