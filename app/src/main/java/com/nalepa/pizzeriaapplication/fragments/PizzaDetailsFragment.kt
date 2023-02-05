package com.nalepa.pizzeriaapplication.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.nalepa.pizzeriaapplication.R
import com.nalepa.pizzeriaapplication.data.order.OrderItem
import com.nalepa.pizzeriaapplication.data.pizza.Pizza
import com.nalepa.pizzeriaapplication.data.pizza.PizzaSize
import com.nalepa.pizzeriaapplication.databinding.FragmentPizzaDetailsBinding
import com.nalepa.pizzeriaapplication.repository.FirebaseRepository
import com.nalepa.pizzeriaapplication.viewmodels.PizzaDetailsViewModel


class PizzaDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPizzaDetailsBinding
    private val pizzaDetailsViewModel by viewModels<PizzaDetailsViewModel>()
    private val navigationArgs: PizzaDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPizzaDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pizzaDetailsViewModel.retrievePizza(navigationArgs.id)

        pizzaDetailsViewModel.pizza.observe(viewLifecycleOwner) { pizza ->
            bindViews(pizza)

            binding.pizzaSize.setOnCheckedChangeListener { _, checkedId ->
                pizza.sizes.apply {
                    when(checkedId) {
                        binding.small.id -> {
                            pizzaDetailsViewModel.setPizzaSize(small)

                        }
                        binding.medium.id -> {
                            pizzaDetailsViewModel.setPizzaSize(medium)

                        }
                        binding.large.id -> {
                            pizzaDetailsViewModel.setPizzaSize(large)

                        }
                    }
                }
            }
            handleCreateOrder()
        }

        pizzaDetailsViewModel.pizzaSize.observe(viewLifecycleOwner) {
            binding.pizzaPrice.text = String.format(getString(R.string.pizza_price), it.price)
            binding.pizzaDiameter.text = String.format(getString(R.string.pizza_diameter), it.diameter)

            handleCreateOrder()
        }

        pizzaDetailsViewModel.quantity.observe(viewLifecycleOwner) {
            binding.quantity.text = pizzaDetailsViewModel.quantity.value.toString()

            handleCreateOrder()
        }

        binding.addQuantity.setOnClickListener {
            pizzaDetailsViewModel.increaseQuantity()
        }

        binding.subtractQuantity.setOnClickListener {
            pizzaDetailsViewModel.decreaseQuantity()
        }

        binding.addToCart.setOnClickListener {
            pizzaDetailsViewModel.apply {
                val createOrder = OrderItem(pizza.value!!, pizzaSize.value!!, quantity.value!!)
            // take order
            }
        }
    }

    private fun bindViews(pizza: Pizza) {
        binding.apply {
            Glide.with(requireView()).load(pizza.image).into(binding.pizzaImage)
            pizzaName.text = pizza.name
            small.text = pizza.sizes.small.name
            medium.text = pizza.sizes.medium.name
            large.text = pizza.sizes.large.name
            pizzaIngredients.text = pizza.ingredients.joinToString(", ")
        }
    }

    private fun handleCreateOrder() {
        pizzaDetailsViewModel.apply {
            if (pizza.value != null && pizzaSize.value != null && quantity.value != null ) {
                binding.addToCart.isEnabled = quantity.value != 0 && pizzaSize.value != PizzaSize()
            }
        }
    }
}