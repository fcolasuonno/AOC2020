package day17

import isDebug
import java.io.File

private val Triple<Int, Int, Int>.neighbours: Set<Triple<Int, Int, Int>>
    get() = (-1..1).flatMap { d1 ->
        (-1..1).flatMap { d2 ->
            (-1..1).map { d3 ->
                Triple(first + d1, second + d2, third + d3)
            }
        }
    }.filter { it != this }.toSet()

fun main() {
    val name = if (isDebug()) "test.txt" else "input.txt"
    System.err.println(name)
    val dir = ::main::class.java.`package`.name
    val input = File("src/main/kotlin/$dir/$name").readLines()
    val parsed = parse(input)
    part1(parsed)
    part2(parsed)
}

fun parse(input: List<String>) = input.flatMapIndexed { y, s ->
    s.mapIndexedNotNull { x, c -> c.takeIf { it == '#' }?.let { Triple(x, y, 0) } }
}.toSet()

fun part1(input: Set<Triple<Int, Int, Int>>) {
    var start = input
    fun Triple<Int, Int, Int>.neighboursCount() = neighbours.count { it in start }
    repeat(6) {
        start = mutableSetOf<Triple<Int, Int, Int>>().apply {
            start.filterTo(this) { each -> each.neighboursCount().let { it == 2 || it == 3 } }
            start.flatMapTo(mutableSetOf()) { it.neighbours }
                .filterTo(this) { each -> each !in start && each.neighboursCount() == 3 }
        }
    }
    println("Part 1 = ${start.size}")
}

fun part2(input: Set<Triple<Int, Int, Int>>) {
    var start = input.mapTo(mutableSetOf()) { Quadruple(it.first, it.second, it.third, 0) }
    fun Quadruple.neighboursCount() = neighbours.count { it in start }
    repeat(6) {
        start = mutableSetOf<Quadruple>().apply {
            start.filterTo(this) { each -> each.neighboursCount().let { it == 2 || it == 3 } }
            start.flatMapTo(mutableSetOf()) { it.neighbours }
                .filterTo(this) { each -> each !in start && each.neighboursCount() == 3 }
        }
    }
    println("Part 2 = ${start.size}")
}

data class Quadruple(
    val first: Int,
    val second: Int,
    val third: Int,
    val fourth: Int
) {
    val neighbours: Set<Quadruple>
        get() = (-1..1).flatMap { d1 ->
            (-1..1).flatMap { d2 ->
                (-1..1).flatMap { d3 ->
                    (-1..1).map { d4 ->
                        Quadruple(first + d1, second + d2, third + d3, fourth + d4)
                    }
                }
            }
        }.filter { it != this }.toSet()
}