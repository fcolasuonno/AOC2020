package day18

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

fun parse(input: List<String>) = input.map { it.replace(" ", "") }

fun part1(input: List<String>) {
    val res = input.sumOf { it.solve1(0).first }
    println("Part 1 = $res")
}

fun String.solve1(start: Int): Pair<Long, Int> {
    var (acc, current) = if (this[start] == '(') solve1(start + 1) else this[start].asLong() to (start + 1)
    do {
        val op: ((Long, Long) -> Long) = if (this[current++] == '+') Long::plus else Long::times
        val next = if (this[current] == '(') solve1(current + 1) else this[current].asLong() to current + 1
        acc = op(acc, next.first)
        current = next.second
    } while (current < length && get(current) != ')')
    return acc to (current + 1)
}

private fun Char.asLong() = (this - '0').toLong()

fun part2(input: List<String>) {
    val res = input.sumOf { it.solve2(0).first }
    println("Part 2 = $res")
}

fun String.solve2(start: Int): Pair<Long, Int> {
    var (acc, current) = if (this[start] == '(') solve2(start + 1) else this[start].asLong() to (start + 1)
    val mult = mutableListOf<Long>()
    do {
        val op: ((Long, Long) -> Long) = if (this[current++] == '+') Long::plus else { a, b -> b.also { mult.add(a) } }
        val next = if (this[current] == '(') solve2(current + 1) else this[current].asLong() to current + 1
        acc = op(acc, next.first)
        current = next.second
    } while (current < length && get(current) != ')')
    acc = mult.fold(acc) { a, l -> a * l }
    return acc to (current + 1)
}