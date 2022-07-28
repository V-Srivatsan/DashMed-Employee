package com.dashmed.employee.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dashmed.employee.R
import com.dashmed.employee.Utils
import com.dashmed.employee.databinding.FragmentOrderDetailsBinding
import com.dashmed.employee.networking.Order
import com.dashmed.employee.viewmodels.OrderVM


class OrderDetails : Fragment() {

    private lateinit var binding: FragmentOrderDetailsBinding
    private lateinit var viewModel: OrderVM

    private lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[OrderVM::class.java]
        viewModel.order_id = requireActivity().intent.getStringExtra("order_id").toString()
        Utils.getUID(requireActivity())?.let { viewModel.uid = it }

        requireActivity().supportFragmentManager.apply {
            for (i in 0 until backStackEntryCount)
                popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        coordinatorLayout = requireActivity().findViewById(R.id.order_coordinator_layout)

        if (OrderVM.res == null)
            viewModel.getOrderDetails().invokeOnCompletion {
                OrderVM.res?.let { res ->
                    if (res.valid) {
                        fillDetails()
                    } else
                        requireActivity().finish()
                }
            }
        else
            fillDetails()

        binding.orderDetailItems.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                replace(R.id.order_container, OrderItems())
                addToBackStack(null)
            }.commit()
        }

        binding.orderDetailMap.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=${OrderVM.res?.lat?.toString()},${OrderVM.res?.long?.toString()}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        binding.orderDetailDelivered.setOnClickListener { btn ->
            binding.orderDetailDeliveryProgress.visibility = View.VISIBLE
            btn.visibility = View.GONE

            viewModel.deliverOrder().invokeOnCompletion {
                val res = viewModel.deliverRes
                if (res.valid)
                    requireActivity().finish()
                else {
                    binding.orderDetailDeliveryProgress.visibility = View.GONE
                    btn.visibility = View.VISIBLE

                    Utils.showSnackbar(coordinatorLayout, res.message)
                }
            }
        }

        return binding.root
    }

    private fun fillDetails() {
        OrderVM.res?.let { res ->
            binding.orderDetail.visibility = View.VISIBLE
            binding.orderDetailProgress.visibility = View.GONE

            var total = 0f
            res.items?.let { for (item: Order in it) total += (item.quantity * item.cost) }
            binding.orderDetailName.text = res.name
            binding.orderDetailContact.text = res.contact
            binding.orderDetailCost.text = "$total INR"
            binding.orderDetailAddress.text = res.address
        }
    }
}