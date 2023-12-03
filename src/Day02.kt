fun main() {
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

data class Game(val number: Int, val sets: List<Rgb>)

fun part1(input: List<String>): Int {
    return games(input)
        .filter { it.sets.all { it.r <= 12 && it.g <= 13 && it.b <= 14 } }
        .sumOf { it.number }
}

fun games(input: List<String>): List<Game> = input
    .map { line -> line
        .substringAfter("Game ")
        .split(": ")
        .let { Game(it[0].toInt(), game(it[1])) }
    }

data class Rgb(var r: Int = 0, var g: Int = 0, var b: Int = 0)

fun rgb(set: String): Rgb {
    val rgb = Rgb()
    set.split(", ").forEach {
        if (it.endsWith("red")) {
            rgb.r = it.split(" ")[0].toInt()
        }
        else if (it.endsWith("green")) {
            rgb.g = it.split(" ")[0].toInt()
        }
        else if (it.endsWith("blue")) {
            rgb.b = it.split(" ")[0].toInt()
        }
        else {
            error("Unknown color")
        }
    }
    return rgb
}

fun game(desc: String): List<Rgb> {
    return desc.split("; ").map { rgb(it) }
}

fun part2(input: List<String>): Int {
    return games(input)
        .sumOf {
            it.sets.maxOf { it.r } * it.sets.maxOf { it.g } * it.sets.maxOf { it.b }
        }
}
