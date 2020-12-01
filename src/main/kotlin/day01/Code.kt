package day01

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

fun parse(input: List<String>) = input.map(String::toInt).requireNoNulls()

fun part1(input: List<Int>) {
    val filtered = input.filter { first ->
        input.any { second -> second + first == 2020 && second != first }
    }
    val res = filtered.multiplyTogether()
    println("Part 1 = $res")
}

fun part2(input: List<Int>) {
    val filtered = input.filter { first ->
        input.any { second ->
            input.any { third ->
                third + second + first == 2020 &&
                        second != first &&
                        third != first &&
                        second != third
            }
        }
    }
    val res = filtered.multiplyTogether()
    println("Part 2 = $res")
}

fun List<Int>.multiplyTogether(): Int = fold(1) { acc, i -> acc * i }