package day05

import isDebug
import java.io.File
import java.util.*

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
    pass.map { it == 'B' || it == 'R' }.binToInt()
}.toSortedSet()

fun part1(input: SortedSet<Int>) {
    val res = input.last()
    println("Part 1 = $res")
}

fun part2(input: SortedSet<Int>) {
    val res = input.let { ids ->
        val range = ids.first()..ids.last()
        range.first { it !in ids }
    }
    println("Part 2 = $res")
}

fun List<Boolean>.binToInt() = fold(0) { acc, b -> acc * 2 + if (b) 1 else 0 }
