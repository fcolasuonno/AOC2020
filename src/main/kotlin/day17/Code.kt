package day17

import isDebug
import java.io.File

private val Triple<Int, Int, Int>.neighbours: Set<Triple<Int, Int, Int>>
    get() = setOf(
        Triple(first - 1, second - 1, third - 1),
        Triple(first - 1, second - 1, third),
        Triple(first - 1, second - 1, third + 1),
        Triple(first - 1, second, third - 1),
        Triple(first - 1, second, third),
        Triple(first - 1, second, third + 1),
        Triple(first - 1, second + 1, third - 1),
        Triple(first - 1, second + 1, third),
        Triple(first - 1, second + 1, third + 1),
        Triple(first, second - 1, third - 1),
        Triple(first, second - 1, third),
        Triple(first, second - 1, third + 1),
        Triple(first, second, third - 1),
//        Triple(first,second,third),
        Triple(first, second, third + 1),
        Triple(first, second + 1, third - 1),
        Triple(first, second + 1, third),
        Triple(first, second + 1, third + 1),
        Triple(first + 1, second - 1, third - 1),
        Triple(first + 1, second - 1, third),
        Triple(first + 1, second - 1, third + 1),
        Triple(first + 1, second, third - 1),
        Triple(first + 1, second, third),
        Triple(first + 1, second, third + 1),
        Triple(first + 1, second + 1, third - 1),
        Triple(first + 1, second + 1, third),
        Triple(first + 1, second + 1, third + 1),
    )

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
    repeat(6) {
        val active1 = start.filter { each -> each.neighbours.count { it in start }.let { it == 2 || it == 3 } }.toSet()
        val active2 = start.flatMap { it.neighbours }.toSet()
            .filter { each -> each !in start && each.neighbours.count { it in start } == 3 }.toSet()
        start = active1 + active2
    }
    println("Part 1 = ${start.size}")
}

fun part2(input: Set<Triple<Int, Int, Int>>) {
    var start = input.map { Quadruple(it.first, it.second, it.third, 0) }.toSet()
    repeat(6) {
        val active1 = start.filter { each -> each.neighbours.count { it in start }.let { it == 2 || it == 3 } }.toSet()
        val active2 = start.flatMap { it.neighbours }.toSet()
            .filter { each -> each !in start && each.neighbours.count { it in start } == 3 }.toSet()
        start = active1 + active2
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
        get() = setOf(
            Quadruple(first - 1, second - 1, third - 1, fourth - 1),
            Quadruple(first - 1, second - 1, third, fourth - 1),
            Quadruple(first - 1, second - 1, third + 1, fourth - 1),
            Quadruple(first - 1, second, third - 1, fourth - 1),
            Quadruple(first - 1, second, third, fourth - 1),
            Quadruple(first - 1, second, third + 1, fourth - 1),
            Quadruple(first - 1, second + 1, third - 1, fourth - 1),
            Quadruple(first - 1, second + 1, third, fourth - 1),
            Quadruple(first - 1, second + 1, third + 1, fourth - 1),
            Quadruple(first, second - 1, third - 1, fourth - 1),
            Quadruple(first, second - 1, third, fourth - 1),
            Quadruple(first, second - 1, third + 1, fourth - 1),
            Quadruple(first, second, third - 1, fourth - 1),
            Quadruple(first, second, third, fourth - 1),
            Quadruple(first, second, third + 1, fourth - 1),
            Quadruple(first, second + 1, third - 1, fourth - 1),
            Quadruple(first, second + 1, third, fourth - 1),
            Quadruple(first, second + 1, third + 1, fourth - 1),
            Quadruple(first + 1, second - 1, third - 1, fourth - 1),
            Quadruple(first + 1, second - 1, third, fourth - 1),
            Quadruple(first + 1, second - 1, third + 1, fourth - 1),
            Quadruple(first + 1, second, third - 1, fourth - 1),
            Quadruple(first + 1, second, third, fourth - 1),
            Quadruple(first + 1, second, third + 1, fourth - 1),
            Quadruple(first + 1, second + 1, third - 1, fourth - 1),
            Quadruple(first + 1, second + 1, third, fourth - 1),
            Quadruple(first + 1, second + 1, third + 1, fourth - 1),

            Quadruple(first - 1, second - 1, third - 1, fourth),
            Quadruple(first - 1, second - 1, third, fourth),
            Quadruple(first - 1, second - 1, third + 1, fourth),
            Quadruple(first - 1, second, third - 1, fourth),
            Quadruple(first - 1, second, third, fourth),
            Quadruple(first - 1, second, third + 1, fourth),
            Quadruple(first - 1, second + 1, third - 1, fourth),
            Quadruple(first - 1, second + 1, third, fourth),
            Quadruple(first - 1, second + 1, third + 1, fourth),
            Quadruple(first, second - 1, third - 1, fourth),
            Quadruple(first, second - 1, third, fourth),
            Quadruple(first, second - 1, third + 1, fourth),
            Quadruple(first, second, third - 1, fourth),
//            Quadruple(first, second, third, fourth),
            Quadruple(first, second, third + 1, fourth),
            Quadruple(first, second + 1, third - 1, fourth),
            Quadruple(first, second + 1, third, fourth),
            Quadruple(first, second + 1, third + 1, fourth),
            Quadruple(first + 1, second - 1, third - 1, fourth),
            Quadruple(first + 1, second - 1, third, fourth),
            Quadruple(first + 1, second - 1, third + 1, fourth),
            Quadruple(first + 1, second, third - 1, fourth),
            Quadruple(first + 1, second, third, fourth),
            Quadruple(first + 1, second, third + 1, fourth),
            Quadruple(first + 1, second + 1, third - 1, fourth),
            Quadruple(first + 1, second + 1, third, fourth),
            Quadruple(first + 1, second + 1, third + 1, fourth),

            Quadruple(first - 1, second - 1, third - 1, fourth + 1),
            Quadruple(first - 1, second - 1, third, fourth + 1),
            Quadruple(first - 1, second - 1, third + 1, fourth + 1),
            Quadruple(first - 1, second, third - 1, fourth + 1),
            Quadruple(first - 1, second, third, fourth + 1),
            Quadruple(first - 1, second, third + 1, fourth + 1),
            Quadruple(first - 1, second + 1, third - 1, fourth + 1),
            Quadruple(first - 1, second + 1, third, fourth + 1),
            Quadruple(first - 1, second + 1, third + 1, fourth + 1),
            Quadruple(first, second - 1, third - 1, fourth + 1),
            Quadruple(first, second - 1, third, fourth + 1),
            Quadruple(first, second - 1, third + 1, fourth + 1),
            Quadruple(first, second, third - 1, fourth + 1),
            Quadruple(first, second, third, fourth + 1),
            Quadruple(first, second, third + 1, fourth + 1),
            Quadruple(first, second + 1, third - 1, fourth + 1),
            Quadruple(first, second + 1, third, fourth + 1),
            Quadruple(first, second + 1, third + 1, fourth + 1),
            Quadruple(first + 1, second - 1, third - 1, fourth + 1),
            Quadruple(first + 1, second - 1, third, fourth + 1),
            Quadruple(first + 1, second - 1, third + 1, fourth + 1),
            Quadruple(first + 1, second, third - 1, fourth + 1),
            Quadruple(first + 1, second, third, fourth + 1),
            Quadruple(first + 1, second, third + 1, fourth + 1),
            Quadruple(first + 1, second + 1, third - 1, fourth + 1),
            Quadruple(first + 1, second + 1, third, fourth + 1),
            Quadruple(first + 1, second + 1, third + 1, fourth + 1),
        )
}