package com.nik.tkforum.ui.home

import com.nik.tkforum.R
import com.nik.tkforum.databinding.FragmentHomeBinding
import com.nik.tkforum.ui.BaseFragment

class HomeFragment : BaseFragment() {

    override val binding get() = _binding as FragmentHomeBinding
    override val layoutId: Int get() = R.layout.fragment_home
}