package com.iot.weather

import android.app.Dialog
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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

    companion object {
        // new instance pattern for fragment
        @JvmStatic
        fun newInstance(userSettings: UserSettings) = SettingsDialog().apply {
            arguments = bundleOf("userSettings" to userSettings)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullscreenDialogTheme)
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor
    private lateinit var binding: FragmentSettingsDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userSettings = arguments?.getSerializable("userSettings") as UserSettings
        binding.apply {
            eMailEdit.setText(userSettings.email)
            temperatureThreshEdit.setText(userSettings.temperatureThreshold.toString())
            humidityThreshEdit.setText(userSettings.humidityThreshold.toString())
            pressureThreshEdit.setText(userSettings.pressureThreshold.toString())
            altitudeThreshEdit.setText(userSettings.altitudeThreshold.toString())
            eMailEdit.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.eMailEdit.text.toString()).matches()) {
                        binding.eMailLayout.error = "Please enter a valid email address"
                    } else {
                        binding.eMailLayout.error = null
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
            temperatureThreshEdit.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (isValid(temperatureThreshEdit, temperatureThreshLayout)) {
                        temperatureThreshLayout.error = null
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
            humidityThreshEdit.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (isValid(humidityThreshEdit, humidityThreshLayout)) {
                        humidityThreshLayout.error = null
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
            pressureThreshEdit.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (isValid(pressureThreshEdit, pressureThreshLayout)) {
                        pressureThreshLayout.error = null
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
            altitudeThreshEdit.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (isValid(altitudeThreshEdit, altitudeThreshLayout)) {
                        altitudeThreshLayout.error = null
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
        binding.saveButton.setOnClickListener {

            // check if any of the fields has an error and if so, don't save
            if (binding.eMailLayout.error != null || binding.temperatureThreshLayout.error != null || binding.humidityThreshLayout.error != null || binding.pressureThreshLayout.error != null || binding.altitudeThreshLayout.error != null) {
                return@setOnClickListener
            }

            val result = Bundle()
            val savedSettings = UserSettings(
                binding.eMailEdit.text.toString(),
                binding.pressureThreshEdit.text.toString().toDouble(),
                binding.temperatureThreshEdit.text.toString().toDouble(),
                binding.humidityThreshEdit.text.toString().toDouble(),
                binding.altitudeThreshEdit.text.toString().toDouble()
            )
            result.putSerializable("userSettings", savedSettings)
            parentFragmentManager.setFragmentResult("userSettings", result)
            sharedPreferences = requireActivity().getSharedPreferences(
                "sharedPreferences", AppCompatActivity.MODE_PRIVATE
            )
            editor = sharedPreferences.edit()
            editor.putString("E-mail", binding.eMailEdit.text.toString())
            editor.putString("Pressure", binding.pressureThreshEdit.text.toString())
            editor.putString("Temperature", binding.temperatureThreshEdit.text.toString())
            editor.putString("Humidity", binding.humidityThreshEdit.text.toString())
            editor.putString("Altitude", binding.altitudeThreshEdit.text.toString())
            editor.apply()

            val database = Firebase.database
            val myRef = database.getReference("settings")
            myRef.child("email").setValue(binding.eMailEdit.text.toString())
            myRef.child("pressureThreshold").setValue(binding.pressureThreshEdit.text.toString().toDouble())
            myRef.child("temperatureThreshold").setValue(binding.temperatureThreshEdit.text.toString().toDouble())
            myRef.child("humidityThreshold").setValue(binding.humidityThreshEdit.text.toString().toDouble())
            myRef.child("altitudeThreshold").setValue(binding.altitudeThreshEdit.text.toString().toDouble())

            dismiss()
        }
    }

    private fun isANumber(edit: TextInputEditText, layout: TextInputLayout): Boolean {
        try {
            edit.text.toString().toDouble()
        } catch (e: NumberFormatException) {
            layout.error = "Please enter a valid number"
            return false
        }
        return true
    }

    private fun isEmpty(edit: TextInputEditText, layout: TextInputLayout): Boolean {
        if (edit.text.toString().trim().isEmpty()) {
            layout.error = "Please enter a value"
            return true
        }
        return false
    }

    private fun isValid(edit: TextInputEditText, layout: TextInputLayout): Boolean {
        if (isANumber(edit, layout) && !isEmpty(edit, layout)) return true
        return false
    }
}