package tasklist

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import java.time.LocalTime
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
    var priority = ""
    var date = ""
    var time = ""
    while (true) {
        println("Input the task priority (C, H, N, L):")
        priority = readln()
        if (priority.lowercase() == "c" || priority.lowercase() == "h" || priority.lowercase() == "n" || priority.lowercase() == "l") {
            break
        }
    }
    while (true) {
        println("Input the date (yyyy-mm-dd):")
        try {
            val inp = readln().split("-")
            date = LocalDate.parse("${inp[0]}-${inp[1].padStart(2, '0')}-${inp[2].padStart(2, '0')}").toString()
            break
        } catch (e: java.lang.Exception) {
            println("The input date is invalid")
        }
    }
    while (true) {
        println("Input the time (hh:mm):")
        try {
            val inp = readln().split(":")
            time = LocalTime.parse("${inp[0].padStart(2, '0')}:${inp[1].padStart(2, '0')}").toString()
            break
        } catch (e:java.lang.Exception) {
            println("The input time is invalid")
        }

    }
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
    task.add(0, "$date $time ${priority.uppercase()}")
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
