package com.hzsoft.basedemo.ui.activity

import android.content.Context
import android.content.Intent
import com.hzsoft.basedemo.R
import com.hzsoft.basedemo.databinding.ActivitySaveStateTestBinding
import com.hzsoft.basedemo.ui.activity.viewmodel.SaveStateTestViewModel
import com.hzsoft.lib.base.view.BaseMvvmViewBindingActivity

/**
 * 测试 SaveStateHandler，使用了ViewBinding示例
 *
 * @author zhouhuan
 * @time 2021/11/23
 */
class SaveStateTestActivity :
    BaseMvvmViewBindingActivity<ActivitySaveStateTestBinding, SaveStateTestViewModel>() {

    companion object {
        fun start(context: Context, info: String) {
            context.startActivity(Intent(context, SaveStateTestActivity::class.java).apply {
                putExtra(SaveStateTestViewModel.JUMP_PAGE, info)
            })
        }
    }

    private var countLeft = 0
    private var countRight = 0

    override fun getTootBarTitle(): String = "SaveState"

    override fun enableToolBarLeft(): Boolean = true

    override fun onBindLayout(): Int = R.layout.activity_save_state_test

    override fun onBindingClass(): Class<ActivitySaveStateTestBinding> {
        return ActivitySaveStateTestBinding::class.java
    }

    override fun initView() {
        requireBinding().left.setOnClickListener {
            ++countLeft
            initData()
        }
        requireBinding().right.setOnClickListener {
            ++countRight
            mViewModel.saveCount(countRight)
            initData()
        }
    }

    override fun initData() {
        if (mViewModel.getCount() != 0) {
            countRight = mViewModel.getCount()
        }

        requireBinding().textView.text = "收到的页面内容：%s".format(mViewModel.getJumpPage())
        requireBinding().textView2.text = "会重置：%s".format(countLeft)
        requireBinding().textView3.text = "不会重置：%s".format(mViewModel.getCount())
    }

    override fun onBindViewModel(): Class<SaveStateTestViewModel> =
        SaveStateTestViewModel::class.java

    override fun initViewObservable() {

    }
}