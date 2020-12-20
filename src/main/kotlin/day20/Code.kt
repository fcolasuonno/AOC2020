package day20

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

private val List<List<Boolean>>.borders: List<List<Boolean>>
    get() = listOf(
        first(),
        map { it.last() },
        last(),
        map { it.first() }
    )

private val lineStructure = """Tile (\d+):""".toRegex()

fun parse(input: List<String>) = input.joinToString(separator = "\n").split("\n\n").map {
    it.split("\n").let { tileInfo ->
        requireNotNull(lineStructure.matchEntire(tileInfo.first())?.destructured?.let {
            val (tileNum) = it.toList()
            tileNum.toLong() to tileInfo.drop(1).map { it.map { it == '#' } }
        })
    }
}.toMap()

fun part1(input: Map<Long, List<List<Boolean>>>) {
    val res = input.mapValues { it.value.borders }.let {
        val borders = it.values.flatten()
        it.filterValues {
            it.count { border ->
                borders.count {
                    it == border || it == border.reversed()
                } == 1
            } == 2
        }.keys.reduce { acc, i -> acc * i }
    }
    println("Part 1 = $res")
}

fun part2(input: Map<Long, List<List<Boolean>>>) {
    val res = input.size
    println("Part 2 = $res")
}

fun List<List<Boolean>>.rotate() = mapIndexed { index, _ -> map { it[index] } }
fun List<List<Boolean>>.flipH() = map { it.reversed() }
fun List<List<Boolean>>.flipV() = reversed()
