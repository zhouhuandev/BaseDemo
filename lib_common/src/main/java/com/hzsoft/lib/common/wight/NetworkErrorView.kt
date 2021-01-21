package com.hzsoft.lib.common.wight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.hzsoft.lib.common.R


/**
 * Description: <NetworkErrorView><br>
 * Author:      mxdl<br>
 * Date:        2018/6/20<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
</NetworkErrorView> */
class NetworkErrorView : RelativeLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context)
            .inflate(R.layout.button_network_err, this, true)
        this.isClickable = true
        this.isFocusable = true
    }
}
