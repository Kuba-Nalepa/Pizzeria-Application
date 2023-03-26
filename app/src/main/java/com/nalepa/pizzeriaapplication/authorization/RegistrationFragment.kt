package com.nalepa.pizzeriaapplication.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.nalepa.pizzeriaapplication.data.User
import com.nalepa.pizzeriaapplication.databinding.FragmentRegistrationBinding
import com.nalepa.pizzeriaapplication.viewmodel.SharedViewModel

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private val fbAuth = FirebaseAuth.getInstance()
    private val viewModel by activityViewModels<SharedViewModel>()

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

        val listOfInputs = listOf(
            Pair(binding.email, binding.emailContainer),
            Pair(binding.password, binding.passwordContainer),
            Pair(binding.confirmPassword, binding.confirmPasswordContainer)
        )

        listOfInputs.forEach {
            inputFocusListeners(it.first, it.second)
        }

        setupRegistrationCLick(listOfInputs)
    }

    private fun setupRegistrationCLick(listOfInputs: List<Pair<TextInputEditText, TextInputLayout>>) {
        binding.btnSubmit.setOnClickListener {

            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

            val isAnyInputEmpty = listOfInputs.any {
                it.first.text!!.isEmpty()
            }

            if(!isAnyInputEmpty) {
                if (password == confirmPassword) {
                    fbAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            if (it.user != null) {
                                val user = User(
                                    fbAuth.currentUser?.uid!!,
                                    "",
                                    "",
                                    fbAuth.currentUser?.email!!,
                                    ""
                                )
                                viewModel.createNewUser(user)
                                Snackbar.make(
                                    requireView(),
                                    "Successfully registered!",
                                    Snackbar.LENGTH_LONG
                                ).show()
                                findNavController().navigateUp()
                            }
                        }
                        .addOnFailureListener {
                            Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                                .show()
                        }
                }   else Snackbar.make(requireView(), "Password must be the same", Snackbar.LENGTH_LONG).show()
            }   else Snackbar.make(requireView(), "Please fill in all fields", Snackbar.LENGTH_LONG).show()

        }
    }

        private fun inputFocusListeners(
            editText: TextInputEditText,
            editTextLayout: TextInputLayout
        ) {
            binding.apply {
                editText.setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        editTextLayout.helperText = validInput(editText)
                    }
                }
            }
        }

    private fun validInput(editText: TextInputEditText): String? {
        binding.apply {
            if (editText.text.toString().isEmpty()) {
                return "Can not be empty"
            }
        }
        return null
    }
}