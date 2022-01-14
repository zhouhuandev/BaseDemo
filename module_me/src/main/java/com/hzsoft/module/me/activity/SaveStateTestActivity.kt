package com.hzsoft.module.me.activity

import android.content.Context
import android.content.Intent
import android.view.ViewStub
import com.hzsoft.lib.base.view.BaseMvvmActivity
import com.hzsoft.lib.base.view.viewbinding.ActivityBinding
import com.hzsoft.lib.base.view.viewbinding.ActivityViewBinding
import com.hzsoft.module.me.R
import com.hzsoft.module.me.databinding.ActivitySaveStateTestBinding

/**
 * 测试 SaveStateHandler，使用了ViewBinding示例
 *
 * @author zhouhuan
 * @time 2021/11/23
 */
class SaveStateTestActivity : BaseMvvmActivity<SaveStateTestViewModel>(),
    ActivityViewBinding<ActivitySaveStateTestBinding> by ActivityBinding() {

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

    override fun initContentView(mViewStubContent: ViewStub) {
        mViewStubContent.setOnInflateListener { _, inflated ->
            inflate({ ActivitySaveStateTestBinding.bind(inflated) }, false)
        }
        super.initContentView(mViewStubContent)
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