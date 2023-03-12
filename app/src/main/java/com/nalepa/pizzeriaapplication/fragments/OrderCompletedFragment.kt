package com.nalepa.pizzeriaapplication.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDeepLinkBuilder
import com.nalepa.pizzeriaapplication.BaseFragment
import com.nalepa.pizzeriaapplication.R
import com.nalepa.pizzeriaapplication.activities.MainActivity
import com.nalepa.pizzeriaapplication.databinding.FragmentOrderCompletedBinding


class OrderCompletedFragment : BaseFragment() {

    private lateinit var binding: FragmentOrderCompletedBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderCompletedBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonMainMenu.setOnClickListener {
            val intent = Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)
        }

        binding.buttonCloseApp.setOnClickListener {
            finishApp()
        }

    }
}