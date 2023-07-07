package com.nik.tkforum.ui.setting

import com.nik.tkforum.R
import com.nik.tkforum.databinding.FragmentSettingBinding
import com.nik.tkforum.ui.BaseFragment

class FragmentSetting : BaseFragment() {

    override val binding get() = _binding as FragmentSettingBinding
    override val layoutId: Int get() = R.layout.fragment_setting
}