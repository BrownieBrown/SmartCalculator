fun main() {
    val input = readLine()!!.toMutableList()
    if (input.size % 2 == 0) {
        input.removeAt(input.size / 2)
        input.removeAt(input.size / 2)
        println(input.joinToString(""))

    } else {
        input.removeAt(input.size / 2)
        println(input.joinToString(""))
    }

}
