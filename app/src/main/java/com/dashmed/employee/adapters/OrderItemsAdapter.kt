package com.dashmed.employee.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dashmed.employee.R
import com.dashmed.employee.Utils
import com.dashmed.employee.networking.Order
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class OrderItemsAdapter (private val context: Context, private val dataset: List<Order>, private val inflater: LayoutInflater) : RecyclerView.Adapter<OrderItemsAdapter.ItemHolder>() {

    inner class ItemHolder (private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val name: TextView = view.findViewById(R.id.med_name)
        val quantity: TextView = view.findViewById(R.id.med_quantity)
        val cost: TextView = view.findViewById(R.id.med_cost)

        lateinit var info: Order

        init { view.setOnClickListener(this) }
        override fun onClick(v: View) {
            Utils.showBottomSheet(R.layout.med_info, inflater, context).apply { 
                findViewById<TextView>(R.id.med_info_name)?.text = info.name
                findViewById<TextView>(R.id.med_info_cost)?.text = "${info.cost} INR"
                findViewById<TextView>(R.id.med_info_description)?.text = info.description
                findViewById<TextView>(R.id.med_info_expiration)?.text = "Expires in ${info.expiration} month(s)"
                
                findViewById<ChipGroup>(R.id.med_info_composition)?.let {
                    for (item: String in info.composition) {
                        val chip = Chip(context)
                        chip.text = item
                        it.addView(chip)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(LayoutInflater.from(context).inflate(R.layout.med_item, parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val info = dataset[position]
        holder.info = info
        holder.name.text = info.name
        holder.quantity.text = "Quantity: ${info.quantity}"
        holder.cost.text = "${info.cost} INR"
    }

    override fun getItemCount(): Int {
        return dataset.count()
    }


}