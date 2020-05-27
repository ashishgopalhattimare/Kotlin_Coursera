package games

// Library functions looksing like in-build constructs

// Under the hood, lambda functions might be inlined
// How inline words

// Eg. run, let, takeIf, takeUnless, repeat

// Run the block of code (lambda) and return the last expression as result
var runFunc = run {
    println("Statement Execution...")
    1234
}

// Let function : is used to coping with nullable types Type?
fun getName(name : String?) : String? = name
fun printName(name : String?) {
    println("My name is $name")
}

fun main() {

//    println(runFunc)
//    println(runFunc)

    /*>>LET FUNCTIONS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
    val name : String? = "Ashish"

    // Approach 1
    if(getName(name) != null) { // Check whether it is null or not
        printName(name) // print name only if its not null
    }
    else println(">> name is null")

    // Approach 2 : Let allows to check the argument for being non-null,
    // not only the receiver
    getName(name)?.let {
        printName(it) // Execute only if the receiver is non-null
    }

    /*>>TAKEIF FUNCTIONS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
    // returns the receiver object if it satisfies the predicate
    // else returns null

    println("\nTAKEIF FUNCTION")
    val number = 11
    println(number.takeIf {
        // if false, returns null, else it value
        it > 10
    })

    /*>>TAKEUNLESS FUNCTIONS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
    // return the receiver object is it does not satisfies the predicate
    // else return null

    // INLINE FUNCTIONS
    /**
     * Inlining a functions mean that the compiler will substitute the body of the function
     * instead of calling it. If the inline function is inside a lambda function as an
     * argument and only call this lambda in set, then inlining will substitute this lambda
     * body
     */
    var list = listOf(0,1,2,0,4,5)
    println(filterNonZero(list))


    // SEQUENCES : If need to perform the extension functions like map, filter in lazy manner
    // It performs in vertical manner and are not triggered until 'terminal operations'
    // are called on the intermediate operations in the expression

    println("\nSEQUENCES\n")
    // yeilds
    var numbers = sequence {
        var x = 0
        println(x)
        while(true) {
            yield(x++)
        }
    }

    println(numbers.take(5).toList())


    println("\nLAMBDA WITH RECEIVERS\n")
    // Example of this are with

    var sb = StringBuffer()
    sb.appendln("Alphabet:")
    for(c in 'a'..'z') {
        sb.append(c)
    }
    sb.toString()

    var ssb = StringBuffer()
    with(ssb, {
        this.appendln("Alphabet:")
        for(c in 'a'..'z') {
            this.append(c)
        }
        this.toString()
    })
    println(ssb)
}

fun filterNonZero(list: List<Int>) = list/*this*/.filter{it != 0 /*predicate(element)*/}
inline fun<T> Iterable<T>.filter(predicate: (T) -> Boolean): List<T> {
    val result = ArrayList<T>()
    for(element in this) {
        if(predicate(element)) {
            result.add(element)
        }
    }
    return result
}