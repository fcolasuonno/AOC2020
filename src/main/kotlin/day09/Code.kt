package day09

import isDebug
import java.io.File

fun main() {
    val name = if (isDebug()) "test.txt" else "input.txt"
    val size = if (isDebug()) 5 else 25
    System.err.println(name)
    val dir = ::main::class.java.`package`.name
    val input = File("src/main/kotlin/$dir/$name").readLines()
    val parsed = parse(input)
    val v1 = part1(parsed, size)
    part2(parsed, v1)
}

fun parse(input: List<String>) = input.map {
    it.toLong()
}.requireNoNulls()

fun part1(input: List<Long>, size: Int): Long {
    val res = input.withIndex().filter { (i, _) -> i >= size }.first { (i, num) ->
        isInvalid(num, input.subList(i - size, i))
    }.value
    println("Part 1 = $res")
    return res
}

fun isInvalid(num: Long, subList: List<Long>) = subList.none { firstNum ->
    subList.filter { i -> i != firstNum }.any { secondNum -> firstNum + secondNum == num }
}

fun part2(input: List<Long>, v1: Long) {
    val res = (2..input.size).asSequence()
            .flatMap { input.windowed(it) }
            .first { it.sum() == v1 }
            .let { it.getMin() + it.getMax() }
    println("Part 2 = $res")
}

fun <T : Comparable<T>> Iterable<T>.getMin() = requireNotNull(minOrNull())
fun <T : Comparable<T>> Iterable<T>.getMax() = requireNotNull(maxOrNull())
