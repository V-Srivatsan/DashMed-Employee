package com.dashmed.employee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dashmed.employee.adapters.OrderItemsAdapter
import com.dashmed.employee.databinding.FragmentOrderItemsBinding
import com.dashmed.employee.viewmodels.OrderVM


class OrderItems : Fragment() {

    private lateinit var binding: FragmentOrderItemsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderItemsBinding.inflate(inflater, container, false)

        OrderVM.res?.items?.let {
            binding.orderItems.adapter = OrderItemsAdapter(requireContext(), it, layoutInflater)
            binding.orderItems.layoutManager = LinearLayoutManager(requireContext())
        }

        return binding.root
    }
}