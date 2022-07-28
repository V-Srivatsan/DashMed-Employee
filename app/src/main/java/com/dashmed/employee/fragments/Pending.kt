package com.dashmed.employee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dashmed.employee.R
import com.dashmed.employee.Utils
import com.dashmed.employee.adapters.PendingListAdapter
import com.dashmed.employee.databinding.FragmentPendingBinding
import com.dashmed.employee.viewmodels.PendingVM

class Pending : Fragment() {

    private lateinit var binding: FragmentPendingBinding
    private lateinit var viewModel: PendingVM
    private lateinit var adapter: PendingListAdapter

    private lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PendingVM::class.java]
        Utils.getUID(requireActivity())?.let { viewModel.uid = it }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPendingBinding.inflate(inflater, container, false)
        coordinatorLayout = requireActivity().findViewById(R.id.main_coordinator_layout)

        adapter = PendingListAdapter(requireContext(), mutableListOf())
        binding.pendingList.layoutManager = LinearLayoutManager(requireContext())
        binding.pendingList.adapter = adapter

        binding.pendingEmptyLayout.setOnRefreshListener { updateList(binding.pendingEmptyLayout) }
        binding.pendingListLayout.setOnRefreshListener { updateList(binding.pendingListLayout) }

        return binding.root
    }

    override fun onResume() {
        updateList(
            if (binding.pendingListLayout.visibility == View.VISIBLE)
                binding.pendingListLayout
            else
                binding.pendingEmptyLayout
        )
        super.onResume()
    }

    private fun updateList(layout: SwipeRefreshLayout) {
        layout.isRefreshing = true
        viewModel.getPending().invokeOnCompletion {
            layout.isRefreshing = false
            val res = viewModel.res
            if (res.valid) {
                res.orders?.let {
                    if (it.isEmpty()) {
                        binding.pendingListLayout.visibility = View.GONE
                        binding.pendingEmptyLayout.visibility = View.VISIBLE
                    } else {
                        binding.pendingListLayout.visibility = View.VISIBLE
                        binding.pendingEmptyLayout.visibility = View.GONE
                        adapter.updateDataset(it)
                    }
                }
            } else {
                if (adapter.itemCount == 0)
                    binding.pendingEmptyLayout.visibility = View.VISIBLE
                Utils.showSnackbar(coordinatorLayout, res.message)
            }
        }
    }
}