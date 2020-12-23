package day23

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

fun parse(input: List<String>) = input.single().map { it - '0' }

fun part1(input: List<Int>) {
    val cups = (input + input.first()).zipWithNext().toMap().toMutableMap()
    val res = generateSequence(input.first()) { index ->
        val p1 = cups[index]!!
        val p2 = cups[p1]!!
        val p3 = cups[p2]!!
        val next = cups[p3]!!
        var destination = index
        do {
            destination--
            if (destination == 0) destination = 9
        } while (destination == p1 || destination == p2 || destination == p3)
        cups[index] = next
        cups[p3] = cups[destination]!!
        cups[destination] = p1
        next
    }.take(100 + 1).last().let {
        generateSequence(cups[1]) { cups[it] }.take(8).joinToString("")
    }
    println("Part 1 = $res")
}

fun part2(input: List<Int>) {
    val max = 1000000
    val cups = (input + (10..max) + input.first()).zipWithNext().toMap().toMutableMap()
    val res = generateSequence(input.first()) { index ->
        val p1 = cups[index]!!
        val p2 = cups[p1]!!
        val p3 = cups[p2]!!
        val next = cups[p3]!!
        var destination = index
        do {
            destination--
            if (destination == 0) destination = max
        } while (destination == p1 || destination == p2 || destination == p3)
        cups[index] = next
        cups[p3] = cups[destination]!!
        cups[destination] = p1
        next
    }.take(10000000 + 1).last().let {
        cups.let {
            val first = it.getValue(1)
            first.toLong() * it.getValue(first)
        }
    }
    println("Part 2 = $res")
}
