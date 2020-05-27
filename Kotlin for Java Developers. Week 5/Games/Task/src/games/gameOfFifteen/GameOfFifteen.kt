package games.gameOfFifteen

import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game = CreateFifteenGame(initializer)

class CreateFifteenGame(var initializer: GameOfFifteenInitializer) : Game {

    private val Matrix = createGameBoard<Int?>(4)
    override fun initialize() {
        Matrix.setup(initializer)
    }

    override fun canMove(): Boolean = true

    override fun hasWon(): Boolean {
        var count = 1
        return Matrix.all {
            it == null || it == count++
        }
    }

    override fun processMove(direction: Direction) {
        with(Matrix) {
            // find the Cell with null value
            find {
                it == null
            }?.also {cell ->
                // If the cell with null value is found
                cell.getNeighbour(direction.reversed())
                    // Neighbour found
                    ?.also { neighbour ->
                        // Move the neighour to reverse direction
                        this[cell] = this[neighbour]
                        this[neighbour] = null
                    }
            }
        }
    }

    override fun get(i: Int, j: Int): Int? {
        return Matrix.run {
            this[getCell(i, j)]
        }
    }

    private fun GameBoard<Int?>.setup(initializer: GameOfFifteenInitializer) : Unit {

        initializer.initialPermutation.forEachIndexed { index, value ->
            val row = index/width+1; val col = index%width+1
            val cell = getCell(row, col)
            this[cell] = value
        }
    }
}