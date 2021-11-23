package com.hzsoft.lib.common.wight

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.hzsoft.lib.common.R
import com.hzsoft.lib.common.utils.DisplayUtil
import com.hzsoft.lib.log.KLog

/**
 * 公共弹窗
 * @author zhouhuan
 * @time 2021-08-19 18:11
 */
class CommonDialogFragment : DialogFragment() {
    private var mOnDialogClickListener: OnDialogClickListener? = null

    override fun dismiss() {
        super.dismiss()
        isShowing = false
        KLog.v(TAG, "dismiss start...")
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

    fun setOnDialogClickListener(onDialogClickListener: OnDialogClickListener?): CommonDialogFragment {
        mOnDialogClickListener = onDialogClickListener
        return this
    }

    interface OnDialogClickListener {
        fun onLeftBtnClick(view: View)
        fun onRightBtnClick(view: View)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            resources.displayMetrics.widthPixels - DisplayUtil.dip2px(40f) * 2,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_common_dialog, container, false)
        val arguments = arguments
        var title: String? = null
        var describe: String? = null
        var leftbtn: String? = null
        var rightbtn: String? = null
        var rightBtnTextColor = 0
        if (arguments != null) {
            title = arguments.getString("title")
            describe = arguments.getString("describe")
            leftbtn = arguments.getString("leftBtn")
            rightbtn = arguments.getString("rightBtn")
            rightBtnTextColor = arguments.getInt("rightBtnColor", 0)
        }
        val txtTitle = view.findViewById<TextView>(R.id.txt_common_dialog_title)
        val txtDescribe = view.findViewById<TextView>(R.id.txt_common_dialog_describe)
        val btnLeft = view.findViewById<Button>(R.id.btn_common_dialog_left)
        val btnRight = view.findViewById<Button>(R.id.btn_common_dialog_right)
        val btnHalving = view.findViewById<View>(R.id.view_halving_line)
        if (!TextUtils.isEmpty(title)) {
            txtTitle.text = title
        } else {
            txtTitle.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(describe)) {
            txtDescribe.text = describe
        }
        if (!TextUtils.isEmpty(leftbtn)) {
            btnHalving.visibility = View.VISIBLE
            btnLeft.visibility = View.VISIBLE
            btnLeft.text = leftbtn
        } else {
            btnHalving.visibility = View.GONE
            btnLeft.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(rightbtn)) {
            btnRight.text = rightbtn
        }
        if (rightBtnTextColor != 0) {
            btnRight.setTextColor(rightBtnTextColor)
        }
        btnLeft.setOnClickListener { v ->
            mOnDialogClickListener?.onLeftBtnClick(v)
            dismiss()
        }
        btnRight.setOnClickListener { v ->
            mOnDialogClickListener?.onRightBtnClick(v)
            dismiss()
        }
        return view
    }

    class Builder {
        var title: String = ""
        var describe: String = ""
        var leftBtn: String = ""
        var rightBtn: String = ""
        var btnRightTextColor: Int = 0
        var mListener: OnDialogClickListener? = null

        @JvmName("setTitle1")
        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        @JvmName("setDescribe1")
        fun setDescribe(describe: String): Builder {
            this.describe = describe
            return this
        }

        @JvmName("setLeftBtn1")
        fun setLeftBtn(leftBtn: String): Builder {
            this.leftBtn = leftBtn
            return this
        }

        @JvmName("setRightBtn1")
        fun setRightBtn(rightBtn: String): Builder {
            this.rightBtn = rightBtn
            return this
        }

        @JvmName("setOnDialogClickListener1")
        fun setOnDialogClickListener(listener: OnDialogClickListener): Builder {
            this.mListener = listener
            return this
        }

        @JvmName("setRightBtnTextColor1")
        fun setRightBtnTextColor(rightBtnTextColor: Int): Builder {
            this.btnRightTextColor = rightBtnTextColor
            return this
        }

        fun build(): CommonDialogFragment {
            return newInstance(this)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        isShowing = false
        KLog.v(TAG, "onCancel start...")
    }

    companion object {
        val TAG: String = CommonDialogFragment::class.java.simpleName

        const val fragmentTag = "common_dialog"

        /**
         * 避免弹多个dialog
         */
        var isShowing = false

        fun newInstance(builder: Builder): CommonDialogFragment {
            val commonDialogFragment = CommonDialogFragment()
            val args = Bundle()
            args.putString("title", builder.title)
            args.putString("describe", builder.describe)
            args.putString("leftBtn", builder.leftBtn)
            args.putString("rightBtn", builder.rightBtn)
            args.putInt("rightBtnColor", builder.btnRightTextColor)
            commonDialogFragment.setOnDialogClickListener(builder.mListener)
            commonDialogFragment.arguments = args
            return commonDialogFragment
        }
    }
}
