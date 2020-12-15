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
    println("Part 1 = ${part(parsed, 2020)}")
    System.err.println(measureTimeMillis {
        println("Part 2 = ${part(parsed, 30000000)}")
    })
}

fun parse(input: List<String>) = input.single().split(",").map {
    it.toInt()
}.requireNoNulls()

fun part(input: List<Int>, num: Int) = input.mapIndexed { index, i -> i to index + 1 }.toMap().toMutableMap().run {
    generateSequence(input.size + 1) { it + 1 }.take(num - input.size).fold(input.last()) { last, turn ->
        put(last, turn - 1)?.let { previousTurn -> turn - 1 - previousTurn } ?: 0
    }
}