package day20

import isDebug
import java.io.File
import kotlin.math.sqrt

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

data class Tile(val tile: Long, val data: List<List<Boolean>>, val neightbours: List<Long?>) {
    val top = neightbours[0]
    val right = neightbours[1]
    val bottom = neightbours[2]
    val left = neightbours[3]

    fun oriented(rTop: Long?, rLeft: Long?) =
        when {
            rTop == this.top && rLeft == this.left -> this
            rTop == this.top && rLeft == this.right -> flipH()
            rTop == this.bottom && rLeft == this.left -> flipV()
            rTop == this.right && rLeft == this.bottom -> rotCW().flipV()
            rTop == this.left && rLeft == this.top -> rotCW().flipH()
            rTop == this.left && rLeft != this.top -> rotCW()
            rTop != this.left && rLeft == this.top -> rotCW().rotCW().rotCW()
            rTop != this.left && rLeft != this.top -> rotCW().rotCW()
            else -> TODO("Not yet implemented TOP:$top , LEFT:$left -> RTOP:$rTop, RLEFT:$rLeft")
        }

    private fun flipH() = copy(data = data.flipH(), neightbours = neightbours.flipH())
    private fun flipV() = copy(data = data.flipV(), neightbours = neightbours.flipV())
    private fun rotCW() = copy(data = data.rotateCW(), neightbours = neightbours.rotateCW())

    //    override fun toString() = data.joinToString("\n") { it.joinToString("") { if (it) "#" else "." } }
    override fun toString() = "$tile: T=$top, R=$right, B=$bottom, L=$left"

    private fun List<Long?>.rotateCW() = mapIndexed { index, _ -> get((index + size - 1) % size) }
    private fun List<Long?>.flipH() = listOf(get(0), get(3), get(2), get(1))
    private fun List<Long?>.flipV() = listOf(get(2), get(1), get(0), get(3))

}

fun part2(input: Map<Long, List<List<Boolean>>>) {
    val tiles = input.mapValues {
        Tile(it.key, it.value, it.value.borders.map { border ->
            input.filter { other ->
                other.key != it.key && other.value.borders.let { border in it || border.reversed() in it }
            }.keys.singleOrNull()
        })
    }
    val size = sqrt(input.size.toDouble()).toInt()
    val arranged = MutableList(size) { MutableList<Tile?>(size) { null } }
    var current = tiles.entries.first { it.value.neightbours.count { it == null } == 2 }.key
    var top: Long? = null
    var left: Long? = null
    for (i in 0 until size) {
        for (j in 0 until size) {
            val oriented = tiles.getValue(current).oriented(top, left)
            arranged[i][j] = oriented
            if (oriented.right != null) {
                left = current
                top = arranged.getOrNull(i - 1)?.getOrNull(j + 1)?.tile
                current = oriented.right
            } else {
                left = null
                top = arranged[i][0]?.tile
                current = arranged[i][0]?.bottom ?: 0L
            }
        }
    }
    val grid = List(size * 8) { y ->
        List(size * 8) { x ->
            arranged[y / 8][x / 8]!!.data[y % 8 + 1][x % 8 + 1]
        }
    }
    val monster = listOf(
        "                  # ",
        "#    ##    ##    ###",
        " #  #  #  #  #  #   "
    ).flatMapIndexed { y, s -> s.indices.filter { s[it] == '#' }.map { x -> x to y } }
    val res = listOf(
        grid,
        grid.rotateCW(),
        grid.rotateCW().rotateCW(),
        grid.rotateCW().rotateCW().rotateCW(),
        grid.flipH(),
        grid.flipV(),
        grid.rotateCW().flipV(),
        grid.rotateCW().flipH(),
    ).map { possibleGrid ->
        (0..(grid.size - 20)).sumBy { x ->
            (0..(grid.size - 3)).count { y ->
                monster.all { possibleGrid[it.second + y][it.first + x] }
            }
        }
    }.maxOrNull()?.let { grid.sumBy { it.count { it } } - it * monster.count() }
    println("Part 2 = $res")
}

fun List<List<Boolean>>.rotateCW() = indices.map { index -> reversed().map { it[index] } }
fun List<List<Boolean>>.flipH() = map { it.reversed() }
fun List<List<Boolean>>.flipV() = reversed()