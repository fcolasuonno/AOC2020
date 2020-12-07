package day07

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

private val lineStructure = """(.+) bags contain (no other bags|((\d+) (.+) bags?, )*((\d+) (.+) bags?)).""".toRegex()
private val bagsStructure = """(\d+) (.+) bags?""".toRegex()

data class Bag(val num: Int, val type: String)

fun parse(input: List<String>) = input.map {
    lineStructure.matchEntire(it)?.destructured?.let {
        val (bagType, bags) = it.toList()
        bagType to if (bags == "no other bags") emptyList() else bags.split(", ").map {
            bagsStructure.matchEntire(it)?.destructured?.let {
                val (num, type) = it.toList()
                Bag(num.toInt(), type)
            }
        }.requireNoNulls()
    }
}.requireNoNulls().toMap()

fun part1(input: Map<String, List<Bag>>) {
    val invert = input.flatMap { (type, bagList) -> bagList.map { it.type to type } }.groupBy { it.first }.mapValues { it.value.map { it.second }.toSet() }
    val check = mutableSetOf("shiny gold")
    val seen = mutableSetOf<String>()
    while (check.isNotEmpty()) {
        seen.addAll(check)
        check.addAll(check.flatMap { invert.getOrDefault(it, emptySet()) })
        check.removeAll(seen)
    }
    println("Part 1 = ${seen.size - 1}")
}

fun part2(input: Map<String, List<Bag>>) {
    val res = generateSequence(listOf(Bag(1, "shiny gold"))) { list: List<Bag> ->
        list.flatMap { bag -> input.getValue(bag.type).map { Bag(it.num * bag.num, it.type) } }.takeIf { it.isNotEmpty() }
    }.sumBy { it.sumBy { it.num } } - 1
    println("Part 2 = $res")
}