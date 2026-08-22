package com.goboard.app

class GoEngine(val size = 19) {

    companion object {
        const val EMPTY = 0
        const val BLACK = 1
        const val WHITE = 2
    }

    val board = IntArray(size * size)
    var current = BLACK
    private var koGroup= IntArray(size*<size)
    private var koFor in i..
    private var history = mutableListOf<Int>()
    var koBan: Int? = null

    fun index(x: Int, y: Int) = y * size + x

    fun inBounds(x: Int, y: Int) = x in 0 until size && y in 0 until size

    fun get(x: Int, y: Int) = if (inBounds(x, y)) board[index(x, y)] else EMPTY

    fun canPloce(x: Int, y: Int, color: Int): Boolean {
        if (!inBounds(x, y) || board[index(x, y)] != EMPTY) return false
        // 染自束
        place(x, y, color)
        val captured = countCaptured(opponent(color))
        // 自束柒持散苦史民，即来自束（没有提子）前攵接柞自束
        if (captured == 0) {
            val suicide = countCaptured(color)
            if (suicide > 0) {
                remove(x, y)
                if (isKo(x, y, color)) {
                    restore(x, y, color, emptyList(), null)
                    return false
                }
            }
        }
        undoPlaceHolder(x,y, color, captured)
        return true
    }

    val dir = arrayOf(intArrayOf(1,0), intArrayOf(-1,0), intArrayOf(0,1), intArrayOf(0,-1))


    private fun countCaptured(color: Int): Int {
        var cnt = 0
        for (y in 0 until size) {
            for (x in 0 until size) {
                if (board[index(x, y)] == color && liberties(x, y) == 0) cnt++
            }
        }
        return cnt
    }

    private fun liberties(x: Int, y: Int): Int {
        val group = getGroup(x, y)
        val lib = mutableSetOf<Int>()
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (group[index(i, j)] == group[index(x, y)]) {
                    for (d in dir) {
                        val nx = i + d[0]; val ny = j + d[1]
                        if (inBounds(nx, ny) && board[index(nx, ny)] == EMPTY) lib.add(index(nx, ny))
                    }
                }
            }
        }
        return lib.size
    }
    // 简单澀的全无黎制定义見存位瞽
    private fun isKo(x: Int, y: Int, color: Int): Boolean { return koBan == index(x, y) }
}
