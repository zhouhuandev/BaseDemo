package com.hzsoft.moudule.me.fragment

import android.view.View
import com.google.gson.Gson
import com.hzsoft.lib.net.utils.ext.view.showToast
import com.hzsoft.lib.base.utils.log.KLog
import com.hzsoft.lib.base.view.BaseFragment
import com.hzsoft.moudule.me.R
import com.ypx.imagepicker.demo.utils.ImagePickerHelper
import com.ypx.imagepicker.demo.utils.ImagePickerHelper.OnResultCallBack
import kotlinx.android.synthetic.main.fragment_me_main.*

/**
 * Describe:
 * 首页
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
class MainMeFragment : BaseFragment() {

    companion object {
        fun newsInstance(): MainMeFragment {
            return MainMeFragment()
        }
    }

    private var imagePickerHelper: ImagePickerHelper? = null

    override fun onBindLayout(): Int = R.layout.fragment_me_main

    override fun initView(mView: View) {

    }

    override fun initData() {
        val config = ImagePickerHelper.with(ImagePickerHelper.Config())
            .setWeChat(true)
            .setMimeType(0)
            .setShowOriginal(true)
            .setSave(true)
            .setMulti(true)
            .setMaxCount(6)
            .build()
        imagePickerHelper = ImagePickerHelper(
            activity, gridLayout,
            OnResultCallBack { items ->
                KLog.d(TAG, "选择的照片数据" + Gson().toJson(items))
            }, config
        )
    }

    override fun initListener() {
        button_1.setOnClickListener(this::onClick)
        button_2.setOnClickListener(this::onClick)
    }

    override fun enableToolbar(): Boolean = true

    override fun getTootBarTitle(): String = resources.getString(R.string.module_me)

    override fun onClick(v: View?) {
        if (beFastClick()) {
            return
        }
        when (v?.id) {
            R.id.button_1 -> {
                val config =
                    ImagePickerHelper.with(ImagePickerHelper.Config())
                        .setWeChat(true)
                        .setMimeType(0)
                        .setShowOriginal(true)
                        .setSave(true)
                        .setMulti(true)
                        .build()
                imagePickerHelper!!.setConfig(config)
                "拍照模式设置成功".showToast(mContext)
            }
            button_2.id -> {
                val config =
                    ImagePickerHelper.with(ImagePickerHelper.Config())
                        .setWeChat(true)
                        .setMimeType(1)
                        .setShowOriginal(true)
                        .setSave(true)
                        .setMulti(true)
                        .build()
                imagePickerHelper!!.setConfig(config)
                "视频模式设置成功".showToast(mContext)
            }
        }
    }
}
