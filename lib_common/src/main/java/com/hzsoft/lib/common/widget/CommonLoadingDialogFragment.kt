package com.hzsoft.lib.common.widget

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.hzsoft.lib.common.R

/**
 * 加载对话框
 *
 * @author zhouhuan
 * @time 2021/9/15
 */
class CommonLoadingDialogFragment : DialogFragment() {
    private var mOnDismissListener: OnDismissListener? = null

    /**
     * 避免弹多个dialog
     */
    private var isShowing = false

    /**
     * 是否第一次展示，主要做相应的动画使用
     */
    private var isFirst = true

    private var rootView: FrameLayout? = null
    private lateinit var linearLoadView: LinearLayout
    private lateinit var tvTitle: TextView

    /**
     * 圆角
     */
    private val borderRadius = 15F

    /**
     * 动画时长
     */
    private val animationDuration = 280L

    /**
     * 显示文字
     */
    private var title: CharSequence? = null

    /**
     * 延迟自动消失,单位ms，当延迟消失时间大于等于1000ms的时候生效
     */
    private var delayDismissTime = 0L

    /**
     * 是否正在显示
     * @return true:显示 false:不显示
     */
    fun isShow() = isShowing

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        defaultConfig()
        Log.v(TAG, "onCancel start...")
    }

    override fun dismiss() {
        super.dismiss()
        defaultConfig()
        mOnDismissListener?.onDismiss()
        Log.v(TAG, "dismiss start...")
    }

    fun show(fragmentManager: FragmentManager) {
        if (!this.isAdded) {
            this.show(fragmentManager, fragmentTag)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (isShowing) {
            return
        }
        isShowing = true
        super.show(manager, tag)
    }

    fun setOnDismissListener(onDismissListener: OnDismissListener?): CommonLoadingDialogFragment {
        mOnDismissListener = onDismissListener
        return this
    }

    interface OnDismissListener {
        /**
         * 消失动画执行完毕后执行
         */
        fun onDismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.common_center_loading, container, false) as FrameLayout

        val arguments = arguments
        if (arguments != null) {
            title = arguments.getCharSequence("title")
            delayDismissTime = arguments.getLong("delayDismissTime", 0L)
        }

        linearLoadView = rootView!!.findViewById(R.id.linear_load_view)
        linearLoadView.background = createDrawable(
            Color.parseColor("#CF000000"),
            borderRadius
        )
        tvTitle = rootView!!.findViewById(R.id.tv_title)
        setup()
        return rootView!!
    }

    /**
     * 设置初始化数据以及动画信息
     */
    private fun setup() {
        if (isShowing) {
            rootView?.post {
                if (!isFirst) {
                    val set = TransitionSet()
                        .setDuration(animationDuration)
                        .addTransition(Fade()).addTransition(ChangeBounds())
                    TransitionManager.beginDelayedTransition(rootView!!, set)
                }
                isFirst = false
                title?.let {
                    if (it.isNotBlank()) {
                        tvTitle.visibility = VISIBLE
                        tvTitle.text = title
                    } else {
                        tvTitle.visibility = GONE
                    }
                }
            }
            if (delayDismissTime >= 1000) {
                rootView?.postDelayed({ dismiss() }, delayDismissTime)
            }
        }
    }

    /**
     * 设置提示语
     */
    fun setTitle(title: CharSequence?): CommonLoadingDialogFragment = apply {
        this.title = title
        setup()
    }

    /**
     * 默认配置
     */
    private fun defaultConfig() {
        isShowing = false
        isFirst = true
        tvTitle.text = ""
        tvTitle.visibility = GONE
    }

    private fun createDrawable(color: Int, radius: Float): Drawable {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.setColor(color)
        drawable.cornerRadius = radius
        return drawable
    }

    class Builder {
        private var title: CharSequence = ""
        private var delayDismissTime = 0L
        private var mListener: OnDismissListener? = null

        fun asLoading(title: CharSequence): Builder = apply { this.title = title }

        fun asDelayDismiss(delayDismissTime: Long): Builder =
            apply { this.delayDismissTime = delayDismissTime }

        fun asOnDismissListener(listener: OnDismissListener): Builder =
            apply { this.mListener = listener }

        fun build(): CommonLoadingDialogFragment {
            return newInstance(this)
        }

        private fun newInstance(builder: Builder): CommonLoadingDialogFragment {
            val commonLoadingDialogFragment = CommonLoadingDialogFragment()
            val args = Bundle()
            commonLoadingDialogFragment.arguments = args.apply {
                with(args) {
                    putCharSequence("title", builder.title)
                    putLong("delayDismissTime", builder.delayDismissTime)
                    commonLoadingDialogFragment.setOnDismissListener(mListener)
                }
            }
            return commonLoadingDialogFragment
        }
    }

    companion object {
        val TAG: String = CommonLoadingDialogFragment::class.java.simpleName

        const val fragmentTag = "common_loading_dialog"
    }
}