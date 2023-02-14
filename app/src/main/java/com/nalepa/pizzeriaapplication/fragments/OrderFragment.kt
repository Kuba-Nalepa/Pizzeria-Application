package com.nalepa.pizzeriaapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.nalepa.pizzeriaapplication.OnOrderClicked
import com.nalepa.pizzeriaapplication.OrderAdapter
import com.nalepa.pizzeriaapplication.data.order.OrderItem
import com.nalepa.pizzeriaapplication.databinding.FragmentOrderBinding
import com.nalepa.pizzeriaapplication.databinding.RecyclerOrderedListItemBinding
import com.nalepa.pizzeriaapplication.viewmodel.SharedViewModel

class OrderFragment : Fragment(), OnOrderClicked {

    private lateinit var binding: FragmentOrderBinding
    private val viewModel by viewModels<SharedViewModel>()
    private val myAdapter = OrderAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            orderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            orderRecyclerView.adapter = myAdapter
        }

        viewModel.userOrder.observe(viewLifecycleOwner) { orderList ->
            myAdapter.setCurrentUserOrder(orderList)
        }
    }

    override fun addQuantity(orderItem: OrderItem) {
        orderItem.quantity += 1
        orderItem.price = orderItem.size.price * orderItem.quantity
        viewModel.updateOrderDetails(orderItem)
    }

    override fun delQuantity(orderItem: OrderItem) {
        if(orderItem.quantity != 1) {
            orderItem.quantity -= 1
            orderItem.price = orderItem.size.price * orderItem.quantity
            viewModel.updateOrderDetails(orderItem)
        } else {
            Toast.makeText(requireContext(),"You can't decrease quantity to 0", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDeleteClick(orderItem: OrderItem) {
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
}