package day11

import isDebug
import java.io.File

fun main() {
    val name = if (isDebug()) "test.txt" else "input.txt"
    System.err.println(name)
    val dir = ::main::class.java.`package`.name
    val input = File("src/main/kotlin/$dir/$name").readLines()
    val (parsed, size) = parse(input)
    part1(parsed)
    part2(parsed, size)
}

fun parse(input: List<String>) = input.flatMapIndexed { j, line ->
    line.withIndex().filterNot { (_, c) -> c == '.' }.map { (i, c) -> (i to j) to (c == '#') }
}.requireNoNulls().toMap() to maxOf(input.first().count(), input.count())

fun part1(input: Map<Pair<Int, Int>, Boolean>) {
    val neighboursMap = input.mapValues { (k, _) -> k.neighbours.filter { it in input } }
    val res = generateSequence(input) { seats ->
        seats.mapValues { (k, occupied) ->
            val neighboursCount = neighboursMap.getValue(k).count {
                seats.getOrDefault(it, false)
            }
            when {
                !occupied && neighboursCount == 0 -> true
                occupied && neighboursCount >= 4 -> false
                else -> occupied
            }
        }
    }.map { it.values.count { it } }.zipWithNext().takeWhile { (first, second) -> first != second }.last().second
    println("Part 1 = $res")
}

fun part2(input: Map<Pair<Int, Int>, Boolean>, size: Int) {
    val neighboursMap = input.mapValues { (k, _) ->
        (1..size).asSequence().let { range ->
            listOfNotNull(
                    range.map { (k.first - it) to k.second }.firstOrNull { it in input },
                    range.map { (k.first - it) to (k.second - it) }.firstOrNull { it in input },
                    range.map { (k.first - it) to (k.second + it) }.firstOrNull { it in input },
                    range.map { (k.first + it) to k.second }.firstOrNull { it in input },
                    range.map { (k.first + it) to (k.second - it) }.firstOrNull { it in input },
                    range.map { (k.first + it) to (k.second + it) }.firstOrNull { it in input },
                    range.map { (k.first) to (k.second - it) }.firstOrNull { it in input },
                    range.map { (k.first) to (k.second + it) }.firstOrNull { it in input })
        }
    }
    val res = generateSequence(input) { seats ->
        seats.mapValues { (k, occupied) ->
            val neighboursCount = neighboursMap.getValue(k).count {
                seats.getOrDefault(it, false)
            }
            when {
                !occupied && neighboursCount == 0 -> true
                occupied && neighboursCount >= 5 -> false
                else -> occupied
            }
        }
    }.map { it.values.count { it } }.zipWithNext().takeWhile { (first, second) -> first != second }.last().second
    println("Part 2 = $res")
}

val Pair<Int, Int>.neighbours: List<Pair<Int, Int>>
    get() = ((first - 1)..(first + 1)).flatMap { i -> ((second - 1)..(second + 1)).map { j -> i to j } }.filter {
        it != this
    }