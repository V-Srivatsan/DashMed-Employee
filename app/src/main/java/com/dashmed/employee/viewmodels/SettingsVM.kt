package com.dashmed.employee.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashmed.employee.networking.API
import com.dashmed.employee.networking.ChangePasswordReq
import com.dashmed.employee.networking.EmptyRes
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SettingsVM : ViewModel() {

    lateinit var uid: String
    lateinit var res: EmptyRes

    fun checkPassword(password: String): Job {
        return viewModelScope.launch {
            try {
                res = API.service.checkPassword(uid, password)
            } catch (e: Exception) {
                res = EmptyRes(false, null)
            }
        }
    }

    fun updatePassword(password: String): Job {
        return viewModelScope.launch {
            try {
                res = API.service.changePassword(ChangePasswordReq(uid, password))
            } catch (e: Exception) {
                res = EmptyRes(false, null)
            }
        }
    }

}