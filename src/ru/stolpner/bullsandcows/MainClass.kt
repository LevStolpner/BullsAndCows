package ru.stolpner.bullsandcows

fun main() {
    startGame()
}

fun startGame() {
    println("Let's play Bulls and Cows!")
    println("First you can try to guess my number")
    startPlayerGuessingGame()

}

fun startPlayerGuessingGame() {
    val digits = pickDigits()
    println("I picked a 4-digit number.")

    var isGameFinished = false
    while (!isGameFinished) {
        print("Your guess: ")
        val input = readLine() ?: ""
        val chars = input.toCharArray()
        if (chars.size != 4 || chars.any { c -> !c.isDigit() }
                || chars.any { c -> chars.count { it == c } > 1 }) {
            println("Incorrect input. Try again.")
        }
        checkBullsAndCows(digits, chars.map { c -> c.digitToInt() }.toIntArray())

    }
}

fun pickDigits(): IntArray {
    val digits = intArrayOf(-1, -1, -1, -1)
    for (i in 0..3) {
        do {
            val pickedDigit = (0..9).random()
            digits[i] = pickedDigit
        } while (digits.count { it == pickedDigit } > 1)
    }
    return digits
}

fun checkBullsAndCows(pickedDigits: IntArray,
                      guessedDigits: IntArray) {

}

fun startComputerGuessingGame() {

}