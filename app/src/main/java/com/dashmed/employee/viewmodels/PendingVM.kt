package com.dashmed.employee.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashmed.employee.networking.API
import com.dashmed.employee.networking.GetPendingRes
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PendingVM : ViewModel() {

    lateinit var uid: String

    lateinit var res: GetPendingRes

    fun getPending(): Job {
        return viewModelScope.launch {
            try {
                res = API.service.getPending(uid)
            } catch (e: Exception) {
                res = GetPendingRes(false, null, null)
            }
        }
    }

}