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

fun parse(input: List<String>) = input.map {
    it.map { it == '#' }
}.requireNoNulls().withIndex()

fun part1(input: Iterable<IndexedValue<List<Boolean>>>) {
    val res = input.count { (index, list) ->
        list[index * 3 % list.size]
    }
    println("Part 1 = $res")
}

fun part2(input: Iterable<IndexedValue<List<Boolean>>>) {
    val res = input.filter { (index, _) -> index % 2 == 1 }.count { (index, list) ->
        list[index / 2 % list.size]
    } * listOf(1, 3, 5, 7).map { slope ->
        input.count { (index, list) ->
            list[index * slope % list.size]
        }
    }.multiplyTogether()
    println("Part 2 = $res")
}