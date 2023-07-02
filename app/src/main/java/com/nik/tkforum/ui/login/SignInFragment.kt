package com.nik.tkforum.ui.login

import com.nik.tkforum.R
import com.nik.tkforum.databinding.FragmentLoginBinding
import com.nik.tkforum.ui.BaseFragment

class SignInFragment : BaseFragment() {

    override val binding get() = _binding as FragmentLoginBinding
    override val layoutId: Int get() = R.layout.fragment_login
}