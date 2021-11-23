package com.hzsoft.moudule.me.activity

import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import com.hzsoft.lib.base.view.BaseMvvmActivity
import com.hzsoft.moudule.me.R

/**
 * 测试 SaveStateHandler
 *
 * @author zhouhuan
 * @time 2021/11/23
 */
class SaveStateTestActivity : BaseMvvmActivity<SaveStateTestViewModel>() {

    companion object {
        fun start(context: Context, info: String) {
            context.startActivity(Intent(context, SaveStateTestActivity::class.java).apply {
                putExtra(SaveStateTestViewModel.JUMP_PAGE, info)
            })
        }
    }

    private lateinit var textView: TextView
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView

    private var countLeft = 0
    private var countRight = 0

    override fun getTootBarTitle(): String = "SaveState"

    override fun enableToolBarLeft(): Boolean = true

    override fun onBindLayout(): Int = R.layout.activity_save_state_test

    override fun initView() {
        textView = findViewById(R.id.textView)
        textView1 = findViewById(R.id.textView1)
        textView2 = findViewById(R.id.textView2)
        textView3 = findViewById(R.id.textView3)
        findViewById<Button>(R.id.left).setOnClickListener {
            ++countLeft
            initData()
        }
        findViewById<Button>(R.id.right).setOnClickListener {
            ++countRight
            mViewModel.saveCount(countRight)
            initData()
        }
    }

    override fun initData() {
        if (mViewModel.getCount() != 0) {
            countRight = mViewModel.getCount()
        }

        textView.text = "收到的页面内容：%s".format(mViewModel.getJumpPage())
        textView2.text = "会重置：%s".format(countLeft)
        textView3.text = "不会重置：%s".format(mViewModel.getCount())
    }

    override fun onBindViewModel(): Class<SaveStateTestViewModel> =
        SaveStateTestViewModel::class.java

    override fun initViewObservable() {

    }
}