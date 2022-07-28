package com.dashmed.employee.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dashmed.employee.LoginActivity
import com.dashmed.employee.MainActivity
import com.dashmed.employee.R
import com.dashmed.employee.Utils
import com.dashmed.employee.databinding.FragmentSettingsBinding
import com.dashmed.employee.viewmodels.SettingsVM
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout


class Settings : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsVM

    private lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SettingsVM::class.java]
        Utils.getUID(requireActivity())?.let { viewModel.uid = it }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        coordinatorLayout = requireActivity().findViewById(R.id.main_coordinator_layout)

        Utils.showDialog(R.layout.password_dialog, layoutInflater, requireContext()).apply {
            val cancel: Button = findViewById(R.id.password_cancel)
            val proceed: Button = findViewById(R.id.password_ok)
            val input: TextInputLayout = findViewById(R.id.password_input)
            val progress: ProgressBar = findViewById(R.id.password_progress)

            cancel.setOnClickListener {
                requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).selectedItemId = R.id.nav_pending
                dismiss()
            }

            input.editText?.addTextChangedListener {
                Utils.validateField(input, Utils.Companion.VALIDATION_TYPE.PASSWORD)
            }

            proceed.setOnClickListener {
                if (Utils.validateField(input, Utils.Companion.VALIDATION_TYPE.PASSWORD)) {
                    progress.visibility = View.VISIBLE
                    cancel.visibility = View.GONE
                    proceed.visibility = View.GONE

                    viewModel.checkPassword(input.editText?.text.toString()).invokeOnCompletion {
                        val res = viewModel.res
                        if (res.valid)
                            dismiss()
                        else {
                            progress.visibility = View.GONE
                            cancel.visibility = View.VISIBLE
                            proceed.visibility = View.VISIBLE
                            input.error = res.message ?: getString(R.string.network_error)
                        }
                    }
                }
            }
        }

        val password = binding.settingsNewPassword
        val confirm = binding.settingsConfirmPassword

        password.editText?.addTextChangedListener { Utils.validateField(password, Utils.Companion.VALIDATION_TYPE.PASSWORD) }
        confirm.editText?.addTextChangedListener { Utils.validateField(confirm, Utils.Companion.VALIDATION_TYPE.PASSWORD) }

        binding.settingsSavePassword.setOnClickListener {
            if (
                Utils.validateField(password, Utils.Companion.VALIDATION_TYPE.PASSWORD) &&
                Utils.validateField(confirm, Utils.Companion.VALIDATION_TYPE.PASSWORD)
            ) {
                val newPassword = password.editText?.text.toString()
                if (newPassword != confirm.editText?.text.toString())
                    confirm.error = "The passwords do not match!"
                else {
                    password.isErrorEnabled = false
                    confirm.isErrorEnabled = false

                    binding.settingsProgress.visibility = View.VISIBLE
                    it.visibility = View.GONE

                    viewModel.updatePassword(newPassword).invokeOnCompletion { _ ->
                        binding.settingsProgress.visibility = View.GONE
                        it.visibility = View.VISIBLE

                        val res = viewModel.res
                        if (res.valid) {
                            password.editText?.setText("")
                            password.isErrorEnabled = false

                            confirm.editText?.setText("")
                            confirm.isErrorEnabled = false

                            Utils.showSnackbar(coordinatorLayout, "Password updated successfully!")
                        } else
                            Utils.showSnackbar(coordinatorLayout, res.message)
                    }
                }
            }
        }

        binding.settingsSignOut.setOnClickListener {
            with (requireActivity().getSharedPreferences("EmpPrefs", Context.MODE_PRIVATE).edit()) {
                clear()
                apply()
            }
            MainActivity.reloadFragment = true
            requireActivity().startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }

        return binding.root
    }
}