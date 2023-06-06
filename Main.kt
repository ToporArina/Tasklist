package tasklist

fun main() {
    val tasks = mutableListOf<String>()

    // Ввод заданий
    println("Input the tasks (enter a blank line to end):")
    while (true) {
        val task = readLine()?.trim()
        if (task.isNullOrBlank()) {
            break
        }
        tasks.add(task)
    }

    // Вывод заданий с номерами
    if (tasks.isEmpty()) {
        println("No tasks have been input")
    } else {
        for ((index, task) in tasks.withIndex()) {
            val taskNumber = index + 1
            val indentation = if (taskNumber in 1..9) "  " else " "
            println("$taskNumber$indentation$task")
        }
    }
}
