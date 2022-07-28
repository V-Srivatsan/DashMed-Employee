package com.dashmed.employee.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashmed.employee.networking.API
import com.dashmed.employee.networking.LoginReq
import com.dashmed.employee.networking.LoginRes
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class LoginVM : ViewModel() {

    lateinit var res: LoginRes

    fun login(username: String, password: String): Job {
        return viewModelScope.launch {
            try {
                res = API.service.login(LoginReq(username, password))
            } catch (e: Exception) {
                res = LoginRes(false, null, null)
            }
        }
    }

}