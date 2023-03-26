package com.nalepa.pizzeriaapplication.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.nalepa.pizzeriaapplication.BaseFragment
import com.nalepa.pizzeriaapplication.FavouritesAdapter
import com.nalepa.pizzeriaapplication.R
import com.nalepa.pizzeriaapplication.databinding.FragmentProfileBinding
import com.nalepa.pizzeriaapplication.viewmodel.SharedViewModel

class ProfileFragment : BaseFragment(), MenuProvider {

    private val viewModel by viewModels<SharedViewModel>()
    private lateinit var binding: FragmentProfileBinding
    private val favouritesAdapter = FavouritesAdapter()

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
        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewModel.uploadUserImage(uri!!)
            Toast.makeText(requireContext(),"Waiting for server", Toast.LENGTH_SHORT).show()
        }

        binding.avatarImage.setOnClickListener {
            checkGalleryPermission(getContent)
        }

        binding.setProfileInfoButton.setOnClickListener {
            updateUserName()
        }

        setProfileDetails()

        binding.favouritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favouritesRecyclerView.adapter = favouritesAdapter
        viewModel.favourites.observe(viewLifecycleOwner) { favouritesList ->

            Log.d("FAV", favouritesList.toString())
            favouritesAdapter.setFavouritePizzas(favouritesList)

        }
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
            binding.emailTextView.setText(user?.email)

            binding.nameTextView.setText(user?.name)
            binding.surnnameTextView.setText(user?.surname)

            if(user.image == "") {
                return@observe
            } else {
                Glide.with(requireContext())
                    .load(user.image)
                    .placeholder(R.drawable.loading_animation)
                    .into(binding.avatarImage)
            }
        }
    }

    private fun checkGalleryPermission(content: ActivityResultLauncher<String>) {
        Dexter
            .withContext(requireContext())
            .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    content.launch("image/*")
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(
                        requireContext(),
                        "Permission denied",
                        Toast.LENGTH_SHORT).show()

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

            }).onSameThread().check()
    }

    private fun updateUserName() {
        val name = binding.nameTextView.text.toString()
        val surname = binding.surnnameTextView.text.toString()
        viewModel.updateUserName(name, surname)
    }

}