package day19

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

private val lineStructure = """(\d+): (?:([0-9 |]+)|"([ab])")""".toRegex()

sealed class Rule {
    abstract fun consumeMatch(rules: Map<Int, Rule>, string: String?): String?

    data class Terminal(val char: Char) : Rule() {
        override fun consumeMatch(rules: Map<Int, Rule>, string: String?) =
            if (string?.firstOrNull() == char) string.substring(1) else null
    }

    data class Composed(val ruleSet: Set<List<Int>>) : Rule() {
        override fun consumeMatch(rules: Map<Int, Rule>, string: String?) = ruleSet.mapNotNull { alternateRules ->
            alternateRules.map { rules.getValue(it) }.fold(string) { acc, rule ->
                acc?.let { rule.consumeMatch(rules, it) }
            }
        }.firstOrNull()
    }

    data class Nested(val startRule: Int, val endRule: Int) : Rule() {
        override fun consumeMatch(rules: Map<Int, Rule>, string: String?): String? =
            rules.getValue(startRule).consumeMatch(rules, string)?.let {
                val end = rules.getValue(endRule)
                end.consumeMatch(rules, it) ?: end.consumeMatch(rules, consumeMatch(rules, it))
            }
    }
}

fun parse(input: List<String>) = input.mapNotNull {
    lineStructure.matchEntire(it)?.destructured?.let {
        val (ruleNum, composed, terminal) = it.toList()
        ruleNum.toInt() to when {
            terminal.isNotEmpty() -> Rule.Terminal(terminal.single())
            else -> Rule.Composed(composed.split(" | ").map {
                it.split(" ").map(String::toInt)
            }.toSet())
        }
    }
}.toMap() to input.filter { it.isNotEmpty() && !it.first().isDigit() }

fun part1(input: Pair<Map<Int, Rule>, List<String>>) {
    val (rules, tests) = input
    val res = tests.count { rules.getValue(0).consumeMatch(rules, it) == "" }
    println("Part 1 = $res")
}

fun part2(input: Pair<Map<Int, Rule>, List<String>>) {
    val (rules, tests) = input
    val r8 = rules.getValue(8)
    val r11 = Rule.Nested(42, 31)
    val res = tests.count {
        generateSequence(it) {
            r8.consumeMatch(rules, it)
        }.drop(1).any {
            r11.consumeMatch(rules, it) == ""
        }
    }
    println("Part 2 = $res")
}