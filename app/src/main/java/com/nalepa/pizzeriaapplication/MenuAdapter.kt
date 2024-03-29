package com.nalepa.pizzeriaapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nalepa.pizzeriaapplication.data.pizza.Pizza
import com.nalepa.pizzeriaapplication.databinding.RecyclerMenuListItemBinding

class MenuAdapter(private val listener: OnPizzaItemClick): RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private val menuList =  ArrayList<Pizza>()

    fun setPizzaMenu(list: List<Pizza>) {
        menuList.clear()
        menuList.addAll(list)
        notifyDataSetChanged()

    }

    inner class MenuViewHolder(binding: RecyclerMenuListItemBinding): RecyclerView.ViewHolder(binding.root) {
        val pizzaName = binding.pizzaName
        val pizzaImage = binding.pizzaImage
        val pizzaPrice = binding.pizzaPrice
        val pizzaIngredients = binding.pizzaIngredients
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = RecyclerMenuListItemBinding.inflate(inflater, parent, false)
        return MenuViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
                listener.onPizzaClick(menuList[position], position)
        }
        holder.pizzaName.text = menuList[position].name
        holder.pizzaIngredients.text = menuList[position].ingredients.joinToString (", ")
        Glide.with(holder.itemView)
            .load(menuList[holder.adapterPosition].image)
            .placeholder(R.drawable.loading_animation)
            .into(holder.pizzaImage)
        holder.pizzaPrice.text = " Min. ${menuList[position].sizes.small.price} $"
    }
}
interface OnPizzaItemClick {
    fun onPizzaClick(pizza: Pizza, position: Int)
}