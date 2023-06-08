package tasklist

import kotlin.system.exitProcess

val tasks = mutableListOf<MutableList<String>>()

fun main() {
    while (true) {
        println("Input an action (add, print, end):")
        when (readln()) {
            "add" -> add()
            "print" -> print()
            "end" -> {
                println("Tasklist exiting!")
                exitProcess(0)
            }
            else -> println("The input action is invalid")
        }
    }
}

fun add() {
    println("Input a new task (enter a blank line to end):")
    val task = mutableListOf<String>()
    while (true) {
        val userInp = readLine()?.trim()
        if (userInp.isNullOrBlank()) {
            if (task.size == 0) {
                println("The task is blank")
                return
            }
            break
        } else {
            task.add(userInp)
        }
    }
    tasks.add(task)
}

fun print() {
    if (tasks.isEmpty()) {
        println("No tasks have been input")
    } else {
        for ((index, task) in tasks.withIndex()) {
            val taskNumber = index + 1
            for (i in 0 until task.size) {
                if (i == 0) {
                    println(String.format("%-3s%s", taskNumber, task[i]))
                } else {
                    println(String.format("   " + task[i]))
                }
            }
            println()
        }
    }
}
