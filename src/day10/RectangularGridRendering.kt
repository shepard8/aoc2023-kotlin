package day10

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class RectangularGridRendering(
    private val grid: CellRectangularGrid,
    private val distances: DijkstraDistances,
    private val cellSize: Int, private val inset: Int
) {

    private val width = grid.width * cellSize
    private val height = grid.height * cellSize
    val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    private val graphics = img.graphics as Graphics2D

    fun setColor(color: Color) {
        graphics.color = color
    }

    private fun withTempColor(color: Color, f: () -> Unit) {
        val oldColor = graphics.color
        setColor(color)
        f()
        setColor(oldColor)
    }

    fun drawBackground() {
        graphics.fillRect(0, 0, width, height)
    }

    fun drawCells() {
        grid.getPositions().forEach { drawCell(it) }
    }

    fun drawCell(position: CartesianCoordinate) {
        val x = position.x * cellSize
        val y = position.y * cellSize

        val distanceColor = Color(1f, 0f, 0f, 1f * (distances[position]?.takeIf { it > 0 }?.let { 1f } ?: 0f))

        withTempColor(distanceColor) {
            graphics.fillRect(x + inset, y + inset, cellSize - 2 * inset, cellSize - 2 * inset)
        }

        if (grid.linked(position, position.north())) {
            withTempColor(distanceColor) {
                graphics.fillRect(x + inset, y + cellSize - inset, cellSize - 2 * inset, inset)
            }
            graphics.drawLine(x + inset, y + cellSize - inset, x + inset, y + cellSize)
            graphics.drawLine(x + cellSize - inset, y + cellSize - inset, x + cellSize - inset, y + cellSize)
        }
        else {
            graphics.drawLine(x + inset, y + cellSize - inset, x + cellSize - inset, y + cellSize - inset)
        }

        if (grid.linked(position, position.south())) {
            withTempColor(distanceColor) {
                graphics.fillRect(x + inset, y, cellSize - 2 * inset, inset)
            }
            graphics.drawLine(x + inset, y + inset, x + inset, y)
            graphics.drawLine(x + cellSize - inset, y + inset, x + cellSize - inset, y)
        }
        else {
            graphics.drawLine(x + inset, y + inset, x + cellSize - inset, y + inset)
        }

        if (grid.linked(position, position.west())) {
            withTempColor(distanceColor) {
                graphics.fillRect(x, y + inset, inset, cellSize - 2 * inset)
            }
            graphics.drawLine(x, y + inset, x + inset, y + inset)
            graphics.drawLine(x, y + cellSize - inset, x + inset, y + cellSize - inset)
        }
        else {
            graphics.drawLine(x + inset, y + inset, x + inset, y + cellSize - inset)
        }

        if (grid.linked(position, position.east())) {
            withTempColor(distanceColor) {
                graphics.fillRect(x + cellSize - inset, y + inset, inset, cellSize - 2 * inset)
            }
            graphics.drawLine(x + cellSize, y + inset, x + cellSize - inset, y + inset)
            graphics.drawLine(x + cellSize, y + cellSize - inset, x + cellSize - inset, y + cellSize - inset)
        }
        else {
            graphics.drawLine(x + cellSize - inset, y + inset, x + cellSize - inset, y + cellSize - inset)
        }

    }

}