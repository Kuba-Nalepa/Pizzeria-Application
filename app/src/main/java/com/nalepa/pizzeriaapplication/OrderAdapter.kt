package com.nalepa.pizzeriaapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nalepa.pizzeriaapplication.data.order.Item
import com.nalepa.pizzeriaapplication.databinding.RecyclerOrderedListItemBinding

class OrderAdapter(private val listener: OnOrderClicked) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private val orderList = ArrayList<Item>()

    fun setCurrentUserOrder(list: List<Item>) {
        orderList.clear()
        orderList.addAll(list)
        notifyDataSetChanged()

    }

    inner class OrderViewHolder(binding: RecyclerOrderedListItemBinding): RecyclerView.ViewHolder(binding.root) {
        val pizzaName = binding.pizzaName
        val pizzaPrice = binding.pizzaPrice
        val pizzaQuantity = binding.pizzaQuantity
        val addQuantity = binding.addQuantityButton
        val subtractQuantity = binding.subtractQuantityButton
        val deletePizza = binding.deletePizza
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = RecyclerOrderedListItemBinding.inflate(inflater, parent, false)

        return OrderViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.pizzaName.text = orderList[position].name
        holder.pizzaPrice.text = "${orderList[position].totalPrice} $"
        holder.pizzaQuantity.text = orderList[position].quantity.toString()

        holder.addQuantity.setOnClickListener {
            listener.addQuantity(orderList[position])
        }

        holder.subtractQuantity.setOnClickListener {
            listener.delQuantity(orderList[position])
        }

        holder.deletePizza.setOnClickListener {
            listener.onDeleteClick(orderList[position])
        }
    }
}

interface OnOrderClicked{
    fun addQuantity(orderItem: Item)
    fun delQuantity(orderItem: Item)
    fun onDeleteClick(orderItem: Item)
}
