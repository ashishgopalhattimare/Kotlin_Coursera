package nicestring

fun followLetters(str: String) : Boolean {
    for(i in 0 until str.length-1) {
        if(str[i] == str[i+1])
            return true
    }
    return false
}

fun String.isNice(): Boolean {
    var total = 0;

    if("b[uae]".toRegex().containsMatchIn(this) == false) total++;
    if(this.filter { it in "aeiou" }.toList().size >= 3) {
        total++;
    }
    if(followLetters(this)) total++;

    return total >= 2
}