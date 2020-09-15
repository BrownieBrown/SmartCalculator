import java.util.Scanner

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val (a, b, c, n) = IntArray(4) { scanner.nextInt() }

    val upper = ('A'..'Z').toList()
    val lower = ('a'..'z').toList()
    val nums = ('0'..'9').toList()

    var answer = ""
    val d = c + (n - a - b - c)

    for (i in 0 until a) {
        answer += upper[i % upper.size]
    }
    for (i in 0 until b) {
        answer += lower[i % lower.size]
    }
    for (i in 0 until d) {
        answer += nums[i % nums.size]
    }

    println(answer)
}