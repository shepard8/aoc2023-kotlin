package day10

import java.awt.Color
import java.awt.image.BufferedImage

class RectangularGridRenderer(private val cellSize: Int, val distances: DijkstraDistances, private val inset: Int) {

    fun toPng(grid: Grid): BufferedImage {
        val rendering = RectangularGridRendering(grid, distances, cellSize, inset)

        rendering.setColor(Color.WHITE)
        rendering.drawBackground()

        rendering.setColor(Color.BLACK)
        rendering.drawCells()

        return rendering.img
    }

}
