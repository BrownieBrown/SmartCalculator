fun main() {
    val source: String = readLine()!!
    println(readLine()!!.toRegex().findAll(source).count())
}
