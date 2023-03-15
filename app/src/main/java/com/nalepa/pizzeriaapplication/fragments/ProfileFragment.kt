package com.nalepa.pizzeriaapplication.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.nalepa.pizzeriaapplication.BaseFragment
import com.nalepa.pizzeriaapplication.R
import com.nalepa.pizzeriaapplication.databinding.FragmentProfileBinding
import com.nalepa.pizzeriaapplication.viewmodel.SharedViewModel

class ProfileFragment : BaseFragment(), MenuProvider {
    private var fbAuth = FirebaseAuth.getInstance()
    private val viewModel by viewModels<SharedViewModel>()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this)

        setProfileDetails()
    }

    override fun onDestroyView() {
        activity?.removeMenuProvider(this)
        super.onDestroyView()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.logout_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return logoutFunction(menuItem)
    }

    private fun logoutFunction(menuItem: MenuItem): Boolean {
        if(menuItem.itemId == R.id.logoutAction) {
            viewModel.logout()
            logout()
            return true
        }
        return false
    }

    private fun setProfileDetails() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.emailTextView.text = user?.email
            if(user?.name == "") {
                binding.nameTextView.text = "Set name"
            } else binding.nameTextView.text = user?.name
            if(user.surname == "") {
                binding.surnameTextView.text = "Set surname"
            } else binding.surnameTextView.text = user?.surname
        }
    }

}