package com.ait.minesweeper

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class MinesweeperView(context: Context?, attrs: AttributeSet?) : View(context, attrs)  {

    private var paintBackGround: Paint = Paint()
    private var paintLine: Paint
    private var colors : Map<String, Int> = mapOf("0" to Color.WHITE ,"1" to Color.BLUE, "2" to Color.GREEN, "3" to Color.RED)
    private var paintText: Paint

    init {


        paintBackGround.color = Color.BLACK
        paintBackGround.style = Paint.Style.FILL

        paintLine = Paint()
        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f

        paintText = Paint()
        paintText.textSize = 100f


    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackGround)
        drawGameArea(canvas!!)
        drawFields(canvas!!)


    }


    private fun drawGameArea(canvas: Canvas){
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)

        val size = MinesweeperModel.size


        for (i in 1 until size){

            //horizontal lines
            canvas.drawLine(0f, (i * height / size).toFloat(), width.toFloat(),
                (i * height / size).toFloat(), paintLine)

            //vertical lines
            canvas.drawLine((i * width / size).toFloat(), 0f, (i * width / size).toFloat(), height.toFloat(),
                paintLine)
        }

    }





    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (MinesweeperModel.isGameWon()){
            (context as MainActivity).showMessage(R.string.won)
            return true

        }

        if (MinesweeperModel.isGameLost()){
            (context as MainActivity).showMessage(R.string.lost)
            return true
        }

        //if (MinesweeperModel.isGameWon() || MinesweeperModel.isGameLost()) return true

        if (event?.action == MotionEvent.ACTION_DOWN) {

            val tX = event.x.toInt() / (width / 5)
            val tY = event.y.toInt() / (height / 5)

            if ((context as MainActivity).isFlagMode() && !MinesweeperModel.fieldMatrix[tX][tY].wasClicked) {
                MinesweeperModel.fieldMatrix[tX][tY].isFlagged = true

            }
            else {
                if (tX < 5 && tY < 5 && !MinesweeperModel.fieldMatrix[tX][tY].wasClicked && !MinesweeperModel.fieldMatrix[tX][tY].isFlagged) {
                    MinesweeperModel.fieldMatrix[tX][tY].wasClicked = true
                    MinesweeperModel.autofill(tX,tY)

                }

            }

            invalidate()
        }

        return true
    }


    private fun drawFields(canvas: Canvas){
        for (i in 0..4){
            for (j in 0..4){
                if (MinesweeperModel.fieldMatrix[i][j].isFlagged){
                    val centerX = (i * width / 5 + width / 10).toFloat()
                    val centerY = (j * height / 5 + height / 10).toFloat()
                    val radius = height / 10 - 4

                    canvas.drawCircle(centerX, centerY, radius.toFloat(), paintLine)

                }

                else if (MinesweeperModel.fieldMatrix[i][j].wasClicked){

                    if (MinesweeperModel.fieldMatrix[i][j].type == 1){
                        canvas.drawLine((i * width / 5).toFloat(), (j * height / 5).toFloat(),
                            ((i + 1) *  width / 5).toFloat(),
                            ((j + 1) * height / 5).toFloat(), paintLine)
                        canvas.drawLine(((i + 1) * width / 5).toFloat(), (j * height / 5).toFloat(),
                            (i * width / 5).toFloat(), ((j + 1) * height / 5).toFloat(), paintLine)

                    }

                    else {
                        val number = MinesweeperModel.fieldMatrix[i][j].minesAround.toString()
                        val centerX = (i * width / 5 + width / 10).toFloat()
                        val centerY = (j * height / 5 + height / 10).toFloat()
                        var bounds = Rect()
                        paintText.color = colors[number]!!
                        paintText.getTextBounds(number, 0, number.length, bounds)
                        paintText.textAlign = Paint.Align.CENTER
                        canvas?.drawText(number, centerX, centerY + 25 , paintText)
                    }

                }

            }


        }


    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = View.MeasureSpec.getSize(widthMeasureSpec)
        val h = View.MeasureSpec.getSize(heightMeasureSpec)
        val d = if (w == 0) h else if (h == 0) w else if (w < h) w else h
        setMeasuredDimension(d, d)
    }



    fun resetGame() {
        MinesweeperModel.resetModel()
        invalidate()
    }




}