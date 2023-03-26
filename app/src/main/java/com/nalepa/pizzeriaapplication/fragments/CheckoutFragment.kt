package com.nalepa.pizzeriaapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nalepa.pizzeriaapplication.data.order.OptionalInputs
import com.nalepa.pizzeriaapplication.data.order.Order
import com.nalepa.pizzeriaapplication.databinding.FragmentCheckoutBinding
import com.nalepa.pizzeriaapplication.viewmodel.SharedViewModel
import java.sql.Date

class CheckoutFragment : Fragment() {

    private lateinit var binding: FragmentCheckoutBinding
    private val viewModel by viewModels<SharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navBar =
            requireActivity().findViewById<BottomNavigationView>(com.nalepa.pizzeriaapplication.R.id.bottomNavigation)
        navBar.visibility = View.GONE

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCheckoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val listOfInputs = listOf(
                Pair(address, addressContainer),
                Pair(postalCode, postalCodeContainer),
                Pair(city, cityContainer),
            )
            listOfInputs.forEach {
                inputFocusListeners(it.first, it.second)
            }

            viewModel.userItems.observe(viewLifecycleOwner) { itemList ->
                    orderAndPay.setOnClickListener {
                        val isAnyInputEmpty = listOfInputs.any {
                            it.first.text!!.isEmpty()
                        }
                        if(isAnyInputEmpty) {
                            Toast.makeText(requireContext(),
                                "Please fill required fields in",
                                Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            val optionalInputs = OptionalInputs(
                                apartmentNumber = apartmentNumber.text.toString().toIntOrNull(),
                                floor = floor.text.toString().toIntOrNull(),
                                msg = msg.text.toString()
                            )
                            val order = Order(
                                date = Date(java.util.Date().time),
                                address = address.text.toString(),
                                postalCode = postalCode.text.toString(),
                                city = city.text.toString(),
                                optionalInputs = optionalInputs,
                                items = itemList
                            )

                            val action = CheckoutFragmentDirections.actionCheckoutFragmentToOrderCompletedFragment()
                            findNavController().navigate(action)

                            viewModel.createOrder(order)
                        }
                    }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val navBar = requireActivity().findViewById<BottomNavigationView>(com.nalepa.pizzeriaapplication.R.id.bottomNavigation)
        navBar.visibility = View.VISIBLE
    }

    private fun inputFocusListeners(editText: TextInputEditText, editTextLayout: TextInputLayout) {
        binding.apply {
            editText.setOnFocusChangeListener { _, hasFocus ->
                if(!hasFocus) {
                    editTextLayout.helperText = validInput(editText)
                }
            }
        }
    }

    private fun validInput(editText: TextInputEditText): String? {
        binding.apply {
            if(editText.text.toString().isEmpty()) {
                return "Required"
            }
        }
        return null
    }
}