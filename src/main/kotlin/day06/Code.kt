package day06

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

fun parse(input: List<String>) = input.collect(String::isEmpty)

fun Collection<String>.collect(splitOn: (String) -> Boolean = String::isEmpty) = fold(mutableListOf(mutableListOf<String>())) { acc, s ->
    acc.apply { if (splitOn(s)) add(mutableListOf()) else last().add(s) }
}

fun part1(input: MutableList<MutableList<String>>) {
    val res = input.sumBy { it.joinToString(separator = "").toSet().size }
    println("Part 1 = $res")
}

fun part2(input: MutableList<MutableList<String>>) {
    val res = input.sumBy { it.map { it.toSet() }.reduce { acc, set -> acc.intersect(set) }.size }
    println("Part 2 = $res")
}