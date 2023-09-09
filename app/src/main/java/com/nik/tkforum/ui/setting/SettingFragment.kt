package com.nik.tkforum.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.nik.tkforum.R
import com.nik.tkforum.databinding.FragmentSettingBinding
import com.nik.tkforum.ui.BaseFragment
import com.nik.tkforum.ui.home.CharacterListViewModel
import com.nik.tkforum.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment() {

    override val binding get() = _binding as FragmentSettingBinding
    override val layoutId: Int get() = R.layout.fragment_setting
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val characterListViewModel: CharacterListViewModel by viewModels()

    private val settingViewModel: SettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        binding.tvLogout.setOnClickListener {
            showLogoutDialog()
        }
        binding.tvSurvey.setOnClickListener {
            surveyClick()
        }

        binding.tvGetFrameInfo.setOnClickListener {
            getNewFrameDialog()
        }
    }

    private fun signOut() {
        firebaseAuth.signOut()
        settingViewModel.deleteUserInfo()
        findNavController().navigate(SettingFragmentDirections.actionNavSettingToNavLogin())
    }

    private fun setLayout() {
        binding.userInfo = settingViewModel.userInfo
        setSwitchListener()
    }

    private fun surveyClick() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.SURVEY_URI))
        startActivity(intent)
    }

    private fun getNewFrameData() {
        characterListViewModel.reDownLoadCharacterList()
    }

    private fun setSwitchListener() {
        binding.swCheckSeries.isChecked = settingViewModel.isSwitchCheck.isNotBlank()

        binding.swCheckSeries.setOnCheckedChangeListener { compoundButton, onSwitch ->
            if (onSwitch) {
                settingViewModel.clickSwitch()
            } else {
                settingViewModel.unCheckSwitch()
            }
        }
    }

    private fun getNewFrameDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)

        builder.setTitle(R.string.dialog_get_new_frame_data_title)
        builder.setPositiveButton(R.string.dialog_positive) { dialog, _ ->
            getNewFrameData()
            dialog.dismiss()
            successDialog()
        }
        builder.setNegativeButton(R.string.dialog_negative) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showLogoutDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)

        builder.setTitle(R.string.dialog_setting_logout_title)
        builder.setMessage(R.string.dialog_setting_logout_message)
        builder.setPositiveButton(R.string.dialog_positive) { dialog, _ ->
            signOut()
            dialog.dismiss()
        }
        builder.setNegativeButton(R.string.dialog_negative) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun successDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        builder.setTitle(R.string.dialog_success_get_frame_data_title)
        builder.setMessage(R.string.dialog_success_get_frame_data_message)
        builder.setPositiveButton(R.string.dialog_positive) { dialog, _ ->
            dialog.dismiss()
            restartApp()
        }
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun restartApp() {
        val intent =
            requireActivity().packageManager.getLaunchIntentForPackage(requireActivity().packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        if (intent != null) {
            startActivity(intent)
        }
        requireActivity().finish()
    }
}