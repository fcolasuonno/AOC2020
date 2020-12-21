package day21

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

private val lineStructure = """(.+) \(contains (.+)\)""".toRegex()

fun parse(input: List<String>) = input.map {
    lineStructure.matchEntire(it)?.destructured?.let {
        val (ingredients, allergens) = it.toList()
        ingredients.split(" ").toSet() to allergens.split(", ").toSet()
    }
}.requireNoNulls()

fun part1(input: List<Pair<Set<String>, Set<String>>>) {
    val allergens = input.flatMap { it.second }.toSet()
    val useMap =
        allergens.map { allergen -> allergen to input.filter { allergen in it.second }.map { it.first } }.toMap()
    val possibleAllergens = useMap.values.flatMap { it.reduce { acc, set -> acc.intersect(set) } }.toSet()
    val count = input.sumBy { it.first.count { it !in possibleAllergens } }
    println("Part 1 = $count")
}

fun part2(input: List<Pair<Set<String>, Set<String>>>) {
    val allergens = input.flatMap { it.second }.toSet()
    val useMap =
        allergens.map { allergen -> allergen to input.filter { allergen in it.second }.map { it.first } }.toMap()
    val allergenMap = useMap.mapValues {
        it.value.reduce { acc, set -> acc.intersect(set) }
    }.toMutableMap()
    val found = mutableSetOf<String>()
    while (allergenMap.any { it.value.size > 1 }) {
        found.addAll(allergenMap.values.filter { it.size == 1 }.flatten())
        allergenMap.forEach { (allergen, possible) ->
            if (possible.size > 1) {
                allergenMap[allergen] = possible - found
            }
        }
    }
    val res = allergenMap.entries.sortedBy { it.key }.map { it.value.single() }.joinToString(",")
    println("Part 2 = $res")
}