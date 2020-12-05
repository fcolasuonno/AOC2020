package day05

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

fun parse(input: List<String>) = input.map { pass ->
    pass.take(7).map { it == 'B' } to pass.takeLast(3).map { it == 'R' }
}.requireNoNulls()

fun part1(input: List<Pair<List<Boolean>, List<Boolean>>>) {
    val res = input.map { (row, col) -> row.binToInt() * 8 + col.binToInt() }.maxOrNull()
    println("Part 1 = $res")
}

fun part2(input: List<Pair<List<Boolean>, List<Boolean>>>) {
    val res = input.map { (row, col) -> row.binToInt() * 8 + col.binToInt() }.sorted().let { ids ->
        val range = requireNotNull(ids.minOrNull())..requireNotNull(ids.maxOrNull())
        range.single { it !in ids }
    }
    println("Part 2 = $res")
}

fun List<Boolean>.binToInt() = fold(0) { acc, b -> acc * 2 + if (b) 1 else 0 }
