package com.nik.tkforum.ui.setting

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.nik.tkforum.R
import com.nik.tkforum.databinding.FragmentSettingBinding
import com.nik.tkforum.ui.BaseFragment
import com.nik.tkforum.ui.home.CharacterListViewModel
import com.nik.tkforum.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        isGetDataSuccess()
        setErrorMessage()
        binding.tvLogout.setOnClickListener {
            showLogoutDialog()
        }
        binding.tvSurvey.setOnClickListener {
            surveyClick()
        }

        binding.tvGetFrameInfo.setOnClickListener {
            getNewFrameDialog()
        }
        binding.tvNotificationPermission.setOnClickListener {
            requestNotificationPermission()
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
            setProgressDialog()
            dialog.dismiss()
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

    private fun isGetDataSuccess() {
        lifecycleScope.launch {
            characterListViewModel.isCharacterSave.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { isSuccess ->
                if (isSuccess) {
                    successDialog()
                }
            }
        }
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

    private fun setProgressDialog() {
        val action = SettingFragmentDirections.actionNavSettingToProgressDialog()
        lifecycleScope.launch {
            characterListViewModel.isReDownload.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { isLoading ->
                if (isLoading == true) {
                    findNavController().navigate(action)
                } else {
                    findNavController().navigateUp()
                }
            }
        }
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

    private fun setErrorMessage() {
        lifecycleScope.launch {
            characterListViewModel.isError.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
                .collect { isError ->
                    if (isError) {
                        Snackbar.make(
                            binding.root,
                            R.string.network_error_message,
                            Snackbar.LENGTH_LONG
                        ).setAction(R.string.close_snack_bar) {
                        }.show()
                    }
                }
        }
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        when (it) {
            true -> {
                Snackbar.make(
                    binding.root,
                    R.string.notification_permission_message,
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.close_snack_bar) {
                }.show()
            }

            false -> {
                Snackbar.make(
                    binding.root,
                    R.string.notification_permission_denied_message,
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.close_snack_bar) {
                }.show()
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}