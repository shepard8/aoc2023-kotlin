import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {

    data class DigPosition(val x: Long, val y: Long) {
        fun nextInDirection(direction: String): DigPosition {
            return when (direction) {
                "U" -> DigPosition(x, y + 1)
                "D" -> DigPosition(x, y - 1)
                "L" -> DigPosition(x - 1, y)
                "R" -> DigPosition(x + 1, y)
                else -> error("")
            }
        }

        fun nextInDirection(direction: String, length: Long): DigPosition {
            return when (direction) {
                "U" -> DigPosition(x, y + length)
                "D" -> DigPosition(x, y - length)
                "L" -> DigPosition(x - length, y)
                "R" -> DigPosition(x + length, y)
                else -> error("")
            }
        }
    }

    fun part1(input: List<String>): Long {
        val dug = mutableListOf<DigPosition>()
        val colors = hashMapOf<DigPosition, MutableList<String>>()
        var lastPos = DigPosition(0, 0)
        input.forEach { line ->
            val (direction, length, color) = line.split(" ")
            colors.getOrPut(lastPos) { mutableListOf() }.add(color)
            (0..<length.toInt()).forEach {
                lastPos = lastPos.nextInDirection(direction)
                dug.add(lastPos)
                colors.getOrPut(lastPos) { mutableListOf() }.add(color)
            }

        }

        val minX = dug.minOf { it.x }.toInt()
        val minY = dug.minOf { it.y }.toInt()
        val maxX = dug.maxOf { it.x }.toInt()
        val maxY = dug.maxOf { it.y }.toInt()
        val polygonXs = dug.map { (it.x - minX).toInt() }.toIntArray()
        val polygonYs = dug.map { (it.y - minY).toInt() }.toIntArray()
        val image = BufferedImage(maxX - minX + 1, maxY - minY + 1, BufferedImage.TYPE_INT_RGB);
        val g = image.createGraphics();  // not sure on this line, but this seems more right
        g.color = Color.white
        g.fillPolygon(polygonXs, polygonYs, dug.count())
        g.drawPolygon(polygonXs, polygonYs, dug.count())
        image.flush()
        val count = (0..(maxY - minY)).sumOf { y ->
            (0..(maxX - minX)).count { x ->
                image.getRGB(x, y) != image.getRGB(0, maxY)
            }
        }.toLong()
        ImageIO.write(image, "PNG", File("test2.png"))

        return count
    }

    fun part2(input: List<String>): Long {
        var lastPos = DigPosition(0, 0)
        val vertices = mutableListOf<DigPosition>(lastPos)
        var circ = 0L

        input.forEach { line ->
            val (_, _, color) = line.split(" ")
            val direction = when (color.drop(2).dropLast(1).last()) {
                '0' -> "R"
                '1' -> "D"
                '2' -> "L"
                '3' -> "U"
                else -> error("")
            }
            val length = color.drop(2).dropLast(2).toInt(16)
            lastPos = lastPos.nextInDirection(direction, length.toLong())
            vertices.add(lastPos)
            if (direction in listOf("L", "D")) circ += length.toLong()
        }

        val area2 = - (vertices.mapIndexed { index, p ->
            p.x * vertices[(index + 1) % vertices.size].y - (vertices[(index + 1) % vertices.size]).x * p.y
        }).sum() / 2 + circ + 1
        println(area2)

        return 0
    }

    val input = readInput("Day18")

    part1(input).println()
    part2(input).println()
}

