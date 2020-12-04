package day04

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

private val lineStructure = """(...):(.+)""".toRegex()

fun parse(input: List<String>): List<Map<String, String>> {
    val passports = input.joinToString(separator = "\n").split("\n\n")
    return passports.map { passwort ->
        passwort.split("""\s""".toRegex()).mapNotNull {
            lineStructure.matchEntire(it)?.destructured?.let {
                val (key, value) = it.toList()
                key to value
            }
        }.toMap()
    }
}

val required = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

fun part1(input: List<Map<String, String>>) {
    val res = input.count { it.keys.containsAll(required) }
    println("Part 1 = $res")
}

fun part2(input: List<Map<String, String>>) {
    val res = input.count { passport ->
        passport.keys.containsAll(required) &&
                passport.getValue("byr").let {
                    """\d\d\d\d""".toRegex().matches(it) && it.toInt() in 1920..2002
                } &&
                passport.getValue("iyr").let {
                    """\d\d\d\d""".toRegex().matches(it) && it.toInt() in 2010..2020
                } &&
                passport.getValue("eyr").let {
                    """\d\d\d\d""".toRegex().matches(it) && it.toInt() in 2020..2030
                } &&
                passport.getValue("hgt").let {
                    """(\d+)(cm|in)""".toRegex().matchEntire(it)?.destructured?.let {
                        val (height, metric) = it.toList()
                        height.toInt() in if (metric == "cm") 150..193 else 59..76
                    } == true
                } &&
                passport.getValue("hcl").let {
                    """#[0-9a-f]{6}""".toRegex().matches(it)
                } &&
                passport.getValue("ecl").let {
                    it in setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
                } &&
                passport.getValue("pid").let {
                    """\d{9}""".toRegex().matches(it)
                }
    }
    println("Part 2 = $res")
}