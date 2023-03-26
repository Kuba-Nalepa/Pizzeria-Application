package com.nalepa.pizzeriaapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nalepa.pizzeriaapplication.OnOrderClicked
import com.nalepa.pizzeriaapplication.OrderAdapter
import com.nalepa.pizzeriaapplication.R
import com.nalepa.pizzeriaapplication.data.order.Item
import com.nalepa.pizzeriaapplication.databinding.FragmentCartBinding
import com.nalepa.pizzeriaapplication.viewmodel.SharedViewModel

class CartFragment : Fragment(), OnOrderClicked {

    private lateinit var binding: FragmentCartBinding
    private val viewModel by viewModels<SharedViewModel>()
    private val myAdapter = OrderAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userItems.observe(viewLifecycleOwner) { itemList ->
            setProperLayout(itemList)
            myAdapter.setCurrentUserOrder(itemList)

            calculatePrice(itemList)

            binding.makeOrder.setOnClickListener {

                val action = CartFragmentDirections.actionCartFragmentToCheckoutFragment()
                findNavController().navigate(action)
            }
        }

        binding.apply {
            orderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            orderRecyclerView.adapter = myAdapter
        }

    }

    override fun addQuantity(orderItem: Item) {
        orderItem.quantity += 1
        orderItem.totalPrice = orderItem.size.price * orderItem.quantity
        viewModel.updateOrderDetails(orderItem)
    }

    override fun delQuantity(orderItem: Item) {
        if(orderItem.quantity != 1) {
            orderItem.quantity -= 1
            orderItem.totalPrice = orderItem.size.price * orderItem.quantity
            viewModel.updateOrderDetails(orderItem)
        } else {
            Toast.makeText(requireContext(),"You can't decrease quantity to 0", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDeleteClick(orderItem: Item) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete")
            .setMessage("Are you sure You want to delete this item in your order?")
            .setPositiveButton("Delete") {_,_ ->
                viewModel.deleteOrder(orderItem)
                Toast.makeText(requireContext(), "Deleted successfully!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Back") {_,_ ->}
            .show()
    }

    private fun calculatePrice(itemList: List<Item>) {
        val deliveryPrice = 10.0
        var sum = 0.0

        binding.delevieryPrice.text = String.format(getString(R.string.pizza_price), deliveryPrice)

        itemList.forEach{
            sum +=  it.totalPrice
            binding.sum.text = String.format(getString(R.string.pizza_price), sum)

        }
        val totalCost = sum + deliveryPrice
        binding.totalCost.text = String.format(getString(R.string.pizza_price), totalCost)

    }

    private fun setProperLayout(itemList: List<Item>) {
        binding.apply {
            if(itemList.isEmpty()) {
                linearLayoutOutside.visibility = View.GONE
                emptyCartImage.visibility = View.VISIBLE
                emptyCartText.visibility = View.VISIBLE
                makeOrder.isEnabled = false
            }   else {
                emptyCartImage.visibility = View.INVISIBLE
                emptyCartText.visibility = View.INVISIBLE
                makeOrder.isEnabled = true
            }
        }

    }
}