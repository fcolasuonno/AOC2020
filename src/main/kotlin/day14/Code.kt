package day14

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

private val lineStructure1 = """mask = (.+)""".toRegex()
private val lineStructure2 = """mem\[(\d+)] = (\d+)""".toRegex()

fun parse(input: List<String>) = input.map {
    lineStructure1.matchEntire(it)?.destructured?.let {
        val (mask) = it.toList()
        mask
    } to lineStructure2.matchEntire(it)?.destructured?.let {
        val (pos, value) = it.toList()
        pos.toLong() to value.toLong()
    }
}.requireNoNulls()

data class Program(
    val mem: MutableMap<Long, Long> = mutableMapOf(),
    var andMask: Long = 0L,
    var orMask: Long = 0L,
    var float: List<Long> = emptyList()
)

fun part1(input: List<Pair<String?, Pair<Long, Long>?>>) {
    val res = input.fold(Program()) { program, (m, v) ->
        program.apply {
            m?.let { mask ->
                andMask = mask.parse('0').inv()
                orMask = mask.parse('1')
            }
            v?.let { (pos, value) ->
                mem[pos] = value.and(andMask).or(orMask)
            }
        }
    }.mem.values.sum()
    println("Part 1 = $res")
}

fun part2(input: List<Pair<String?, Pair<Long, Long>?>>) {
    val res = input.fold(Program()) { program, (m, v) ->
        program.apply {
            m?.let { mask ->
                andMask = mask.parse('X').inv()
                orMask = mask.parse('1')
                float = mask.withIndex().filter { it.value == 'X' }.map { 2L.shl(it.index) }
            }
            v?.let { (pos, value) ->
                float.fold(listOf(pos.and(andMask).or(orMask))) { acc, l ->
                    acc + acc.map { it + l }
                }.forEach {
                    mem[it] = value
                }
            }
        }
    }.mem.values.sum()
    println("Part 2 = $res")
}

private fun String.parse(char: Char) = map { if (it == char) '1' else '0' }.joinToString("").toLong(2)