package com.nik.tkforum.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.FirebaseAuth
import com.nik.tkforum.R
import com.nik.tkforum.data.source.local.PreferenceManager
import com.nik.tkforum.databinding.FragmentSettingBinding
import com.nik.tkforum.ui.BaseFragment
import com.nik.tkforum.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : BaseFragment() {

    override val binding get() = _binding as FragmentSettingBinding
    override val layoutId: Int get() = R.layout.fragment_setting
    private val firebaseAuth = FirebaseAuth.getInstance()

    @Inject
    lateinit var preferencesManager: PreferenceManager

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
            getFrameInfo()
        }
    }

    private fun signOut() {
        firebaseAuth.signOut()
        with(preferencesManager) {
            clear()
        }
        findNavController().navigate(SettingFragmentDirections.actionNavSettingToNavLogin())
    }

    private fun setLayout() {
        with(preferencesManager) {
            binding.tvMail.text = getString(Constants.KEY_MAIL_ADDRESS, "")
            binding.tvNickname.text = getString(Constants.KEY_NICKNAME, "")
            binding.ivProfileImage.load(getString(Constants.KEY_PROFILE_IMAGE, "")) {
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun surveyClick() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.SURVEY_URI))
        startActivity(intent)
    }

    private fun getFrameInfo() {
        val dbFile = requireContext().getDatabasePath(Constants.CHARACTER_DATA_BASE)
        if (dbFile.exists()) {
            dbFile.delete()
        }
    }
}