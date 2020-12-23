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

fun parse(input: List<String>) = input.single().map {
    it - '0'
}

fun part1(input: List<Int>) {
    val seq = generateSequence(input to 0) { (cups, index) ->
        val nextIndices = listOf((index + 1) % cups.size, (index + 2) % cups.size, (index + 3) % cups.size)
        val pick = cups.slice(nextIndices)
        var destination = cups[index]
        do {
            destination--
            if (destination == 0) destination = 9
        } while (destination in pick)
        val newIndices = cups.indices.toMutableList().apply {
            removeAll(nextIndices)
            addAll((indexOf(cups.indexOf(destination)) + 1), nextIndices)
        }.run {
            val currentIndexDiff = indexOf(index) - index
            (0..8).map { i -> get((i + currentIndexDiff) % size) }
        }
        cups.slice(newIndices) to ((index + 1) % cups.size)
    }.take(100 + 1).last().first
    val res = (seq + seq).let { it.dropWhile { it != 1 }.drop(1).take(8).joinToString("") }
    println("Part 1 = $res")
}

fun part2(input: List<Int>) {
    val res = input.size
    println("Part 2 = $res")
}