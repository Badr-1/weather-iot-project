package com.iot.weather

import android.app.Dialog
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.iot.weather.databinding.FragmentSettingsDialogBinding
import com.iot.weather.utils.UserSettings


class SettingsDialog : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        val window = dialog.window
        if (window != null) {
            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullscreenDialogTheme)
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor
    private lateinit var binding: FragmentSettingsDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveButton.setOnClickListener {
            val result = Bundle()
            val userSettings = UserSettings(
                binding.eMailEdit.text.toString(),
                binding.pressureThreshEdit.text.toString().toDouble(),
                binding.temperatureThreshEdit.text.toString().toDouble(),
                binding.humidityThreshEdit.text.toString().toDouble(),
                binding.altitudeThreshEdit.text.toString().toDouble()
            )
            result.putSerializable("userSettings", userSettings)
            parentFragmentManager.setFragmentResult("userSettings", result)
            sharedPreferences = requireActivity().getSharedPreferences(
                "sharedPreferences",
                AppCompatActivity.MODE_PRIVATE
            )
            editor = sharedPreferences.edit()
            editor.putString("E-mail", binding.eMailEdit.text.toString())
            editor.putString("Pressure", binding.pressureThreshEdit.text.toString())
            editor.putString("Temperature", binding.temperatureThreshEdit.text.toString())
            editor.putString("Humidity", binding.humidityThreshEdit.text.toString())
            editor.putString("Altitude", binding.altitudeThreshEdit.text.toString())
            editor.apply()
            dismiss()
        }
    }
}