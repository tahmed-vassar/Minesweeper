package com.ait.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ait.minesweeper.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MinesweeperModel.createModel()

        showMessage(R.string.instructions)

        binding.btnStart.setOnClickListener(){
            binding.MinesweeperView.resetGame()
        }


    }


    fun showMessage(msg: Int) {
        Snackbar.make(binding.root, getString(msg), Snackbar.LENGTH_LONG).show()
    }


    fun isFlagMode() : Boolean = binding.tgbtnIsFlag.isChecked
}