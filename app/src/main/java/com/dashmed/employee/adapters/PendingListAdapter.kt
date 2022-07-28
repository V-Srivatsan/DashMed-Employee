package com.dashmed.employee.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dashmed.employee.OrderActivity
import com.dashmed.employee.R
import com.dashmed.employee.networking.OrderItem

class PendingListAdapter (private val context: Context, private var dataset: MutableList<OrderItem>) : RecyclerView.Adapter<PendingListAdapter.OrderHolder>() {

    inner class OrderHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val name: TextView = view.findViewById(R.id.order_name)
        val quantity: TextView = view.findViewById(R.id.order_quantity)

        lateinit var info: OrderItem

        init { view.setOnClickListener(this) }

        override fun onClick(view: View?) {
            val intent = Intent(context, OrderActivity::class.java)
            intent.putExtra("order_id", info.uid)
            context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false)
        return OrderHolder(layout)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        val data = dataset[position]
        holder.name.text = data.name
        holder.quantity.text = "Quantity: ${data.quantity}"
        holder.info = data
    }

    override fun getItemCount(): Int {
        return dataset.count()
    }

    fun updateDataset(new_dataset: List<OrderItem>) {
        dataset = new_dataset.toMutableList()
        notifyDataSetChanged()
    }

}