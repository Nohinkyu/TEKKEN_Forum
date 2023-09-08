package com.nik.tkforum.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
            signOut()
        }
        binding.tvSurvey.setOnClickListener {
            surveyClick()
        }

        binding.tvGetFrameInfo.setOnClickListener {
            getNewFrameData()
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
}