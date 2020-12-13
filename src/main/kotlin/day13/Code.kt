package day13

import isDebug
import java.io.File
import java.math.BigInteger

fun main() {
    val name = if (isDebug()) "test.txt" else "input.txt"
    System.err.println(name)
    val dir = ::main::class.java.`package`.name
    val input = File("src/main/kotlin/$dir/$name").readLines()
    val parsed = parse(input)
    part1(parsed)
    part2(parsed)
}

fun parse(input: List<String>) = input.first().toInt() to input[1].split(',').mapIndexedNotNull { index, s ->
    s.toIntOrNull()?.let { IndexedValue(index, it) }
}

fun part1(input: Pair<Int, List<IndexedValue<Int>>>) {
    val (minTimestamp, schedules) = input
    val res = schedules
            .map { (_, schedule) -> schedule to ((minTimestamp / schedule + 1) * schedule) }
            .minByOrNull { it.second }
            ?.let { (schedule, departure) ->
                schedule * (departure - minTimestamp)
            }
    println("Part 1 = $res")
}

fun part2(input: Pair<Int, List<IndexedValue<Int>>>) {
    val (_, times) = input
    val res = times.map { it.index.toBigInteger() to it.value.toBigInteger() }
            .fold(BigInteger.ZERO to BigInteger.ONE) {
                // reduce one by one each "departure at a given offset with period" to an "accumulator" bus with
                // starting offset = first matching start for all the previous buses
                // period = period at which new matching offset occur
                (minTimestamp, period), (offset, value) ->
                val newPeriod = period * value  //all numbers in input are coprime, so a matching departure time has
                // a periodicity of the product of the previous periods

                val modInverse = value.modInverse(period)   // modInverse finds x for a * x = 1 + k * m
                // where a is the period of the current bus
                // k is any integer and m is the period of the accumulator bus
                // (a bus that has a period corresponding to all the previous
                // "match" events
                val multiplier = (minTimestamp + offset) * modInverse % period
                // modInverse finds the multiplier for an offset 1, but we need the multiplier for the actual offset
                // that is the current bus offset plus the accumulated bus offset.
                // The multiplier is a multiple of the period, so use module to find the smallest positive one
                val newOffset = multiplier * value - offset
                // find the new offset by multiplying the current bus period by the found multiplier and rebasing back to
                // the accumulator bus offset
                newOffset to newPeriod
            }.first
    println("Part 2 = $res")
}