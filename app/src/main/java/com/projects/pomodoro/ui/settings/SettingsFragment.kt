package com.projects.pomodoro.ui.settings

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.projects.pomodoro.R
import com.projects.pomodoro.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private lateinit var pomodoroBtn: Button
    private lateinit var shortBreakBtn: Button
    private lateinit var longBreakBtn: Button
    private lateinit var beforeLongBtn: Button
    private lateinit var startBreakAutoSwitch: Switch
    private lateinit var resetBtn: Button
    private var _binding: FragmentSettingsBinding? = null
    private lateinit var sharedPref: SharedPreferences

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this)[SettingsViewModel::class.java]

        // Get the shared preferences
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            ?: return inflater.inflate(R.layout.fragment_settings, container, false)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get the settings buttons and set the text to the current settings
        pomodoroBtn = binding.pomodoroBtn
        shortBreakBtn = binding.shortBreakBtn
        longBreakBtn = binding.longBreakBtn
        beforeLongBtn = binding.beforeLongBtn
        startBreakAutoSwitch = binding.startBreakAutoSwitch
        resetBtn = binding.resetBtn


        pomodoroBtn.text = sharedPref.getInt("pomodoro_time", 0).toString()
        shortBreakBtn.text = sharedPref.getInt("short_break_time", 0).toString()
        longBreakBtn.text = sharedPref.getInt("long_break_time", 0).toString()
        beforeLongBtn.text = sharedPref.getInt("before_long_break", 0).toString()
        startBreakAutoSwitch.isChecked = sharedPref.getBoolean("start_break_auto", false)

        // Set the pomodoro button to open an alert dialog to change the pomodoro time
        pomodoroBtnClick()
        shortBreakBtnClick()
        longBreakBtnClick()
        beforeLongBtnClick()
        startBreakAutoSwitchClick()
        resetBtnClick()

        return root
    }

    private fun pomodoroBtnClick() {
        pomodoroBtn.setOnClickListener {
            // Create an alert dialog
            val builder = AlertDialog.Builder(this.requireContext())
            builder.setTitle("Pomodoro")
            // Set a number picker for the pomodoro time
            val input = EditText(this.requireContext())
            input.inputType = InputType.TYPE_CLASS_NUMBER
            input.setText(sharedPref.getInt("pomodoro_time", 0).toString())
            builder.setView(input)

            // Set the OK button to save the new pomodoro time
            builder.setPositiveButton("OK") { dialog, which ->
                val text = input.text.toString()
                with(sharedPref.edit()) {
                    putInt("pomodoro_time", text.toInt())
                    apply()
                    android.widget.Toast.makeText(
                        context,
                        sharedPref.getInt("pomodoro_time", 0).toString(),
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                }
                // Set the pomodoro button text to the new pomodoro time
                pomodoroBtn.text = text
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }


    private fun shortBreakBtnClick() {
        shortBreakBtn.setOnClickListener {
            // Create an alert dialog
            val builder = AlertDialog.Builder(this.requireContext())
            builder.setTitle("Short Break")
            // Set a number picker for the pomodoro time
            val input = EditText(this.requireContext())
            input.inputType = InputType.TYPE_CLASS_NUMBER
            input.setText(sharedPref.getInt("short_break_time", 0).toString())
            builder.setView(input)

            // Set the OK button to save the new pomodoro time
            builder.setPositiveButton("OK") { dialog, which ->
                val text = input.text.toString()
                with(sharedPref.edit()) {
                    putInt("short_break_time", text.toInt())
                    apply()
                    android.widget.Toast.makeText(
                        context,
                        sharedPref.getInt("short_break_time", 0).toString(),
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                }
                // Set the pomodoro button text to the new pomodoro time
                shortBreakBtn.text = text
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun longBreakBtnClick() {
        longBreakBtn.setOnClickListener {
            // Create an alert dialog
            val builder = AlertDialog.Builder(this.requireContext())
            builder.setTitle("Long Break")
            // Set a number picker for the pomodoro time
            val input = EditText(this.requireContext())
            input.inputType = InputType.TYPE_CLASS_NUMBER
            input.setText(sharedPref.getInt("long_break_time", 0).toString())
            builder.setView(input)

            // Set the OK button to save the new pomodoro time
            builder.setPositiveButton("OK") { dialog, which ->
                val text = input.text.toString()
//                Toast.makeText(context,"Clicked, text entered: $text",Toast.LENGTH_LONG).show()
                with(sharedPref.edit()) {
                    putInt("long_break_time", text.toInt())
                    apply()
                    android.widget.Toast.makeText(
                        context,
                        sharedPref.getInt("long_break_time", 0).toString(),
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                }
                // Set the pomodoro button text to the new pomodoro time
                longBreakBtn.text = text
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun beforeLongBtnClick() {
        beforeLongBtn.setOnClickListener {
            // Create an alert dialog
            val builder = AlertDialog.Builder(this.requireContext())
            builder.setTitle("Before Long Break")
            // Set a number picker for the pomodoro time
            val input = EditText(this.requireContext())
            input.inputType = InputType.TYPE_CLASS_NUMBER
            input.setText(sharedPref.getInt("before_long_break", 0).toString())
            builder.setView(input)

            // Set the OK button to save the new pomodoro time
            builder.setPositiveButton("OK") { dialog, which ->
                val text = input.text.toString()
                with(sharedPref.edit()) {
                    putInt("before_long_break", text.toInt())
                    apply()
                    android.widget.Toast.makeText(
                        context,
                        sharedPref.getInt("before_long_break", 0).toString(),
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                }
                // Set the pomodoro button text to the new pomodoro time
                beforeLongBtn.text = text
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun startBreakAutoSwitchClick() {
        startBreakAutoSwitch.setOnClickListener {
            Toast.makeText(this.requireContext(), "Start break auto switch clicked", Toast.LENGTH_SHORT).show()
            with(sharedPref.edit()) {
                putBoolean("start_break_auto", startBreakAutoSwitch.isChecked)
                apply()
            }
        }
    }

    private fun resetBtnClick() {
        resetBtn.setOnClickListener {
            // reset pomodoros count
            with(sharedPref.edit()) {
                putInt("pomodoros", 0)
                apply()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}