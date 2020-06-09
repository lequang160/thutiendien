package com.xep.thutiendien.base

import androidx.appcompat.app.AppCompatActivity
import com.xep.thutiendien.module.Network

abstract class BaseActivity : AppCompatActivity(){
    var mApi = Network.appApi

    override fun onDestroy() {
        super.onDestroy()
        mApi = null
    }
}