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
    val res = (input + input.first()).play().take(8).joinToString("")
    println("Part 1 = $res")
}

fun part2(input: List<Int>) {
    val res = (input + 10).play(1000000, 10000000).take(2).fold(1L) { acc, i -> acc * i }
    println("Part 2 = $res")
}

private fun List<Int>.play(num: Int = size - 1, rounds: Int = 100) = IntArray(num) { it + 1 }
    .also { list ->
        map { it - 1 }.zipWithNext().forEach { list[it.first] = it.second }
        var index = first() - 1
        if (num > size) {
            list[list.size - 1] = index
        }
        repeat(rounds) {
            val p1 = list[index]
            val p2 = list[p1]
            val p3 = list[p2]
            val next = list[p3]
            var destination = index
            do {
                destination--
                if (destination == -1) destination = (list.size - 1)
            } while (destination == p1 || destination == p2 || destination == p3)
            list[index] = next
            list[p3] = list[destination]
            list[destination] = p1
            index = next
        }
    }.let { list ->
        generateSequence(list[0]) { list[it] }.map { it + 1 }
    }

