package com.projects.pomodoro.ui.pomodoro

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
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
    private var isPomodoro = false
    private var isShortBreak = false
    private var isLongBreak = false

    private var startBreakAuto = true

    private lateinit var startStopBtn: Button
    private lateinit var remainingView: TextView
    private lateinit var pomodorosView: TextView


    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPomodoroBinding.inflate(inflater, container, false)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return inflater.inflate(R.layout.fragment_settings, container, false)
        // Clear the shared preferences

        // Check if the shared preferences are empty, if so set them to the default values
        if (sharedPref.getInt("pomodoro_time", 0) == 0) {
            with(sharedPref.edit()) {
                putInt("pomodoro_time", 25)
                putInt("short_break_time", 5)
                putInt("long_break_time", 15)
                putInt("before_long_break", 4)
                putInt("pomodoros", 0)
                putBoolean("start_break_auto", true)
                apply()
            }
        }
        // Save all preferences to variables
        pomodoroTime = sharedPref.getInt("pomodoro_time", 0)
        shortBreakTime = sharedPref.getInt("short_break_time", 0)
        longBreakTime = sharedPref.getInt("long_break_time", 0)
        beforeLongBreak = sharedPref.getInt("before_long_break", 0)
        pomodoros = sharedPref.getInt("pomodoros", 0)
        startBreakAuto = sharedPref.getBoolean("start_break_auto", true)

        // Set the remaining time to the pomodoro time
        remainingTime = pomodoroTime

        // Set the remaining time text view to the pomodoro time
        remainingView = binding.timerView
        remainingView.text = pomodoroTime.toString()
        startStopBtn = binding.startStop
        pomodorosView = binding.pomodorosView
        pomodorosView.text = pomodoros.toString()

        startStopBtn.text = requireContext().applicationContext.getString(R.string.start)

        startStopBtn.setOnClickListener {
            if (timerIsRunning) {
                stopTimer()
            } else {
                if (!isPomodoro) {
                    startPomodoro()
                } else if (pomodoros % beforeLongBreak == 0) {
                    startLongBreak()
                } else {
                    startShortBreak()
                }
                startTimer(remainingTime)
            }
        }
        return binding.root
    }

    private fun updateUI(textView: TextView) {
        textView.text = remainingTime.toString()
    }


    // Add to startTimer as parameter the time of the stage
    private fun startTimer(stageTime: Int) {

        remainingTime = stageTime

        timer = object : CountDownTimer((remainingTime * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished.toInt() / 1000
                timerIsRunning = true
                updateUI(remainingView)
            }

            override fun onFinish() {
                remainingTime = 0
                timerIsRunning = false
                updateUI(remainingView)
                stopTimer()
                startStopBtn.text = requireContext().applicationContext.getString(R.string.start)
                if (isShortBreak || isLongBreak) {
                    remainingView.text = pomodoroTime.toString()
                } else if (isPomodoro && !startBreakAuto) {
                    if (pomodoros % beforeLongBreak == 0) {
                        remainingView.text = longBreakTime.toString()
                    } else {
                        remainingView.text = shortBreakTime.toString()
                    }
                }
                if (isPomodoro && startBreakAuto) {
                    if (pomodoros % beforeLongBreak == 0) {
                        startLongBreak()
                    } else {
                        startShortBreak()
                    }
                }
            }
        }.start()
        startStopBtn.text = requireContext().applicationContext.getString(R.string.stop)

    }

    private fun startPomodoro() {
        isPomodoro = true
        isShortBreak = false
        isLongBreak = false
        pomodoros++
        pomodorosView.text = pomodoros.toString()
        remainingTime = pomodoroTime
        startTimer(remainingTime+1)
    }

    private fun startShortBreak() {
        isPomodoro = false
        isShortBreak = true
        isLongBreak = false
        shortBreaks++
        remainingTime = shortBreakTime
        startTimer(remainingTime+1)
    }

    private fun startLongBreak() {
        isPomodoro = false
        isShortBreak = false
        isLongBreak = true
        longBreaks++
        remainingTime = longBreakTime
        startTimer(remainingTime+1)
    }

    private fun stopTimer() {
        timer?.cancel()
        timerIsRunning = false
        startStopBtn.text = requireContext().applicationContext.getString(R.string.start)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}