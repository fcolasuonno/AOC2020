package day22

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

fun parse(input: List<String>) = input.indexOf("").let {
    input.subList(1, it).map { it.toInt() } to input.subList(it + 2, input.size).map { it.toInt() }
}

fun part1(input: Pair<List<Int>, List<Int>>) {
    val game = generateSequence(input) { (p1, p2) ->
        if (p1.isEmpty() || p2.isEmpty()) null else {
            val firsts = p1.first() to p2.first()
            p1.drop(1).let { if (firsts.first > firsts.second) (it + firsts.first + firsts.second) else it } to
                    p2.drop(1).let { if (firsts.second > firsts.first) (it + firsts.second + firsts.first) else it }
        }
    }
    println(
        "Part 1 = ${
            game.last().let { if (it.first.isNotEmpty()) it.first else it.second }.let { cards ->
                cards.foldIndexed(0L) { index: Int, acc: Long, i: Int -> acc + i.toLong() * (cards.size - index) }
            }
        }"
    )
}

fun part2(input: Pair<List<Int>, List<Int>>) {
    val seen: MutableMap<String, Boolean> = mutableMapOf()
    val game = winner(input, 1, seen).let { if (it.first.isNotEmpty()) it.first else it.second }.let { cards ->
        cards.foldIndexed(0L) { index: Int, acc: Long, i: Int -> acc + i.toLong() * (cards.size - index) }
    }
    println("Part 2 = $game")
}

private fun winner(
    input: Pair<List<Int>, List<Int>>,
    i: Int,
    seen: MutableMap<String, Boolean>
): Pair<List<Int>, List<Int>> {
    val seenP1 = mutableSetOf<String>()
    val seenP2 = mutableSetOf<String>()
    return generateSequence(input) { (p1, p2) ->
        val p1String = p1.toString()
        val p2String = p2.toString()
        when {
            p1.isEmpty() || p2.isEmpty() -> null
            p1String in seenP1 || p2String in seenP2 -> System.err.println("found").let { null }
            else -> {
                seenP1.add(p1String)
                seenP2.add(p2String)
                val firsts = p1.first() to p2.first()
                val p1Hand = p1.drop(1)
                val p2Hand = p2.drop(1)
                //            System.err.println("Game $i $p1Hand $p2Hand")
                val p1Wins = if (p1Hand.size >= firsts.first && p2Hand.size >= firsts.second) {
                    if (i > 3) true else {
                        val subGameString = (p1Hand to p2Hand).toString()
                        seen[subGameString] ?: winner(p1Hand to p2Hand, i + 1, seen).first.isNotEmpty().also {
                            seen[subGameString] = it
                            seen[(p2Hand to p1Hand).toString()] = !it
                        }
                    }
                } else {
                    firsts.first > firsts.second
                }
                p1Hand.let { if (p1Wins) (it + firsts.first + firsts.second) else it } to
                        p2Hand.let { if (!p1Wins) (it + firsts.second + firsts.first) else it }
            }
        }
    }.last()
}