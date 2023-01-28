package com.nalepa.pizzeriaapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = RecyclerMenuListItemBinding.inflate(inflater, parent, false)
        return MenuViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
                listener.onPizzaClick(menuList[position], position)
        }
        holder.pizzaName.text = menuList[position].name
        Glide.with(holder.itemView)
            .load(menuList[holder.adapterPosition].image)
            .into(holder.pizzaImage)
    }

//    private fun bindData(holder: MenuViewHolder) {
//        val pizzaName = holder.itemView.findViewById<TextView>(R.id.pizzaName)
//        val pizzaImage = holder.itemView.findViewById<ImageView>(R.id.pizzaImage)
//
//        pizzaName.text = menuList[holder.adapterPosition].name
//        Glide.with(holder.itemView)
//            .load(menuList[holder.adapterPosition].image)
//            .into(pizzaImage)
//    }
}
interface OnPizzaItemClick {
    fun onPizzaClick(pizza: Pizza, position: Int)
}