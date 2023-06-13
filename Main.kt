package tasklist

import kotlinx.datetime.*
import com.squareup.moshi.*
import java.io.File
import java.io.FileReader
import java.time.LocalTime
import kotlin.system.exitProcess


data class TaskList(var date: String, var time: String, var priority: String, var task: MutableList<String>)

var tasks = mutableListOf<TaskList>()
var task = mutableListOf<String>()
var priority = ""
var date = ""
var time = ""
val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
val type = Types.newParameterizedType(MutableList::class.java, TaskList::class.java)
val taskListAdapter = moshi.adapter<MutableList<TaskList>>(type)


var json = ""
val jsonFile = File("tasklist.json")
fun main() {

    if (!jsonFile.exists()) {
        jsonFile.createNewFile()
    } else {
        tasks = taskListAdapter.fromJson(jsonFile.readText()) as MutableList<TaskList>
    }

    while (true) {
        println("Input an action (add, print, edit, delete, end):")
        when (readln()) {
            "add" -> add()
            "print" -> print()
            "edit" -> edit()
            "delete" -> delete()
            "end" -> {
                println("Tasklist exiting!")
                endAndSaveToFile()
                exitProcess(0)
            }
            else -> println("The input action is invalid")
        }
    }
}

fun endAndSaveToFile() {
    val json: String = taskListAdapter.toJson(tasks)
    jsonFile.writeText(json)
}

fun delete() {
    if (tasks.isEmpty()) {
        println("No tasks have been input")
    } else {
        print()
        while (true) {
            println("Input the task number (1-${tasks.size}):")
            var taskNum = 0
            try {
                taskNum = readln().toInt()
            } catch (_: java.lang.Exception) {
            }
            if (taskNum < 1 || taskNum > tasks.size) {
                println("Invalid task number")
            } else {
                tasks.removeAt(taskNum - 1)
                println("The task is deleted")
                break
            }
        }
    }
}

fun edit() {
    if (tasks.isEmpty()) {
        println("No tasks have been input")
    } else {
        print()
        var taskNum = 0
        while (true) {
            println("Input the task number (1-${tasks.size}):")
            try {
                taskNum = readln().toInt()
            } catch (_: java.lang.Exception) {
            }
            if (taskNum < 1 || taskNum > tasks.size) {
                println("Invalid task number")
            } else {
                while (true) {
                    println("Input a field to edit (priority, date, time, task):")
                    when (readln()) {
                        "priority" -> {
                            editPriority()
                            tasks[taskNum - 1].priority = priority
                            println("The task is changed")
                            break
                        }
                        "date" -> {
                            editDate()
                            tasks[taskNum - 1].date = date
                            println("The task is changed")
                            break
                        }
                        "time" -> {
                            editTime()
                            tasks[taskNum - 1].time = time
                            println("The task is changed")
                            break
                        }
                        "task" -> {
                            editTask()
                            tasks[taskNum - 1].task.clear()
                            tasks[taskNum - 1].task.addAll(task)

                            println("The task is changed")
                            break
                        }
                        else -> {
                            println("Invalid field")
                        }
                    }
                }
                break
            }
        }
    }
}

fun editPriority() {
    while (true) {
        println("Input the task priority (C, H, N, L):")
        priority = readln()
        if (priority.lowercase() == "c" || priority.lowercase() == "h" || priority.lowercase() == "n" || priority.lowercase() == "l") {
            break
        }
    }
}

fun editDate() {
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
}

fun editTime() {
    while (true) {
        println("Input the time (hh:mm):")
        try {
            val inp = readln().split(":")
            time = LocalTime.parse("${inp[0].padStart(2, '0')}:${inp[1].padStart(2, '0')}").toString()
            break
        } catch (e: java.lang.Exception) {
            println("The input time is invalid")
        }
    }
}

fun editTask() {
    println("Input a new task (enter a blank line to end):")
    task = mutableListOf()
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
}

fun add() {
    editPriority()
    editDate()
    editTime()
    editTask()
    tasks.add(TaskList(date, time, priority, task))
}

fun print() {
    if (tasks.isEmpty()) {
        println("No tasks have been input")
    } else {
        println(
            """
            +----+------------+-------+---+---+--------------------------------------------+
            | N  |    Date    | Time  | P | D |                   Task                     |
            +----+------------+-------+---+---+--------------------------------------------+
        """.trimIndent()
        )
        for (i in 0 until tasks.size) {
            val taskNumber = i + 1
            for (y in 0 until tasks[i].task.size) {
                val chunk = tasks[i].task[y].chunked(44)
                if (y == 0) {
                    println(
                        String.format(
                            "| %s  | %s | %s | %s | %s |%-44s|",
                            taskNumber,
                            tasks[i].date,
                            tasks[i].time,
                            priority(tasks[i].priority),
                            dueTag(
                                tasks[i].date
                            ), chunk[0]
                        )
                    )
                    if (chunk.size > 1) {
                        for (line in 1 until chunk.size) {
                            println(String.format("|    |            |       |   |   |%-44s|", chunk[line]))
                        }
                    }
                } else {
                    for (line in 0 until chunk.size) {
                        println(String.format("|    |            |       |   |   |%-44s|", chunk[line]))
                    }

                }
            }
            println("+----+------------+-------+---+---+--------------------------------------------+")
        }
    }
}

fun priority(taskPriority: String): String {
    return when (taskPriority.lowercase()) {
        "c" -> "\u001B[101m \u001B[0m"
        "h" -> "\u001B[103m \u001B[0m"
        "n" -> "\u001B[102m \u001B[0m"
        "l" -> "\u001B[104m \u001B[0m"
        else -> ""
    }
}

fun dueTag(taskDate: String): String {
    return when (Clock.System.now().toLocalDateTime(TimeZone.UTC).date.daysUntil(LocalDate.parse(taskDate))) {
        0 -> "\u001B[103m \u001B[0m" //yellow
        in 1..Int.MAX_VALUE -> "\u001B[102m \u001B[0m" //green
        else -> "\u001B[101m \u001B[0m" //red
    }
}
