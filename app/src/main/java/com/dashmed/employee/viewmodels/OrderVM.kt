package com.dashmed.employee.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashmed.employee.networking.API
import com.dashmed.employee.networking.DeliverOrderReq
import com.dashmed.employee.networking.EmptyRes
import com.dashmed.employee.networking.GetOrderRes
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderVM : ViewModel() {

    companion object {
        var res: GetOrderRes? = null
    }

    lateinit var deliverRes: EmptyRes
    lateinit var order_id: String
    lateinit var uid: String

    fun getOrderDetails(): Job {
        return viewModelScope.launch {
            try {
                res = API.service.getOrder(order_id)
            } catch (e: Exception) {
                res = GetOrderRes(false, null, null, null,null, null, null, null)
            }
        }
    }

    fun deliverOrder(): Job {
        return viewModelScope.launch {
            try {
                deliverRes = API.service.deliverOrder(DeliverOrderReq(uid, order_id))
            } catch (e: Exception) {
                deliverRes = EmptyRes(false, null)
            }
        }
    }

}