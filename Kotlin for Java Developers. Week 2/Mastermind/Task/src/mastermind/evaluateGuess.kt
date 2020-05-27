package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {

    var secretAr = IntArray(26, {i->0})
    var  guessAr = IntArray(26, {i->0})

    for(element in secret) secretAr[element -'A']++

    var rightPosition = 0
    for(i in guess.indices) {
        if(secret[i] == guess[i])
        {
            secretAr[guess[i]-'A']--
            rightPosition++
        }
        else {
            guessAr[guess[i]-'A']++
        }
    }

    var wrongPosition = 0
    for(i in 0 until 26) {
        wrongPosition += Math.min(secretAr[i], guessAr[i])
    }

    return Evaluation(rightPosition, wrongPosition)
}