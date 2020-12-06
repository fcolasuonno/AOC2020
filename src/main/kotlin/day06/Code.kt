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

fun parse(input: List<String>) = input.joinToString(separator = "\n").split("\n\n")

fun part1(input: List<String>) {
    val res = input.sumBy { it.filterNot { it == '\n' }.toSet().count() }
    println("Part 1 = $res")
}

fun part2(input: List<String>) {
    val res = input.sumBy {
        it.split("\n").map { it.toSet() }.reduce { acc, set -> acc.intersect(set) }.size
    }
    println("Part 2 = $res")
}