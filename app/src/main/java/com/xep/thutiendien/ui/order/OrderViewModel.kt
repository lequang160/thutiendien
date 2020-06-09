package com.xep.thutiendien.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xep.thutiendien.ResultState
import com.xep.thutiendien.base.BaseViewModel
import com.xep.thutiendien.models.OrderModel
import com.xep.thutiendien.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderViewModel : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    val orderLiveData = SingleLiveEvent<List<OrderModel>>()

    fun fetchOrder(){
        CoroutineScope(Dispatchers.IO).launch {
            val order = safeApiCall { mApi.fetchOrderList("") }
            withContext(Dispatchers.Main){
                when(order){
                    is ResultState.Success -> orderLiveData.postValue(order.data.map { it.toOrderModel() })
                    is ResultState.Error -> _text.value = order.exception.message
                }
            }
        }
    }

    fun updateOrder(id: String){
        CoroutineScope(Dispatchers.IO).launch {
            val status = safeApiCall { mApi.updateStatusOrder(id, "1") }
            withContext(Dispatchers.Main){
                when(status){
                    is ResultState.Success -> _text.value = status.data.KetQua
                    is ResultState.Error -> _text.value = status.exception.message
                }
            }
        }
    }
}