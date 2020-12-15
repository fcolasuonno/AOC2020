package day15

import isDebug
import java.io.File
import java.util.*
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
    val seen = mutableMapOf<Int, Deque<Int>>().withDefault { LinkedList() }
    input.forEachIndexed { index, i ->
        seen.getOrPut(i).push(index + 1)
    }
    var last = input.last()
    val res = generateSequence(input.size + 1) { it + 1 }.map { num ->
        last = seen.getValue(last).takeIf { it.size > 1 }?.let { deque -> deque.first - deque.removeLast() } ?: 0
        seen.getOrPut(last).push(num)
        last
    }.elementAt(2020 - input.size - 1)
    println("Part 1 = $res")
}

fun part2(input: List<Int>) {
    val seen = mutableMapOf<Int, Deque<Int>>().withDefault { LinkedList() }
    input.forEachIndexed { index, i ->
        seen.getOrPut(i).push(index + 1)
    }
    var last = input.last()
    val res = generateSequence(input.size + 1) { it + 1 }.map { num ->
        last = seen.getValue(last).takeIf { it.size > 1 }?.let { deque -> deque.first - deque.removeLast() } ?: 0
        seen.getOrPut(last).push(num)
        last
    }.elementAt(30000000 - input.size - 1)
    println("Part 2 = $res")
}

fun <K, V> MutableMap<K, V>.getOrPut(key: K) = getValue(key).also { putIfAbsent(key, it) }
