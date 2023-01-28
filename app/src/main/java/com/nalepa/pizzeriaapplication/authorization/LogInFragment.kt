package com.nalepa.pizzeriaapplication.authorization


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.nalepa.pizzeriaapplication.BaseFragment
import com.nalepa.pizzeriaapplication.databinding.FragmentLogInBinding


class LogInFragment : BaseFragment() {
    private lateinit var binding: FragmentLogInBinding
    private val fbAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLoginClick()
        navigateToRegistration()

    }

    private fun navigateToRegistration() {
        binding.btnSignUp.setOnClickListener {
            val action = LogInFragmentDirections.actionLogInFragmentToRegistrationFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupLoginClick() {
        binding.btnSubmit.setOnClickListener {
            val email = binding.tfEmail.text.toString()
            val password = binding.tfPassword.text.toString()

            fbAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    if(it.user != null) {
                        startApp()
                    }
                }
                .addOnFailureListener {
                    Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                }
        }
    }

}