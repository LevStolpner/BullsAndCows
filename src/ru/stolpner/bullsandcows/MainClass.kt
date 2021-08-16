package ru.stolpner.bullsandcows

fun main() {
    startGame()
}

fun startGame() {
    println("Let's play Bulls and Cows!")
    println()
    while (true) {
        startPlayerGuessingGame()
        startComputerGuessingGame()
    }
}

fun startPlayerGuessingGame() {
    println("Now you can try to guess my number.")

    val digits = pickDigits()
    println("I picked a 4-digit number.")
    println()

    var isGameFinished = false
    while (!isGameFinished) {
        print("Your guess: ")
        val input = readLine() ?: ""
        val chars = input.toCharArray()
        if (chars.size != 4 || chars.any { c -> !c.isDigit() }
                || chars.any { c -> chars.count { it == c } > 1 }) {
            println("Incorrect input. Try again.")
            continue
        }
        val (bulls, cows) = checkBullsAndCows(digits, chars.map { c -> c.digitToInt() }.toIntArray())
        println("Result: bulls=${bulls}, cows=${cows}")
        if (bulls == 4) {
            isGameFinished = true
        }
    }
    println("Nice game.")
    println()
}

fun startComputerGuessingGame() {
    println("Now you can pick a number and I will try to guess it.")
    print("Enter anything if you already picked your 4 digits")
    readLine()

    println("Nice game.")
    println()
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
                      guessedDigits: IntArray): CheckResult {
    var bullsCount = 0
    var cowsCount = 0

    for (i in 0..3) {
        if (guessedDigits[i] == pickedDigits[i]) {
            bullsCount++
            continue
        }
        if (pickedDigits.any { d -> d == guessedDigits[i] }) {
            cowsCount++
        }
    }

    return CheckResult(bullsCount, cowsCount)
}

data class CheckResult(val bulls: Int, val cows: Int)