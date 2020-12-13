package day12

import isDebug
import java.io.File
import kotlin.math.abs

fun main() {
    val name = if (isDebug()) "test.txt" else "input.txt"
    System.err.println(name)
    val dir = ::main::class.java.`package`.name
    val input = File("src/main/kotlin/$dir/$name").readLines()
    val parsed = parse(input)
    part1(parsed)
    part2(parsed)
}

private val lineStructure = """(.)(\d+)""".toRegex()

fun parse(input: List<String>) = input.map {
    lineStructure.matchEntire(it)?.destructured?.let {
        val (instruction, amount) = it.toList()
        instruction.single() to amount.toInt()
    }
}.requireNoNulls()

fun part1(input: List<Pair<Char, Int>>) {
    val res = input.fold('E' to (0 to 0)) { (dir, pos), (instr, amount) ->
        when (instr) {
            'L' -> generateSequence(dir) { it.left() }.elementAt(amount / 90)
            'R' -> generateSequence(dir) { it.right() }.elementAt(amount / 90)
            else -> dir
        } to when (instr) {
            'F' -> pos.move(dir, amount)
            'L', 'R' -> pos
            else -> pos.move(instr, amount)
        }
    }.let { (_, pos) -> pos.manhattam() }
    println("Part 1 = $res")
}

fun part2(input: List<Pair<Char, Int>>) {
    val res = input.fold((10 to 1) to (0 to 0)) { (waypoint, pos), (instr, amount) ->
        when (instr) {
            'F' -> waypoint
            'R' -> generateSequence(waypoint) { (x, y) -> y to -x }.elementAt(amount / 90)
            'L' -> generateSequence(waypoint) { (x, y) -> -y to x }.elementAt(amount / 90)
            else -> waypoint.move(instr, amount)
        } to when (instr) {
            'F' -> generateSequence(pos) { (x, y) -> (waypoint.first + x) to (waypoint.second + y) }.elementAt(amount)
            else -> pos
        }
    }.let { (_, pos) -> pos.manhattam() }
    println("Part 2 = $res")
}

fun Pair<Int, Int>.manhattam() = abs(first) + abs(second)

fun Pair<Int, Int>.move(dir: Char, amount: Int): Pair<Int, Int> = when (dir) {
    'N' -> copy(second = second + amount)
    'E' -> copy(first = first + amount)
    'S' -> copy(second = second - amount)
    'W' -> copy(first = first - amount)
    else -> throw IllegalArgumentException()
}

fun Char.left() = "NWSE".let { it[(it.indexOf(this) + 1) % 4] }

fun Char.right() = "NESW".let { it[(it.indexOf(this) + 1) % 4] }