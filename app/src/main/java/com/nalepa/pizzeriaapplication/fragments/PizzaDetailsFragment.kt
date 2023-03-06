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
import com.nalepa.pizzeriaapplication.data.order.Item
import com.nalepa.pizzeriaapplication.data.pizza.Pizza
import com.nalepa.pizzeriaapplication.data.pizza.PizzaSize
import com.nalepa.pizzeriaapplication.databinding.FragmentPizzaDetailsBinding
import com.nalepa.pizzeriaapplication.viewmodel.SharedViewModel


class PizzaDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPizzaDetailsBinding
    private val viewModel by viewModels<SharedViewModel>()
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
        viewModel.retrievePizza(navigationArgs.id)

        viewModel.pizza.observe(viewLifecycleOwner) { pizza ->
            bindViews(pizza)

            binding.pizzaSize.setOnCheckedChangeListener { _, checkedId ->
                pizza.sizes.apply {
                    when(checkedId) {
                        binding.small.id -> {
                            viewModel.setPizzaSize(small)

                        }
                        binding.medium.id -> {
                            viewModel.setPizzaSize(medium)

                        }
                        binding.large.id -> {
                            viewModel.setPizzaSize(large)

                        }
                    }
                }
            }
            handleAddItemToCart()
        }

        viewModel.pizzaSize.observe(viewLifecycleOwner) {
            binding.pizzaPrice.text = String.format(getString(R.string.pizza_price), it.price)
            binding.pizzaDiameter.text = String.format(getString(R.string.pizza_diameter), it.diameter)

            handleAddItemToCart()
        }

        viewModel.quantity.observe(viewLifecycleOwner) {
            binding.quantity.text = viewModel.quantity.value.toString()

            handleAddItemToCart()
        }

        viewModel.price.observe(viewLifecycleOwner) {
            binding.pizzaPrice.text = String.format(getString(R.string.pizza_price), it)
        }

        binding.addQuantity.setOnClickListener {
            viewModel.increaseQuantity()
        }

        binding.subtractQuantity.setOnClickListener {
            viewModel.decreaseQuantity()
        }

        binding.addToCart.setOnClickListener {
            viewModel.apply {
                val item = Item(
                    id = pizza.value?.id!!,
                    name = pizza.value!!.name,
                    size = pizzaSize.value!!,
                    quantity = quantity.value!!,
                    totalPrice = price.value!!
                )

                addItemToCart(item)
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

    private fun handleAddItemToCart() {
        viewModel.apply {
            if (pizza.value != null && pizzaSize.value != null && quantity.value != null ) {
                binding.addToCart.isEnabled = quantity.value != 0 && pizzaSize.value != PizzaSize()
            }
        }
    }
}