package com.nik.tkforum.ui.setting

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.FirebaseAuth
import com.nik.tkforum.R
import com.nik.tkforum.TekkenForumApplication
import com.nik.tkforum.databinding.FragmentSettingBinding
import com.nik.tkforum.ui.BaseFragment
import com.nik.tkforum.util.Constants

class SettingFragment : BaseFragment() {

    override val binding get() = _binding as FragmentSettingBinding
    override val layoutId: Int get() = R.layout.fragment_setting
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val preferencesManager = TekkenForumApplication.preferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayout()
        binding.tvLogout.setOnClickListener {
            signOut()
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
}