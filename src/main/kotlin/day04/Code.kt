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

data class Passport(val map: Map<String, String>) {
    val byr by map
    val iyr by map
    val eyr by map
    val hgt by map
    val hcl by map
    val ecl by map
    val pid by map
}

private val lineStructure = """(...):(.+)""".toRegex()

fun parse(input: List<String>): List<Passport> {
    val passports = input.joinToString(separator = "\n").split("\n\n")
    return passports.map { passport ->
        Passport(passport.split("""\s""".toRegex()).mapNotNull {
            lineStructure.matchEntire(it)?.destructured?.let {
                val (key, value) = it.toList()
                key to value
            }
        }.toMap())
    }
}

val required = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

fun part1(input: List<Passport>) {
    val res = input.count { it.map.keys.containsAll(required) }
    println("Part 1 = $res")
}

fun part2(input: List<Passport>) {
    val res = input.count { passport ->
        passport.run {
            map.keys.containsAll(required) &&
                    """\d\d\d\d""".toRegex().matches(byr) && byr.toInt() in 1920..2002 &&
                    """\d\d\d\d""".toRegex().matches(iyr) && iyr.toInt() in 2010..2020 &&
                    """\d\d\d\d""".toRegex().matches(eyr) && eyr.toInt() in 2020..2030 &&
                    """(\d+)(cm|in)""".toRegex().matchEntire(hgt)?.destructured?.let {
                        it.component1().toInt() in if (it.component2() == "cm") 150..193 else 59..76
                    } == true &&
                    """#[0-9a-f]{6}""".toRegex().matches(hcl) &&
                    ecl in setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth") &&
                    """\d{9}""".toRegex().matches(pid)
        }
    }
    println("Part 2 = $res")
}