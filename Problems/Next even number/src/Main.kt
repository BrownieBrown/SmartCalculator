import java.util.Scanner

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val number = scanner.nextInt()
    if ((number + 1) % 2 == 0) println(number + 1) else println(number + 2)
}