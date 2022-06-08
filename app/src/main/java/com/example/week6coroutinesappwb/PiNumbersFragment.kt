package com.example.week6coroutinesappwb

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week6coroutinesappwb.databinding.FragmentPiNumbersBinding
import kotlinx.coroutines.*


class PiNumbersFragment : Fragment() {

    private lateinit var binding: FragmentPiNumbersBinding
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPiNumbersBinding.inflate(inflater, container, false)
        binding.piTextView.text = "3.14"

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener("requestPiCharKey", requireActivity()) { requestPiCharKey, bundle ->
            val resultPiChar = bundle.getString("PI_CHAR_KEY")

            scope.launch {
                withContext(Dispatchers.Main) {
                    binding.piTextView.append(resultPiChar)
                    binding.scrollView.fullScroll(View.FOCUS_DOWN)
                }
            }
        }

        parentFragmentManager.setFragmentResultListener("requestResultKey", requireActivity()) { requestResultKey, bundle ->
            val isReset = bundle.getBoolean("RESET_KEY")

            scope.launch {
                if (isReset)
                    binding.piTextView.text = "3.14"
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}