package com.nalepa.pizzeriaapplication.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.nalepa.pizzeriaapplication.data.User
import com.nalepa.pizzeriaapplication.databinding.FragmentRegistrationBinding
import com.nalepa.pizzeriaapplication.viewmodels.RegistrationViewModel

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private val fbAuth = FirebaseAuth.getInstance()
    private val registrationViewModel by activityViewModels<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRegistrationCLick()
    }

    private fun setupRegistrationCLick() {
       binding.btnSubmit.setOnClickListener {
           val email = binding.tfEmail.text.toString()
           val password = binding.tfPassword.text.toString()
           val confirmPassword = binding.tfConfirmPassword.text.toString()

           if(password == confirmPassword) {
               fbAuth.createUserWithEmailAndPassword(email, password)
                   .addOnSuccessListener {

                       if(it.user != null) {
                           val user = User(
                               fbAuth.currentUser?.uid!!,
                               "",
                               "",
                               fbAuth.currentUser?.email!!,
                               listOf())
                           registrationViewModel.createNewUser(user)
                           Snackbar.make(requireView(),"Successfully registered!", Snackbar.LENGTH_LONG).show()
                           findNavController().navigateUp()
                       }
                   }
                   .addOnFailureListener {
                       Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                   }
           }
       }
    }
}