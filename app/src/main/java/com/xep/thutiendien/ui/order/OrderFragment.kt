package com.xep.thutiendien.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xep.thutiendien.MainActivity
import com.xep.thutiendien.R
import com.xep.thutiendien.base.BaseFragment
import com.xep.thutiendien.models.OrderModel

class OrderFragment : BaseFragment() {

    private lateinit var orderViewModel: OrderViewModel
    lateinit var mOrderRecyclerView: RecyclerView
    val mAdapter: OrderAdapter = OrderAdapter( item = null)
    lateinit var mOrderSelected: OrderModel
    lateinit var mOrderListTemp: List<OrderModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        orderViewModel =
            ViewModelProviders.of(this).get(OrderViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        orderViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).mSearchListener = { query ->

            val temp = mOrderListTemp.filter { orderModel ->  orderModel.address.contains(query) ||
                    orderModel.customerName.contains(query) ||
                    orderModel.customerId.contains(query) ||
                    orderModel.phoneNumber.contains(query) ||
                    orderModel.transaction.contains(query)  }
            mAdapter.data.clear()
            mAdapter.data.addAll(temp)
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mOrderRecyclerView = view.findViewById(R.id.fragment_home_order_rv)
        val layoutManage = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mOrderRecyclerView.layoutManager = layoutManage
        mOrderRecyclerView.adapter = mAdapter

        orderViewModel.orderLiveData.observe(viewLifecycleOwner, Observer {
            mOrderListTemp = it
            mAdapter.data.clear()
            mAdapter.data.addAll(it)
            mAdapter.notifyDataSetChanged()
            hideLoading()
        })

        orderViewModel.text.observe(viewLifecycleOwner, Observer {
            orderViewModel.fetchOrder()
        })

        orderViewModel.fetchOrder().run {
            showLoading()
        }

        mAdapter.mPaymentListener = {
            order ->
            mOrderSelected = order
            orderViewModel.updateOrder(order.id).run {
                showLoading()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (this::mOrderListTemp.isInitialized) {
            mOrderListTemp = arrayListOf()
        }
    }
}
