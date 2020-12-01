package day01

import isDebug
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val name = if (isDebug()) "test.txt" else "input.txt"
    System.err.println(name)
    val dir = ::main::class.java.`package`.name
    val input = File("src/main/kotlin/$dir/$name").readLines()
    val parsed = parse(input)
    System.err.println("Part 1 = ${
        measureTimeMillis {
            part1(parsed)
        }
    }ms")
    System.err.println("Part 2 = ${
        measureTimeMillis {
            part2(parsed)
        }
    }ms")
}

fun parse(input: List<String>) = input.map(String::toInt).sorted().requireNoNulls()

fun part1(input: List<Int>) {
    for (i in input.indices) {
        val first = input[i]
        for (j in i until input.size) {
            val second = input[j]
            if (first + second > 2020) break
            if (first + second == 2020) println("Part 1 = ${first * second}")
        }
    }
}

fun part2(input: List<Int>) {
    for (i in input.indices) {
        val first = input[i]
        for (j in i until input.size) {
            val second = input[j]
            if (first + second > 2020) break
            for (k in j until input.size) {
                val third = input[k]
                if (first + second + third > 2020) break
                if (first + second + third == 2020) println("Part 1 = ${first * second * third}")
            }
        }
    }
}

fun List<Int>.multiplyTogether(): Int = fold(1) { acc, i -> acc * i }