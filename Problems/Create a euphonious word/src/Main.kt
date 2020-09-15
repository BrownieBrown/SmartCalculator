fun main() {
    val word = readLine()!!.toMutableList()
    checkWord(word)
}

fun isVowel(c: Char): Boolean {
    val vowels = listOf('a', 'e', 'i', 'o', 'u', 'y')
    return c in vowels
}

fun checkWord(word: MutableList<Char>) {
    var counter = 0
    var i = 0
    while (i + 2 < word.size) {
        if (isVowel(word[i]) && isVowel(word[i + 1]) && isVowel(word[i + 2])) {
            counter++
            word.add(i + 2, 'b')
//            println(word)
        }
        if (!isVowel(word[i]) && !isVowel(word[i + 1]) && !isVowel(word[i + 2])) {
            counter++
            word.add(i + 2, 'a')
//            println(word)
        }
        i++
    }
    println(counter)
}
