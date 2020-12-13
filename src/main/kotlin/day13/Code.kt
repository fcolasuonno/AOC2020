package day13

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

fun parse(input: List<String>) = input.first().toInt() to input[1].split(',').mapIndexedNotNull { index, s ->
    s.toIntOrNull()?.let { IndexedValue(index, it) }
}

fun part1(input: Pair<Int, List<IndexedValue<Int>>>) {
    val (minTimestamp, schedules) = input
    val res = schedules
        .map { (_, schedule) -> schedule to ((minTimestamp / schedule + 1) * schedule) }
        .minByOrNull { it.second }
        ?.let { (schedule, departure) ->
            schedule * (departure - minTimestamp)
        }
    println("Part 1 = $res")
}

fun part2(input: Pair<Int, List<IndexedValue<Int>>>) {
    val (_, times) = input
    val res = times
        .map { it.index.toLong() to it.value.toLong() }
        .reduce { (minTimestamp, period), (offset, schedule) ->
            val matchingOffset = generateSequence(minTimestamp) { i -> i + period }.first { min ->
                val part1 = (min / schedule + 1) * schedule - min
                part1 == (offset % schedule)
            }
            matchingOffset to period * schedule
        }.first
    println("Part 2 = $res")
}