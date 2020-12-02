package day02

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

data class PasswordPolice(val low: Int, val high: Int, val letter: Char, val password: String)

private val lineStructure = """(\d+)-(\d+) (.): (.+)""".toRegex()

fun parse(input: List<String>) = input.map {
    lineStructure.matchEntire(it)?.destructured?.let {
        val (low, high, letter, password) = it.toList()
        PasswordPolice(low.toInt(), high.toInt(), letter.single(), password)
    }
}.requireNoNulls()

fun part1(input: List<PasswordPolice>) {
    val res = input.count { policy ->
        policy.password
                .count { it == policy.letter } in policy.low..policy.high
    }
    println("Part 1 = $res")
}

fun part2(input: List<PasswordPolice>) {
    val res = input.count { policy ->
        listOf(policy.password[policy.low - 1], policy.password[policy.high - 1])
                .count { it == policy.letter } == 1
    }
    println("Part 2 = $res")
}