package board

import board.Direction.*
import java.lang.IllegalArgumentException

fun createSquareBoard(width: Int): SquareBoard = GenerateSquareBoard(width)

fun <T> createGameBoard(width: Int): GameBoard<T> = GenerateGameBoard(width)

open class GenerateSquareBoard(override val width: Int) : SquareBoard {

    private val matrix = (1..width).flatMap { i ->
        (1..width).map { j ->
            Cell(i, j)
        }
    } // Create a 1-D array of all the values, and then use filter and other functions over it

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return getAllCells().firstOrNull {
            it == Cell(i, j)
        }
    }

    override fun getCell(i: Int, j: Int): Cell {
        return getCellOrNull(i, j) ?: throw IllegalArgumentException()
    }

    override fun getAllCells(): Collection<Cell> = matrix

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {

        var start = jRange.first; var end = jRange.last
        if(jRange.first > jRange.last) {
            end = jRange.first; start = jRange.last
        }

        val result = matrix.filter { i == it.i && it.j in start..end }

        if(jRange.first > jRange.last) return result.reversed()
        return result
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {

        var start = iRange.first; var end = iRange.last
        if(iRange.first > iRange.last) {
            end = iRange.first; start = iRange.last
        }

        val result = matrix.filter { j == it.j && it.i in start..end }

        if(iRange.first > iRange.last) return result.reversed()
        return result
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {

        var fx = 0; var fy = 0
        when(direction) {
            UP  -> fy = -1; DOWN -> fy = 1
            LEFT-> fx = -1; else -> fx = 1
        }
        return getCellOrNull(this.i+fy, this.j+fx)
    }
}

class GenerateGameBoard<T>(width: Int) : GenerateSquareBoard(width), GameBoard<T> {

    private val Matrix = getAllCells().map {
        it to null
    }.toMap<Cell, T?>().toMutableMap()

    override operator fun get(cell: Cell): T? = Matrix[cell]
    override operator fun set(cell: Cell, value: T?) {
        Matrix[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
            Matrix.entries.filter {
                predicate(it.value) // match for the condition, and return the Pair<Cell, T?>
            }.map /*Filter the map with keys only*/{ it.key }

    override fun find(predicate: (T?) -> Boolean): Cell? =
            Matrix.entries.find {
                predicate(it.value)
            }?.key

    override fun any(predicate: (T?) -> Boolean): Boolean =
            Matrix.entries.any {
                predicate(it.value)
            }

    override fun all(predicate: (T?) -> Boolean): Boolean =
            Matrix.entries.all {
                predicate(it.value)
            }
}