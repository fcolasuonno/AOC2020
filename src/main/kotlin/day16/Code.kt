package day16

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

data class Rule(val name: String, val range1: IntRange, val range2: IntRange) {
    override fun toString() = name

    operator fun contains(value: Int) = value in range1 || value in range2
}

private val ruleStructure = """([^:]+): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex()

fun parse(input: List<String>): Triple<List<Rule>, List<Int>, List<List<Int>>> {
    val (rules, mine, others) = input.joinToString(separator = "\n").split("\n\n")
    return Triple(rules.split("\n").map {
        ruleStructure.matchEntire(it)?.destructured?.let {
            val (rule, range1Start, range1End, range2Start, range2End) = it.toList()
            Rule(rule, range1Start.toInt()..range1End.toInt(), range2Start.toInt()..range2End.toInt())
        }
    }.requireNoNulls(),
        mine.split("\n").drop(1).single().split(",").map { it.toInt() },
        others.split("\n").drop(1).map { it.split(",").map { it.toInt() } })
}

fun part1(input: Triple<List<Rule>, List<Int>, List<List<Int>>>) {
    val (rules, _, others) = input
    val res = others.flatMap { it.filterNot { field -> rules.any { rule -> field in rule } } }.sum()
    println("Part 1 = $res")
}

fun part2(input: Triple<List<Rule>, List<Int>, List<List<Int>>>) {
    val (rules, mine, others) = input
    val valid = others.filter { it.all { field -> rules.any { rule -> field in rule } } }
    val foundRules = mutableMapOf<Rule, Int>()
    val unknownRules = rules.toMutableSet()
    while (unknownRules.isNotEmpty()) {
        val unknownIndices = mine.indices - foundRules.values
        foundRules += unknownIndices.mapNotNull { index ->
            unknownRules.singleOrNull { rule -> valid.all { it[index] in rule } }?.let { it to index }
        }
        unknownRules -= foundRules.keys
    }
    val res = foundRules
        .filterKeys { if (!isDebug()) it.name.startsWith("departure") else true }
        .values
        .fold(1L) { acc, i -> acc * mine[i] }
    println("Part 2 = $res")
}