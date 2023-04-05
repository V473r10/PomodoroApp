package com.projects.pomodoro.ui.pomodoro

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.projects.pomodoro.MainActivity
import com.projects.pomodoro.R
import com.projects.pomodoro.databinding.FragmentPomodoroBinding

class PomodoroFragment : Fragment() {

    private var _binding: FragmentPomodoroBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var pomodoroTime = 0
    private var shortBreakTime = 0
    private var longBreakTime = 0
    private var remainingTime = pomodoroTime
    private var timerIsRunning = false
    private var pomodoros = 0
    private var shortBreaks = 0
    private var longBreaks = 0
    private var beforeLongBreak = 0
    private var isPomodoro = true
    private var isShortBreak = false
    private var isLongBreak = false
    private var isPaused = false

    private var startBreakAuto = true

    private lateinit var screen: ConstraintLayout
    private lateinit var remainingView: TextView
    private lateinit var pomodorosView: TextView


    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPomodoroBinding.inflate(inflater, container, false)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            ?: return inflater.inflate(R.layout.fragment_settings, container, false)
        // Clear the shared preferences

        // Check if the shared preferences are empty, if so set them to the default values
        if (sharedPref.getInt("pomodoro_time", 0) == 0) {
            with(sharedPref.edit()) {
                putInt("pomodoro_time", 25)
                putInt("short_break_time", 5)
                putInt("long_break_time", 15)
                putInt("before_long_break", 4)
//                putInt("pomodoros", 0)
                putBoolean("start_break_auto", true)
                apply()
            }
        }
        with(sharedPref.edit()) {
            putInt("pomodoros", pomodoros)
        }
        // Save all preferences to variables
        pomodoroTime = sharedPref.getInt("pomodoro_time", 0)
        shortBreakTime = sharedPref.getInt("short_break_time", 0)
        longBreakTime = sharedPref.getInt("long_break_time", 0)
        beforeLongBreak = sharedPref.getInt("before_long_break", 0)
//        pomodoros = sharedPref.getInt("pomodoros", 0)
        startBreakAuto = sharedPref.getBoolean("start_break_auto", true)

        if (isPomodoro && remainingTime == 0)
            remainingTime = pomodoroTime

        // Set the remaining time to the pomodoro time
//        remainingTime = pomodoroTime

        // Set the remaining time text view to the pomodoro time

        remainingView = binding.timerView
        remainingView.text = remainingTime.toString()
        screen = binding.root
        pomodorosView = binding.pomodorosView
        pomodorosView.text = pomodoros.toString()

        screen.setOnClickListener {
            if (timerIsRunning) {
                stopTimer()
            } else {
                if (isPomodoro) {
                    startPomodoro()
                } else if (isShortBreak) {
                    startShortBreak()
                } else if (isLongBreak) {
                    startLongBreak()
                }
                isPaused = false

            }
        }

        screen.setOnLongClickListener {
            resetTimer()
            true
        }

        // Hide de action bar
        (activity as MainActivity).supportActionBar?.hide()

        return binding.root
    }

    private fun updateUI(textView: TextView) {
        textView.text = remainingTime.toString()
    }


    // Add to startTimer as parameter the time of the stage
    private fun startTimer(time: Int) {


        if (isPaused)
            remainingTime = time
        if (remainingTime == shortBreakTime || remainingTime == longBreakTime || remainingTime == pomodoroTime ) {
            remainingTime = time
        }


        timer = object : CountDownTimer((remainingTime * 1000).toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                if (timerIsRunning) {
                    remainingTime = millisUntilFinished.toInt() / 1000
                    timerIsRunning = true
                    updateUI(remainingView)
                } else
                    cancel()
            }


            override fun onFinish() {
                timerIsRunning = false
                updateUI(remainingView)
                stopTimer()
                isPaused = false

                if (remainingTime == 0) {

                    if (isShortBreak || isLongBreak) {
                        pomodoroTime.toString().also { remainingView.text = it }

                        isPomodoro = true
                        isShortBreak = false
                        isLongBreak = false
                    } else if (isPomodoro) {
                        if (pomodoros == beforeLongBreak + 1) {
                            longBreakTime.toString().also { remainingView.text = it }

                            isLongBreak = true
                            isPomodoro = false
                            if (startBreakAuto)
                                startLongBreak()
                        } else {
                            (this@PomodoroFragment.shortBreakTime +1).toString()
                                .also { remainingView.text = it }
                            updateUI(remainingView)
                            isShortBreak = true
                            isPomodoro = false
                            if (startBreakAuto)
                                startShortBreak()
                        }
                    }
                }

                if (isLongBreak)
                    resetCycle()
            }
        }.start()
    }

    private fun startPomodoro() {
        isPomodoro = true
        isShortBreak = false
        isLongBreak = false
        if (!isPaused)
            remainingTime = pomodoroTime
        if (remainingTime == pomodoroTime)
            pomodoros++
        pomodorosView.text = pomodoros.toString()
        timerIsRunning = true
        startTimer(remainingTime + 1)
        Toast.makeText(requireContext().applicationContext, "Pomodoro started", Toast.LENGTH_SHORT).show()
    }

    private fun startShortBreak() {
        isPomodoro = false
        isShortBreak = true
        isLongBreak = false
        shortBreaks++
        if (!isPaused || remainingTime == 0)
            remainingTime = shortBreakTime
        timerIsRunning = true
        startTimer(remainingTime + 1)
        Toast.makeText(requireContext().applicationContext, "Short break started", Toast.LENGTH_SHORT).show()
    }

    private fun startLongBreak() {
        isPomodoro = false
        isShortBreak = false
        isLongBreak = true
        longBreaks++
        if (!isPaused || remainingTime == 0)
            remainingTime = longBreakTime
        timerIsRunning = true
        startTimer(remainingTime + 1)
        Toast.makeText(requireContext().applicationContext, "Long break started", Toast.LENGTH_SHORT).show()
    }

    private fun stopTimer() {
        timer?.cancel()
        isPaused = true
        timerIsRunning = false
    }

    private fun resetCycle() {
        pomodoros = 0
        shortBreaks = 0
        longBreaks = 0
        pomodorosView.text = pomodoros.toString()
    }

    private fun resetTimer() {
        stopTimer()
        if (isPomodoro) {
            remainingTime = pomodoroTime
            pomodoros--
        } else if (isShortBreak) {
            remainingTime = shortBreakTime
        } else if (isLongBreak) {
            remainingTime = longBreakTime
        }
        updateUI(remainingView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}