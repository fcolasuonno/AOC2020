package day15

import isDebug
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val name = if (isDebug()) "test.txt" else "input.txt"
    System.err.println(name)
    val dir = ::main::class.java.`package`.name
    val input = File("src/main/kotlin/$dir/$name").readLines()
    val parsed = parse(input)
    part1(parsed)
    System.err.println(measureTimeMillis {
        part2(parsed)
    })
}


fun parse(input: List<String>) = input.single().split(",").map {
    it.toInt()
}.requireNoNulls()

fun part1(input: List<Int>) {
    val seen = mutableMapOf<Int, Int>()
    input.forEachIndexed { index, i ->
        seen[i] = index + 1
    }
    val res = generateSequence(input.size + 1) { it + 1 }.take(2020 - input.size).fold(input.last()) { last, turn ->
        (seen[last]?.let { turn - 1 - it } ?: 0).also {
            seen[last] = turn - 1
        }
    }
    println("Part 1 = $res")
}

fun part2(input: List<Int>) {
    val seen = mutableMapOf<Int, Int>()
    input.forEachIndexed { index, i ->
        seen[i] = index + 1
    }
    val res = generateSequence(input.size + 1) { it + 1 }.take(30000000 - input.size).fold(input.last()) { last, turn ->
        (seen[last]?.let { turn - 1 - it } ?: 0).also {
            seen[last] = turn - 1
        }
    }
    println("Part 2 = $res")
}