package com.xep.thutiendien.ui.order_paid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xep.thutiendien.MainActivity
import com.xep.thutiendien.R
import com.xep.thutiendien.models.OrderModel
import com.xep.thutiendien.ui.order.OrderAdapter

class OrderPaidFragment : Fragment() {

    private lateinit var orderPaidViewModel: OrderPaidViewModel
    lateinit var mOrderRecyclerView: RecyclerView
    val mAdapter: OrderAdapter = OrderAdapter(item = null)
    lateinit var mOrderListTemp: List<OrderModel>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        orderPaidViewModel =
            ViewModelProviders.of(this).get(OrderPaidViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)

        orderPaidViewModel.text.observe(viewLifecycleOwner, Observer {

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
        mAdapter.dathu = true
        mOrderRecyclerView.adapter = mAdapter

        orderPaidViewModel.orderLiveData.observe(viewLifecycleOwner, Observer {
            mOrderListTemp = it
            mAdapter.data.clear()
            mAdapter.data.addAll(it)
            mAdapter.notifyDataSetChanged()
        })

        orderPaidViewModel.fetchOrderPaid()

        mAdapter.mPrintListener = { order ->

        }
    }

    override fun onDetach() {
        super.onDetach()
        if (this::mOrderListTemp.isInitialized) {
            mOrderListTemp = arrayListOf()
        }
    }
}
