package com.example.week6coroutinesappwb

import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.week6coroutinesappwb.databinding.FragmentButtonsBinding
import kotlinx.coroutines.*
import java.util.*

class ButtonsFragment : Fragment() {
    private var isThreadRunning = false
    private lateinit var binding: FragmentButtonsBinding
    private var pauseOffset: Long = 0
    private var chronometer20secDelimiter = 1
    private var currentChronometerTime: Long = 0L
    private val random = Random()
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentButtonsBinding.inflate(inflater, container, false)

        binding.playButton.setOnClickListener { startCoroutineCalculation() }
        binding.pauseButton.setOnClickListener { pauseCoroutineCalculation() }
        binding.resetButton.setOnClickListener { resetCoroutineCalculation() }

        binding.coroutineChronometer.setOnChronometerTickListener {
            currentChronometerTime = SystemClock.elapsedRealtime() - binding.coroutineChronometer.base

            if (currentChronometerTime / chronometer20secDelimiter > 20000) {
                chronometer20secDelimiter++
                val randomColor: Int = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
                binding.layout.setBackgroundColor(randomColor)
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private fun startCoroutineCalculation() {
        isThreadRunning = true
        binding.playButton.isEnabled = false
        binding.resetButton.isEnabled = false

        if (isThreadRunning) {
            binding.coroutineChronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            binding.coroutineChronometer.start()
        }

        scope.launch(Dispatchers.IO) {
            var counter = 4 //расчет начинается с 4 символов числа ПИ (3.14 стоит в TextView по умолчанию)
            while (isThreadRunning) {
                val pi = PiCalculation.piSpigot(counter).toString()
                val piChar = pi.substring(pi.length - 1, pi.length)
                counter += 1
                parentFragmentManager.setFragmentResult(
                    "requestPiCharKey", bundleOf("PI_CHAR_KEY" to piChar)
                )
                delay(10)
            }
        }
    }

    private fun pauseCoroutineCalculation() {
        isThreadRunning = false
        binding.playButton.isEnabled = true
        binding.resetButton.isEnabled = true

        if (!isThreadRunning) {
            binding.coroutineChronometer.stop()
            pauseOffset = SystemClock.elapsedRealtime() - binding.coroutineChronometer.base
        }
    }

    private fun resetCoroutineCalculation() {
        isThreadRunning = false

        if (!isThreadRunning) {
            binding.coroutineChronometer.base = SystemClock.elapsedRealtime()
            pauseOffset = 0
            currentChronometerTime = 0L
            chronometer20secDelimiter = 1
            binding.coroutineChronometer.stop()
        }

        binding.playButton.isEnabled = true
        val isReset = true
        parentFragmentManager.setFragmentResult(
            "requestResultKey",
            bundleOf("RESET_KEY" to isReset)
        )
    }
}