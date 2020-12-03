package day03

import day01.multiplyTogether
import isDebug
import java.io.File

fun main() {
    val name = if (isDebug()) "test.txt" else "input.txt"
    System.err.println(name)
    val dir = ::main::class.java.`package`.name
    val input = File("src/main/kotlin/$dir/$name").readLines()
    val parsed = parse(input)
    part1(parsed)
    part2(parsed)
}

data class Grid(val input: List<String>) {
    operator fun get(xy: Pair<Int, Int>) =
            input.getOrNull(xy.second)?.let { row -> row.getOrNull(xy.first % row.length) }
}

fun parse(input: List<String>) = Grid(input)

fun part1(input: Grid) {
    val res = projectLine(delta = Pair(3, 1)).mapSequence { line -> input[line] }.count { it == '#' }
    println("Part 1 = $res")
}

fun part2(input: Grid) {
    val res = listOf(Pair(1, 1), Pair(3, 1), Pair(5, 1), Pair(7, 1), Pair(1, 2)).map { delta ->
        projectLine(delta).mapSequence { line -> input[line] }.count { it == '#' }
    }.multiplyTogether()
    println("Part 2 = $res")
}

fun projectLine(delta: Pair<Int, Int>, start: Pair<Int, Int> = Pair(0, 0)) = generateSequence(start) { Pair(it.first + delta.first, it.second + delta.second) }

fun <T, V> Sequence<T>.mapSequence(function: (T) -> V?) = map(function).takeWhile { it != null }
