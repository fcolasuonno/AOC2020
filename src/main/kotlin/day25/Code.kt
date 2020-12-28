package day25

import isDebug
import java.io.File

fun main() {
    val name = if (isDebug()) "test.txt" else "input.txt"
    System.err.println(name)
    val dir = ::main::class.java.`package`.name
    val input = File("src/main/kotlin/$dir/$name").readLines()
    val parsed = parse(input)
    part(parsed)
}

fun parse(input: List<String>) = input.map(String::toLong).sorted()

fun part(input: List<Long>) {
    val (card, door) = input
    val res = encryptSequence(door).elementAt(encryptSequence(7).indexOfFirst { it == card })
    println("Part 1 = $res")
}

private fun encryptSequence(subject: Long) = generateSequence(1L) { subject * it % 20201227L }
