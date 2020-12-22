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
    input.subList(1, it).map(String::toInt) to input.subList(it + 2, input.size).map(String::toInt)
}

fun part1(input: Pair<List<Int>, List<Int>>) {
    val winner = generateSequence(input) { (p1, p2) ->
        p1.firstOrNull()?.let { p1Hand ->
            p2.firstOrNull()?.let { p2Hand ->
                val p1Cards = p1.drop(1)
                val p2Cards = p2.drop(1)
                val p1Wins = p1Hand > p2Hand
                Pair(
                    if (p1Wins) (p1Cards + p1Hand + p2Hand) else p1Cards,
                    if (!p1Wins) (p2Cards + p2Hand + p1Hand) else p2Cards
                )
            }
        }
    }.last()
    println("Part 1 = ${winner.score}")
}

fun part2(input: Pair<List<Int>, List<Int>>) {
    val res = winner(input).score
    println("Part 2 = $res")
}

private val Pair<List<Int>, List<Int>>.score: Int
    get() = (first.takeIf { it.isNotEmpty() } ?: second).reversed()
        .reduceIndexed { index, acc, i -> acc + i * (index + 1) }

private fun winner(input: Pair<List<Int>, List<Int>>): Pair<List<Int>, List<Int>> =
    Pair(mutableSetOf<List<Int>>(), mutableSetOf<List<Int>>()).run {
        generateSequence(input) { (p1, p2) ->
            p1.takeIf { first.add(it) }?.firstOrNull()?.let { p1Hand ->
                p2.takeIf { second.add(it) }?.firstOrNull()?.let { p2Hand ->
                    val p1Cards = p1.drop(1)
                    val p2Cards = p2.drop(1)
                    val p1Wins = p1Cards.takeIf { it.size >= p1Hand }?.take(p1Hand)?.let { p1Copy ->
                        p2Cards.takeIf { it.size >= p2Hand }?.take(p2Hand)?.let { p2Copy ->
                            winner(p1Copy to p2Copy).first.isNotEmpty()
                        }
                    } ?: (p1Hand > p2Hand)
                    Pair(
                        if (p1Wins) (p1Cards + p1Hand + p2Hand) else p1Cards,
                        if (!p1Wins) (p2Cards + p2Hand + p1Hand) else p2Cards
                    )
                }
            }
        }.last()
    }