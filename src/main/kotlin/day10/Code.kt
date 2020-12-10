package day10

import day09.getMax
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


fun parse(input: List<String>) = input.map {
    it.toInt()
}.requireNoNulls()

fun part1(input: List<Int>) {
    val res = (input + (input.getMax() + 3) + 0).sorted().zipWithNext().map { it.second - it.first }.groupBy { it }.let {
        it.getValue(1).count() * it.getValue(3).count()
    }
    println("Part 1 = $res")
}

fun part2(input: List<Int>) {
    val res = (input + 0).sorted().let { list ->
        list.dropLast(1).foldRight(mapOf(list.last() to 1L)) { adapter, pathCountMap ->
            pathCountMap + (adapter to list.filter { it in (adapter + 1)..(adapter + 3) }.sumOf { pathCountMap.getValue(it) })
        }[0]
    }
    println("Part 2 = $res")
}
