fun main() {
    val inputList = readLine()!!.split(" ")
    println(inputList.maxBy { it.length })
}