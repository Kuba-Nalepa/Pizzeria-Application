package com.nalepa.pizzeriaapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nalepa.pizzeriaapplication.data.pizza.Pizza
import com.nalepa.pizzeriaapplication.databinding.RecyclerMenuListItemBinding

class FavouritesAdapter: RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>() {

    private val favouritesList =  ArrayList<Pizza>()

    fun setFavouritePizzas(list: List<Pizza>) {
        favouritesList.clear()
        favouritesList.addAll(list)
        notifyDataSetChanged()

    }

    inner class FavouritesViewHolder(binding: RecyclerMenuListItemBinding): RecyclerView.ViewHolder(binding.root) {
        val pizzaName = binding.pizzaName
        val pizzaImage = binding.pizzaImage
        val pizzaPrice = binding.pizzaPrice
        val pizzaIngredients = binding.pizzaIngredients
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = RecyclerMenuListItemBinding.inflate(inflater, parent, false)
        return FavouritesViewHolder(viewHolder)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        holder.pizzaName.text = favouritesList[position].name
        holder.pizzaIngredients.text = favouritesList[position].ingredients.joinToString (", ")
        Glide.with(holder.itemView)
            .load(favouritesList[holder.adapterPosition].image)
            .into(holder.pizzaImage)
        holder.pizzaPrice.text = " Min. ${favouritesList[position].sizes.small.price} $"
    }

    override fun getItemCount(): Int {
        return favouritesList.size
    }


}
