package day24

import isDebug
import java.io.File

private val Pair<Int, Int>.hexNeighbours: Set<Pair<Int, Int>>
    get() = Direction.values().map { Pair(first + it.x, second + it.y) }.toSet()

fun main() {
    val name = if (isDebug()) "test.txt" else "input.txt"
    System.err.println(name)
    val dir = ::main::class.java.`package`.name
    val input = File("src/main/kotlin/$dir/$name").readLines()
    val parsed = parse(input)
    part1(parsed)
    part2(parsed)
}

enum class Direction(val x: Int, val y: Int) {
    e(2, 0),
    se(1, -1),
    sw(-1, -1),
    w(-2, 0),
    nw(-1, 1),
    ne(1, 1)
}

fun parse(input: List<String>) = input.map {
    mutableListOf<Direction>().apply {
        var cursor = 0
        while (cursor < it.length) {
            add(try {
                Direction.valueOf(it.substring(cursor, cursor + 1)).also { cursor++ }
            } catch (_: Throwable) {
                Direction.valueOf(it.substring(cursor, cursor + 2)).also { cursor += 2 }
            })
        }
    }.toList()
}


fun part1(input: List<List<Direction>>) {
    val map = mutableMapOf<Pair<Int, Int>, Boolean>().apply {
        input.forEach {
            val pos = it.fold(0 to 0) { acc, direction ->
                acc.copy(acc.first + direction.x, acc.second + direction.y)
            }
            this[pos] = !getOrDefault(pos, false)
        }
    }.toMap()
    println("Part 1 = ${map.count { it.value }}")
}

fun part2(input: List<List<Direction>>) {
    val startingBlacks = mutableMapOf<Pair<Int, Int>, Boolean>().apply {
        input.forEach {
            val pos = it.fold(0 to 0) { acc, direction ->
                acc.copy(acc.first + direction.x, acc.second + direction.y)
            }
            this[pos] = !getOrDefault(pos, false)
        }
    }.filterValues { it }.keys
    val res = generateSequence(startingBlacks) { blacks ->
        val neighbours = blacks.associateWith { it.hexNeighbours }
        val toWhite = blacks.filter {
            val nextBlacks = neighbours.getValue(it).count { it in blacks }
            nextBlacks == 0 || nextBlacks > 2
        }
        val whiteNeighbours = (neighbours.flatMap { it.value } - blacks).groupingBy { it }.eachCount()
        val newBlacks = whiteNeighbours.filter { it.value == 2 }.keys
        blacks - toWhite + newBlacks
    }.elementAt(100).count()
    println("Part 2 = $res")
}