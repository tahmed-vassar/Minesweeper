package com.ait.minesweeper



object MinesweeperModel {

    data class Field(
        var type: Int = 0,
        var minesAround: Int = 0,
        var isFlagged: Boolean = false,
        var wasClicked: Boolean = false
    )


    var fieldMatrix: Array<Array<Field>> = Array(5){Array(5){Field()}}


    val size: Int = fieldMatrix[0].size

    var mineMatrix: Array<Pair<Int, Int>> = Array(3){Pair<Int, Int>(0,0)}

    private fun adjacencies(x: Int, y: Int): MutableList<Pair<Int, Int>> {
        var adjList: MutableList<Pair<Int, Int>> = mutableListOf()

        if (x > 0) adjList.add(Pair(x-1,y))

        if (x+1 < size) adjList.add(Pair(x+1, y))

        if (y > 0) adjList.add(Pair(x, y - 1))

        if (y+1 < size) adjList.add(Pair(x, y+1))

        return adjList

    }


    fun autofill(x: Int, y : Int) {
        var field = fieldMatrix[x][y]

        if (field.minesAround == 0) {

            var adj_list = adjacencies(x, y)

            for (i in adj_list.indices) {
                val j = adj_list[i].first
                val k = adj_list[i].second

                field = fieldMatrix[j][k]

                while (!field.wasClicked && !field.isFlagged && field.type != 1) {
                    field.wasClicked = true
                    autofill(j, k)
                }
            }
        }
    }




    fun createModel() {
        for (i in 0..4) {
            for (j in 0..4) {
                val field = Field()
                fieldMatrix[i][j] = field
            }
        }
        setMines()
        calcMinesAround()

    }





    private fun setMines(){

        var i = 0
        while(i < 3){
            val x = (0 until size-1).random()
            val y = (0 until size-1).random()

            if (fieldMatrix[x][y].type != 1){
                mineMatrix[i] = Pair(x, y)
                fieldMatrix[x][y].type = 1
                i++
            }
        }
    }




    private fun calcMinesAround(){
        for (i in 0..4){
            for (j in 0..4){
                for (k in mineMatrix.indices){
                    val x = mineMatrix[k].first
                    val y = mineMatrix[k].second

                    if (kotlin.math.abs(i - x) <= 1 && kotlin.math.abs(j - y) <= 1)
                        fieldMatrix[i][j].minesAround++

                }
            }
        }
    }


    fun resetModel() {
        createModel()
    }

    fun isGameWon(): Boolean{
        for (i in mineMatrix.indices){
            val x = mineMatrix[i].first
            val y = mineMatrix[i].second

            if (!fieldMatrix[x][y].isFlagged) return false
        }
        return true
    }


    fun isGameLost(): Boolean{
        for (i in mineMatrix.indices){
            val x = mineMatrix[i].first
            val y = mineMatrix[i].second

            if (fieldMatrix[x][y].wasClicked) return true

        }
        return false
    }



}

