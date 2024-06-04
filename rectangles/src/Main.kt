/*
//This type of task is about precise positioning of rectangles
import java.io.File
import kotlin.math.max
import kotlin.math.min

data class Rectangle(val x1: Int, val y1: Int, val x2: Int, val y2: Int)
data class CanvasExtremePoints(val minX: Int, val minY: Int, val maxX: Int, val maxY: Int)

class Table(private val rectangles: List<Rectangle>) {

    companion object {
        const val MIN_CELL_SIZE = 20
        const val MAX_TABLE_WIDTH = 500
    }

    fun generateTable(): String {
        val (canvasMinX, canvasMinY, canvasMaxX, canvasMaxY) = calculateCanvasSize()
        val numCellsX = canvasMaxX - canvasMinX + 1
        val numCellsY = canvasMaxY - canvasMinY + 1
        val cellSize = min(MIN_CELL_SIZE, MAX_TABLE_WIDTH / max(numCellsX, numCellsY))

        return buildString {
            append("<html><body><table border='1' cellpadding='0' cellspacing='0'>")
            for (y in canvasMinY..canvasMaxY) {
                append("<tr>")
                for (x in canvasMinX..canvasMaxX) {
                    val style = "width: ${cellSize}px; height: ${cellSize}px; border: 1px solid rgba(0, 0, 0, 0.1);"
                    append("<td style='$style${if (isInRectangle(x, y)) " background-color: black;" else ""}'></td>")
                }
                append("</tr>")
            }
            append("</table></body></html>")
        }
    }

    private fun calculateCanvasSize(): CanvasExtremePoints {
        val minX = rectangles.minOf { it.x1 }
        val minY = rectangles.minOf { it.y1 }
        val maxX = rectangles.maxOf { it.x2 }
        val maxY = rectangles.maxOf { it.y2 }
        return CanvasExtremePoints(minX, minY, maxX, maxY)
    }

    private fun isInRectangle(x: Int, y: Int): Boolean {
        return rectangles.any { rect -> x in rect.x1..rect.x2 && y in rect.y1..rect.y2 }
    }
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException("Empty command line arguments")
    }

    val fileName = args[0]
    val rectangles = try {
        File(fileName).readLines().map { parseRectangle(it) }
    } catch (e: Exception) {
        println("Error reading file: ${e.message}")
        return
    }

    File("output.html").writeText(Table(rectangles).generateTable())
}

fun parseRectangle(line: String): Rectangle {
    val coordinates = line.split(",").map { it.trim().toIntOrNull() }
    if (coordinates.size != 4 || coordinates.any { it == null }) {
        throw IllegalArgumentException("Invalid rectangle format ($line)")
    }
    return Rectangle(coordinates[0]!!, coordinates[1]!!, coordinates[2]!!, coordinates[3]!!)
}*/

//This type of task is about relative positioning of rectangles
import java.io.File
import kotlin.math.max
import kotlin.math.min

data class Rectangle(val x1: Int, val y1: Int, val x2: Int, val y2: Int)
data class CanvasExtremePoints(val minX: Int, val minY: Int, val maxX: Int, val maxY: Int)

class Table(private val rectangles: List<Rectangle>) {

    companion object {
        const val MIN_CELL_SIZE = 20
        const val MAX_TABLE_WIDTH = 500
        const val MAX_GAP = 10
    }

    fun generateTable(): String {
        val adjustedRectangles = adjustRectangles(rectangles)
        val (canvasMinX, canvasMinY, canvasMaxX, canvasMaxY) = calculateCanvasSize(adjustedRectangles)
        val numCellsX = canvasMaxX - canvasMinX + 1
        val numCellsY = canvasMaxY - canvasMinY + 1
        val cellSize = min(MIN_CELL_SIZE, MAX_TABLE_WIDTH / max(numCellsX, numCellsY))

        return buildString {
            append("<html><body><table border='1' cellpadding='0' cellspacing='0'>")
            for (y in canvasMinY..canvasMaxY) {
                append("<tr>")
                for (x in canvasMinX..canvasMaxX) {
                    val style = "width: ${cellSize}px; height: ${cellSize}px; border: 1px solid rgba(0, 0, 0, 0.1);"
                    append("<td style='$style${if (isInRectangle(adjustedRectangles, x, y)) " background-color: black;" else ""}'></td>")
                }
                append("</tr>")
            }
            append("</table></body></html>")
        }
    }

    private fun adjustRectangles(rectangles: List<Rectangle>): List<Rectangle> {
        val adjustedRectangles = mutableListOf<Rectangle>()
        rectangles.sortedBy { it.y1 }.forEach { rectangle ->
            if (adjustedRectangles.isEmpty()) {
                adjustedRectangles.add(rectangle)
            } else {
                val lastRect = adjustedRectangles.last()
                val newX1 = if (rectangle.x1 - lastRect.x2 > MAX_GAP) lastRect.x2 + MAX_GAP else rectangle.x1
                val newY1 = if (rectangle.y1 - lastRect.y2 > MAX_GAP) lastRect.y2 + MAX_GAP else rectangle.y1
                val newX2 = newX1 + (rectangle.x2 - rectangle.x1)
                val newY2 = newY1 + (rectangle.y2 - rectangle.y1)
                adjustedRectangles.add(Rectangle(newX1, newY1, newX2, newY2))
            }
        }
        return adjustedRectangles
    }

    private fun calculateCanvasSize(rectangles: List<Rectangle>): CanvasExtremePoints {
        val minX = rectangles.minOf { it.x1 }
        val minY = rectangles.minOf { it.y1 }
        val maxX = rectangles.maxOf { it.x2 }
        val maxY = rectangles.maxOf { it.y2 }
        return CanvasExtremePoints(minX, minY, maxX, maxY)
    }

    private fun isInRectangle(rectangles: List<Rectangle>, x: Int, y: Int): Boolean {
        return rectangles.any { rect -> x in rect.x1..rect.x2 && y in rect.y1..rect.y2 }
    }
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException("Empty command line arguments")
    }

    val fileName = args[0]
    val rectangles = try {
        File(fileName).readLines().map { parseRectangle(it) }
    } catch (e: Exception) {
        println("Error reading file: ${e.message}")
        return
    }

    File("output.html").writeText(Table(rectangles).generateTable())
}

fun parseRectangle(line: String): Rectangle {
    val coordinates = line.split(",").map { it.trim().toIntOrNull() }
    if (coordinates.size != 4 || coordinates.any { it == null }) {
        throw IllegalArgumentException("Invalid rectangle format ($line)")
    }
    return Rectangle(coordinates[0]!!, coordinates[1]!!, coordinates[2]!!, coordinates[3]!!)
}