package com.nik.tkforum.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.nik.tkforum.databinding.DialogWorkingProgressBinding
import com.nik.tkforum.ui.extensions.setDialogSize

class ProgressDialog : DialogFragment() {

    private var _binding: DialogWorkingProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogWorkingProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setOnKeyListener { dialog, keyCode, event ->
                return@setOnKeyListener keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP
            }
        }
    }

    override fun onResume() {
        super.onResume()
        context?.setDialogSize(this, 0.9f, 0.3f)
    }
}