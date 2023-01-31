package com.nalepa.pizzeriaapplication.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.nalepa.pizzeriaapplication.databinding.FragmentPizzaDetailsBinding
import com.nalepa.pizzeriaapplication.repository.FirebaseRepository
import com.nalepa.pizzeriaapplication.viewmodels.PizzaDetailsViewModel


class PizzaDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPizzaDetailsBinding
    private val pizzaDetailsViewModel by viewModels<PizzaDetailsViewModel>()
    private val navigationArgs: PizzaDetailsFragmentArgs by navArgs()
    private val repository = FirebaseRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPizzaDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pizzaDetailsViewModel.retrievePizzaDetails(navigationArgs.id)
        Log.d("pizza_details", pizzaDetailsViewModel.retrievePizzaDetails(navigationArgs.id).toString())
        pizzaDetailsViewModel.pizzaDetails.observe(viewLifecycleOwner) { pizza ->
            Log.d("DETAILS w srodku", navigationArgs.id)
            binding.apply {
                Glide.with(view).load(pizza.image).into(binding.pizzaImage)
                pizzaName.text = pizza.name
                pizzaPrice.text = "${pizza.sizes["small"]?.price.toString()} $"
                pizzaIngredients.text = pizza.ingredients.joinToString(", ")

                pizzaDiameter.text = "${pizza.sizes["small"]?.diameter.toString()} cm"
                small.text = pizza.sizes["small"]?.name
                medium.text = pizza.sizes["medium"]?.name
                large.text = pizza.sizes["large"]?.name

            }
        }
    }
}