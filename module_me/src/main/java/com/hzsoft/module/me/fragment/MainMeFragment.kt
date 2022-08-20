package com.hzsoft.module.me.fragment

import android.view.View
import com.google.gson.Gson
import com.hzsoft.lib.base.utils.ToastUtil
import com.hzsoft.lib.base.utils.ext.view.showToast
import com.hzsoft.lib.base.view.BaseMvvmViewBindingFragment
import com.hzsoft.lib.log.KLog
import com.hzsoft.module.me.R
import com.hzsoft.module.me.activity.RoomTestActivity
import com.hzsoft.module.me.activity.SaveStateTestActivity
import com.hzsoft.module.me.databinding.FragmentMeMainBinding
import com.hzsoft.module.me.viewmodel.MainMeViewModel
import com.ypx.imagepicker.demo.utils.ImagePickerHelper

/**
 * Describe:
 * 首页，使用了ViewBinding示例
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
class MainMeFragment : BaseMvvmViewBindingFragment<FragmentMeMainBinding, MainMeViewModel>() {

    companion object {
        fun newsInstance(): MainMeFragment {
            return MainMeFragment()
        }
    }


    private var imagePickerHelper: ImagePickerHelper? = null

    override fun onBindLayout(): Int = R.layout.fragment_me_main

    override fun initView(mView: View) {
        val config = ImagePickerHelper.with(ImagePickerHelper.Config())
            .setWeChat(true)
            .setMimeType(0)
            .setShowOriginal(true)
            .setSave(true)
            .setMulti(true)
            .setMaxCount(6)
            .build()
        imagePickerHelper = ImagePickerHelper(
            activity, requireBinding().gridLayout,
            { items ->
                KLog.d(TAG, "选择的照片数据" + Gson().toJson(items))
            }, config
        )
    }

    override fun initData() {

    }

    override fun initListener() {
        requireBinding().button1.setOnClickListener(this::onClick)
        requireBinding().button2.setOnClickListener(this::onClick)
        requireBinding().button3.setOnClickListener(this::onClick)
        requireBinding().button4.setOnClickListener(this::onClick)
    }

    override fun enableToolbar(): Boolean = true

    override fun getTootBarTitle(): String = getString(R.string.title_mine)

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
                "拍照模式设置成功".showToast()
            }
            R.id.button_2 -> {
                val config =
                    ImagePickerHelper.with(ImagePickerHelper.Config())
                        .setWeChat(true)
                        .setMimeType(1)
                        .setShowOriginal(true)
                        .setSave(true)
                        .setMulti(true)
                        .build()
                imagePickerHelper!!.setConfig(config)
                "视频模式设置成功".showToast()
            }
            R.id.button_3 -> {
                val trim = requireBinding().editText.text.toString().trim()
                if (trim.isBlank()) {
                    ToastUtil.showToastCenter("输入内容不能为空")
                    return
                }
                SaveStateTestActivity.start(mContext, trim)
            }
            R.id.button_4 -> {
                RoomTestActivity.start(mContext)
            }
        }
    }

    override fun initViewObservable() {

    }
}
