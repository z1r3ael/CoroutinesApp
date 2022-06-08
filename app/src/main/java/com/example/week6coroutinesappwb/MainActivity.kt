package com.example.week6coroutinesappwb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.week6coroutinesappwb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val fragmentPiNumbers = PiNumbersFragment()
            val fragmentButtons = ButtonsFragment()

            supportFragmentManager
                .beginTransaction()
                .add(R.id.piNumbersFragmentContainer, fragmentPiNumbers)
                .add(R.id.buttonsFragmentContainer, fragmentButtons)
                .commit()
        }
    }
}