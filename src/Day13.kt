import kotlin.math.min

fun main() {

    fun isHorizontalReflexion(puzzle: List<String>, column: Int): Boolean {
        val minSize = min(column, puzzle[0].length - column)
//        println(minSize)
        return puzzle.all {
            var before = it.substring(0, column).reversed()
            if (before.length > minSize) before = before.substring(0, minSize)
            var after = it.substring(column)
            if (after.length > minSize) after = after.substring(0, minSize)
//            println("$before ==? $after")
            before == after
        }
    }

    fun translate(puzzle: List<String>): List<String> {
        return puzzle[0].indices.map {
            puzzle.joinToString("") { line -> "" + line[it] }
        }
    }

    fun isVerticalReflexion(puzzle: List<String>, row: Int): Boolean {
        return isHorizontalReflexion(translate(puzzle), row)
    }

    fun findReflexion(puzzle: List<String>): Long {
        puzzle[0].indices.forEach { col ->
            if (col > 0 && isHorizontalReflexion(puzzle, col)) {
                return 1L * col
            }
        }
        puzzle.indices.forEach { row ->
            if (row > 0 && isVerticalReflexion(puzzle, row)) {
                return 100L * row
            }
        }

        error("")
    }

    fun part1(input: List<String>): Long {
        val puzzles = mutableListOf<MutableList<String>>(mutableListOf())

        input.forEach {
            if (it.isEmpty()) {
                puzzles.add(mutableListOf())
            }
            else {
                puzzles.last().add(it)
            }
        }

        return puzzles.sumOf { findReflexion(it) }
    }

    fun isHorizontalSmudgedReflexion(puzzle: List<String>, column: Int): Boolean {
        val minSize = min(column, puzzle[0].length - column)
//        println(minSize)
        return puzzle.sumOf {
            var before = it.substring(0, column).reversed()
            if (before.length > minSize) before = before.substring(0, minSize)
            var after = it.substring(column)
            if (after.length > minSize) after = after.substring(0, minSize)
//            println("$before ==? $after")
            before.mapIndexed { index, c -> if (c != after[index]) 1 else 0 }.sum()
        } == 1
    }

    fun isVerticalSmudgedReflexion(puzzle: List<String>, row: Int): Boolean {
        return isHorizontalSmudgedReflexion(translate(puzzle), row)
    }

    fun findSmudgedReflexion(puzzle: List<String>): Long {
        puzzle[0].indices.forEach { col ->
            if (col > 0 && isHorizontalSmudgedReflexion(puzzle, col)) {
                return 1L * col
            }
        }
        puzzle.indices.forEach { row ->
            if (row > 0 && isVerticalSmudgedReflexion(puzzle, row)) {
                return 100L * row
            }
        }

        error("")
    }

    fun part2(input: List<String>): Long {
        val puzzles = mutableListOf<MutableList<String>>(mutableListOf())

        input.forEach {
            if (it.isEmpty()) {
                puzzles.add(mutableListOf())
            }
            else {
                puzzles.last().add(it)
            }
        }

        return puzzles.sumOf { findSmudgedReflexion(it) }

    }

    val input = readInput("Day13")

    part1(input).println()
    part2(input).println()
}
