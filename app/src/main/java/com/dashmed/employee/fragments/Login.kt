package com.dashmed.employee.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dashmed.employee.MainActivity
import com.dashmed.employee.R
import com.dashmed.employee.Utils
import com.dashmed.employee.databinding.FragmentLoginBinding
import com.dashmed.employee.viewmodels.LoginVM


class Login : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginVM

    private lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginVM::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        coordinatorLayout = requireActivity().findViewById(R.id.login_coordinator_layout)

        var isListening = false
        binding.loginBtn.setOnClickListener {
            if (! isListening) {
                isListening = true
                binding.loginUsername.editText?.addTextChangedListener {
                    Utils.validateField(binding.loginUsername, Utils.Companion.VALIDATION_TYPE.TEXT, "Username")
                }
                binding.loginPassword.editText?.addTextChangedListener {
                    Utils.validateField(binding.loginPassword, Utils.Companion.VALIDATION_TYPE.PASSWORD)
                }
            }

            if (
                Utils.validateField(binding.loginUsername, Utils.Companion.VALIDATION_TYPE.TEXT, "Username") &&
                Utils.validateField(binding.loginPassword, Utils.Companion.VALIDATION_TYPE.PASSWORD)
            ) {
                binding.loginBtn.visibility = View.GONE
                binding.loginProgress.visibility = View.VISIBLE

                viewModel.login(binding.loginUsername.editText?.text.toString(), binding.loginPassword.editText?.text.toString())
                    .invokeOnCompletion {
                        val res = viewModel.res
                        if (res.valid) {
                            with (requireActivity().getSharedPreferences("EmpPrefs", Context.MODE_PRIVATE).edit()) {
                                putString("UID", res.uid.toString())
                                apply()
                            }
                            requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
                            requireActivity().finish()
                        }
                        else {
                            Utils.showSnackbar(coordinatorLayout, res.message)

                            binding.loginBtn.visibility = View.VISIBLE
                            binding.loginProgress.visibility = View.GONE
                        }
                    }
            }
        }

        return binding.root
    }
}