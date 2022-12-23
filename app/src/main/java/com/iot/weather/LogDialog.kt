package com.iot.weather

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.iot.weather.databinding.FragmentLogDialogBinding


class LogDialog : DialogFragment() {

    private lateinit var binding: FragmentLogDialogBinding
    private val viewModel: LogViewModel by lazy {
        ViewModelProvider(this)[LogViewModel::class.java]
    }

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        viewModel.loadData()
        binding.viewModel = viewModel
        viewModel.status.observe(viewLifecycleOwner) {
            if (it == ReadingLogStatus.DONE) {
                Snackbar.make(requireView(), "${viewModel.log.value?.size} Readings", Snackbar.LENGTH_SHORT).setAction("dismiss") {}.show()
            }
        }
        binding.readingRecycler.adapter = ReadingAdapter()
    }
}