package com.nalepa.pizzeriaapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nalepa.pizzeriaapplication.MenuAdapter
import com.nalepa.pizzeriaapplication.OnPizzaItemClick
import com.nalepa.pizzeriaapplication.data.pizza.Pizza
import com.nalepa.pizzeriaapplication.databinding.FragmentHomeBinding
import com.nalepa.pizzeriaapplication.viewmodels.HomeViewModel

class HomeFragment : Fragment(), OnPizzaItemClick {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()
    private val myAdapter = MenuAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = myAdapter
        }
        homeViewModel.menu.observe(viewLifecycleOwner) { menuList ->
            myAdapter.setPizzaMenu(menuList)

        }
    }

    override fun onPizzaClick(pizza: Pizza, position: Int) {
        val pizzaPath = pizza.id
        val action = HomeFragmentDirections.actionHomeFragmentToPizzaDetailsFragment(pizzaPath!!)
        findNavController().navigate(action)

    }
}