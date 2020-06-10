package com.xep.thutiendien.ui.order

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xep.thutiendien.MainActivity
import com.xep.thutiendien.PrinterActivity
import com.xep.thutiendien.R
import com.xep.thutiendien.base.BaseFragment
import com.xep.thutiendien.models.OrderModel

class OrderFragment : BaseFragment() {

    private lateinit var orderViewModel: OrderViewModel
    lateinit var mOrderRecyclerView: RecyclerView
    val mAdapter: OrderAdapter = OrderAdapter(item = null)
    lateinit var mOrderSelected: OrderModel
    var mOrderListTemp: MutableList<OrderModel> = arrayListOf()
    var mCurrentPage = 1
    var isLoadMore = false

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

            if (query.isEmpty()) {
                mAdapter.data.clear()
                mAdapter.data.addAll(mOrderListTemp)
                mAdapter.notifyDataSetChanged()
            } else {
                val temp = mOrderListTemp.filter { orderModel ->
                    orderModel.address.toUpperCase().contains(query.toUpperCase()) ||
                            orderModel.customerName.toUpperCase().contains(query.toUpperCase()) ||
                            orderModel.customerId.contains(query) ||
                            orderModel.phoneNumber.contains(query) ||
                            orderModel.transaction.contains(query)
                }
                mAdapter.data.clear()
                mAdapter.data.addAll(temp)
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mOrderRecyclerView = view.findViewById(R.id.fragment_home_order_rv)
        val layoutManage = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mOrderRecyclerView.layoutManager = layoutManage
        mOrderRecyclerView.adapter = mAdapter
        mAdapter.isLoadMoreEnable = true

        orderViewModel.orderLiveData.observe(viewLifecycleOwner, Observer {
            if (isLoadMore) {
                mAdapter.data.addAll(it)
                mAdapter.notifyDataSetChanged()
            } else {
                mAdapter.data.clear()
                mAdapter.data.addAll(it)
                mAdapter.notifyDataSetChanged()
            }
            if (it.isNullOrEmpty()) {
                isLoadMore = false
                mAdapter.isLoadMoreEnable = false
            }
            mOrderListTemp.clear()
            mOrderListTemp.addAll(mAdapter.data)
            hideLoading()
        })

        orderViewModel.text.observe(viewLifecycleOwner, Observer {
            for (i in 1..10) {
                mAdapter.data.removeAt(mAdapter.data.size - 1)
            }
            orderViewModel.fetchOrder(mCurrentPage.toString())
        })

        orderViewModel.fetchOrder(mCurrentPage.toString()).run {
            showLoading()
        }

        mAdapter.mPaymentListener = { order ->
            mOrderSelected = order
            orderViewModel.updateOrder(order.id).run {
                showLoading()
            }
        }

        mAdapter.onLoadMoreListener = {
            isLoadMore = true
            mCurrentPage += 1
            orderViewModel.fetchOrder(mCurrentPage.toString())
        }

        mAdapter.mPrintListener = { order ->
            val intent = Intent(requireContext(), PrinterActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("data", order)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    override fun onDetach() {
        super.onDetach()
        mOrderListTemp = arrayListOf()

    }
}
