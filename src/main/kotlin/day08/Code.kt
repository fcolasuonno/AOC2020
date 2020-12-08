package day08

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

private val lineStructure = """(...) ([+-]\d+)""".toRegex()

data class Regs(var acc: Int = 0, var ip: Int = 0)

interface Op {
    fun execute(regs: Regs): Regs
}

data class Nop(val value: Int) : Op {
    override fun execute(regs: Regs) = regs.copy(ip = regs.ip + 1)
}

data class Jmp(val value: Int) : Op {
    override fun execute(regs: Regs) = regs.copy(ip = regs.ip + value)
}

data class Acc(val value: Int) : Op {
    override fun execute(regs: Regs) = regs.copy(ip = regs.ip + 1, acc = regs.acc + value)
}

val opcodeMap = mapOf<String, (Int) -> Op>("nop" to { x -> Nop(x) },
        "jmp" to { x -> Jmp(x) },
        "acc" to { x -> Acc(x) })

fun parse(input: List<String>) = input.map {
    lineStructure.matchEntire(it)?.destructured?.let {
        val (opcode, op) = it.toList()
        opcodeMap.getValue(opcode)(op.toInt())
    }
}.requireNoNulls()

fun part1(input: List<Op>) {
    val seen = mutableSetOf<Int>()
    val cpu = generateSequence(Regs()) { regs ->
        seen.add(regs.ip)
        val op = input.getOrNull(regs.ip)
        op?.execute(regs)
    }
    val last = cpu.takeWhile { regs -> regs.ip !in seen }.last()
    val res = last.acc
    println("Part 1 = $res")
}

fun part2(input: List<Op>) {
    val changeOp = input.withIndex().filter { it.value is Jmp || it.value is Nop }.map { it.index }
    val res = changeOp.map { change ->
        input.mapIndexed { index, op ->
            when {
                index != change -> op
                op is Jmp -> Nop(op.value)
                op is Nop -> Jmp(op.value)
                else -> op
            }
        }
    }.map { execute(it) }.single { it.ip >= input.size }.acc
    println("Part 2 = $res")
}

fun execute(input: List<Op>): Regs {
    val seen = mutableSetOf<Int>()
    return generateSequence(Regs()) { regs ->
        seen.add(regs.ip)
        input.getOrNull(regs.ip)?.execute(regs)
    }.takeWhile { regs -> regs.ip !in seen }.last()
}
