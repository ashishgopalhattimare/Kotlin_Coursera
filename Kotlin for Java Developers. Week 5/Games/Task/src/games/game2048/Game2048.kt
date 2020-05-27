package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game).
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game running 'PlayGame2048'.
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
        Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        // Add two default values randomly
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

/*
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {

    initializer.nextValue(this)?.also {
        this.set(it.first, it.second)
    }
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" in a specified rowOrColumn only.
 * Use the helper function 'moveAndMergeEqual' (in Game2048Helper.kt).
 * The values should be moved to the beginning of the row (or column),
 * in the same manner as in the function 'moveAndMergeEqual'.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {

    // rowOrColumn = [(1,1), (1,2), (1,3), (1,4), (1,5), (1,6), (1,7)]
    val result = rowOrColumn.map {
        // get the value at that cell from the board using mapping
        this.get(it)
        // [2,null,4,null,2,2,8] like this
    }.moveAndMergeEqual {
        // merge condition when adjacent same found
        value -> value * 2
        // result = [2,4,4,8]
    }

    var modified = false
    if(result.isNotEmpty() && result.size != rowOrColumn.size) {
        modified = true

        // Reset all the values in the cell
        rowOrColumn.forEach { this[it] = null }

        // Update the first n values from the result list
        result.forEachIndexed { index, value ->
            this[rowOrColumn[index]] = value
        }
    }

    return modified
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" to the specified direction
 * following the rules of the 2048 game .
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */

// Modify Each Column of the Game
fun GameBoard<Int?>.colAlgo(rangeTo: IntRange): Boolean {

    var modified = false
    for(col in 1..width) {
        if(moveValuesInRowOrColumn(getColumn(rangeTo, col)))
            modified = true
    }
    return modified
}

// Modify Each Row of the Game
fun GameBoard<Int?>.rowAlgo(rangeTo: IntRange): Boolean {

    var modified = false
    for(row in 1..width) {
        if(moveValuesInRowOrColumn(getRow(row, rangeTo)))
            modified = true
    }
    return modified
}

fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {

    return when(direction) {
        // Depending on the direction get the rowOrColumn
        Direction.UP   -> colAlgo(1..width)
        Direction.DOWN -> colAlgo(width..1)
        Direction.RIGHT-> rowAlgo(width..1)
        Direction.LEFT -> rowAlgo(1..width)
    }
}