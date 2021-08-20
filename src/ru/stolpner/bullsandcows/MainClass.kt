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

    val digits = pickRandomDigits()
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
    println("Enter anything if you already picked your 4 digits")
    readLine()
    println("OK, let's begin.")

    //let's now use THE BEST algorithm here
    //remember, I am just trying to learn Kotlin :)
    //we'll say at the beginning we have all the possible combinations
    //after each guess we will reduce the number of possible combinations according to player's response on bulls and cows
    //each guess is made random from all remaining possible combinations
    //we continue guessing until we have 1 option remaining
    //so what's to program?
    //1) cycle with exit conditions 3) reducing the possibilities

    var setOfOptions = createSetOfAllOptions()

    var isGameFinished = false
    while (!isGameFinished) {
        val guess = setOfOptions.random()
        println("My guess: ${guess.contentToString()}")
        println("Enter, how many bulls in my guess: ")
        val bulls = readLine()?.toIntOrNull()
        if (bulls == null || bulls !in (0..4)) {
            println("Incorrect input. Try again.")
            continue
        }
        println("Enter, how many cows in my guess: ")
        val cows = readLine()?.toIntOrNull()
        if (cows == null || cows !in (0..4) || bulls.plus(cows) > 4) {
            println("Incorrect input. Try again.")
            continue
        }

        setOfOptions = setOfOptions.filter { option -> isPossibleSolution(option, guess, bulls, cows)}.toMutableSet()
        println(setOfOptions.size)

        if (setOfOptions.size == 0) {
            println("Oops. I have now idea what your number could be :( " +
                    "Maybe you entered incorrect amount of bulls or cows at some point. Please try again.")
            isGameFinished = true
        }

        if (setOfOptions.size == 1) {
            println("I found your number!")
            println("Your number is: ${setOfOptions.first().contentToString()}")
            isGameFinished = true
        }
    }

    println("Nice game.")
    println()
}

//not pretty, but didn't want to write recursive algorithm
fun createSetOfAllOptions(): MutableSet<IntArray> {
    val setOfAllOptions = mutableSetOf<IntArray>()
    for (i in 0..9) {
        for (j in 0..9) {
            if (j == i) continue
            for (k in 0..9) {
                if (k == j || k == i) continue
                for (l in 0..9) {
                    if (l == k || l == j || l == i) continue
                    setOfAllOptions.add(intArrayOf(i, j, k, l))
                }
            }
        }
    }
    return setOfAllOptions
}

fun isPossibleSolution(option: IntArray, guess: IntArray, bulls: Int, cows: Int): Boolean {
    val (bullsFound, cowsFound) = checkBullsAndCows(guess, option)
    return bullsFound == bulls && cowsFound == cows
}

fun pickRandomDigits(): IntArray {
    val digits = intArrayOf(-1, -1, -1, -1)
    for (i in 0..3) {
        do {
            val pickedDigit = (0..9).random()
            digits[i] = pickedDigit
        } while (digits.count { it == pickedDigit } > 1)
    }
    return digits
}

fun checkBullsAndCows(
    pickedDigits: IntArray,
    guessedDigits: IntArray
): CheckResult {
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