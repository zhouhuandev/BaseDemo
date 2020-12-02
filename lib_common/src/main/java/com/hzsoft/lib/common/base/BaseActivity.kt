package com.hzsoft.lib.common.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.alibaba.android.arouter.launcher.ARouter
import com.fly.tour.common.util.log.KLog
import com.hzsoft.lib.common.R
import com.hzsoft.lib.common.event.common.BaseActivityEvent
import com.hzsoft.lib.common.manager.ActivityManager
import com.hzsoft.lib.common.mvvm.view.BaseView
import com.hzsoft.lib.common.utils.NetUtil
import com.hzsoft.lib.common.wight.LoadingInitView
import com.hzsoft.lib.common.wight.LoadingTransView
import com.hzsoft.lib.common.wight.NetErrorView
import com.hzsoft.lib.common.wight.NoDataView
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 基础页面
 * @author zhouhuan
 * @time 2020/12/2
 */
abstract class BaseActivity : RxAppCompatActivity(), BaseView {
    protected lateinit var mContext: Context

    protected lateinit var mTxtTitle: TextView
    protected lateinit var tvToolbarRight: TextView
    protected lateinit var ivToolbarRight: ImageView
    protected lateinit var mToolbar: Toolbar

    protected var mNetErrorView: NetErrorView? = null
    protected var mNoDataView: NoDataView? = null
    protected var mLoadingInitView: LoadingInitView? = null
    protected var mLoadingTransView: LoadingTransView? = null

    private lateinit var mViewStubToolbar: ViewStub
    private lateinit var mViewStubContent: ViewStub
    private lateinit var mViewStubInitLoading: ViewStub
    private lateinit var mViewStubTransLoading: ViewStub
    private lateinit var mViewStubNoData: ViewStub
    private lateinit var mViewStubError: ViewStub
    private val isrefresh = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_root)
        mContext = this
        initCommonView()
        ARouter.getInstance().inject(this)
        initListener()
        initData()
        EventBus.getDefault().register(this)
        ActivityManager.getInstance()?.addActivity(this)
        KLog.e(TAG, "onCreate: 当前进入的Activity: $localClassName")
    }

    protected open fun initCommonView() {
        mViewStubToolbar = findViewById(R.id.view_stub_toolbar)
        mViewStubContent = findViewById(R.id.view_stub_content)
        mViewStubContent = findViewById(R.id.view_stub_content)
        mViewStubInitLoading = findViewById(R.id.view_stub_init_loading)
        mViewStubTransLoading = findViewById(R.id.view_stub_trans_loading)
        mViewStubError = findViewById(R.id.view_stub_error)
        mViewStubNoData = findViewById(R.id.view_stub_nodata)

        if (enableToolbar()) {
            mViewStubToolbar.layoutResource = onBindToolbarLayout()
            val view = mViewStubToolbar.inflate()
            initToolbar(view)
        }
        mViewStubContent.layoutResource = onBindLayout()
        mViewStubContent.inflate()
    }

    protected fun initToolbar(view: View) {
        mToolbar = view.findViewById(R.id.toolbar_root)
        mTxtTitle = view.findViewById(R.id.toolbar_title)
        tvToolbarRight = view.findViewById(R.id.tv_toolbar_right)
        ivToolbarRight = view.findViewById(R.id.iv_toolbar_right)
        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
            //是否显示标题
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            mToolbar.setNavigationOnClickListener { onBackPressed() }

            if (enableToolBarLeft()) {
                //设置是否添加显示NavigationIcon.如果要用
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                //设置NavigationIcon的icon.可以是Drawable ,也可以是ResId
                mToolbar.setNavigationIcon(getToolBarLeftIcon())
                mToolbar.setNavigationOnClickListener { onBackPressed() }
            }
            //当标题栏右边的文字不为空时进行填充文字信息
            if (tvToolbarRight != null && !TextUtils.isEmpty(getToolBarRightTxt())) {
                tvToolbarRight.setText(getToolBarRightTxt())
                tvToolbarRight.visibility = View.VISIBLE
                tvToolbarRight.setOnClickListener(getToolBarRightTxtClick())
            }
            //当标题右边的图标不为 默认0时进行填充图标
            if (ivToolbarRight != null && getToolBarRightImg() != 0) {
                ivToolbarRight.setImageResource(getToolBarRightImg())
                ivToolbarRight.visibility = View.VISIBLE
                ivToolbarRight.setOnClickListener(getToolBarRightImgClick())
            }
        }
    }

    override fun onTitleChanged(title: CharSequence, color: Int) {
        super.onTitleChanged(title, color)
        if (mTxtTitle != null && !TextUtils.isEmpty(title)) {
            mTxtTitle.text = title
        }
        //可以再次覆盖设置title
        val tootBarTitle = getTootBarTitle()
        if (mTxtTitle != null && !TextUtils.isEmpty(tootBarTitle)) {
            mTxtTitle.text = tootBarTitle
        }
    }

    open fun getTootBarTitle(): String {
        return ""
    }

    /**
     * 设置返回按钮的图样，可以是Drawable ,也可以是ResId
     * 注：仅在 enableToolBarLeft 返回为 true 时候有效
     *
     * @return
     */
    open fun getToolBarLeftIcon(): Int {
        return R.drawable.ic_white_black_45dp
    }

    /**
     * 是否打开返回
     *
     * @return
     */
    open fun enableToolBarLeft(): Boolean {
        return false
    }

    /**
     * 设置标题右边显示文字
     *
     * @return
     */
    open fun getToolBarRightTxt(): String {
        return ""
    }

    /**
     * 设置标题右边显示 Icon
     *
     * @return int resId 类型
     */
    open fun getToolBarRightImg(): Int {
        return 0
    }

    /**
     * 右边文字监听回调
     * @return
     */
    open fun getToolBarRightTxtClick(): View.OnClickListener? {
        return null
    }

    /**
     * 右边图标监听回调
     * @return
     */
    open fun getToolBarRightImgClick(): View.OnClickListener? {
        return null
    }

    open fun onBindToolbarLayout(): Int {
        return R.layout.common_toolbar
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        ActivityManager.getInstance()?.finishActivity(this)
    }

    abstract fun onBindLayout(): Int

    abstract override fun initData()

    override fun initListener() {}

    override fun finishActivity() {
        finish()
    }

    fun enableToolbar(): Boolean {
        return true
    }

    override fun showInitLoadView() {
        showInitLoadView(true)
    }

    override fun hideInitLoadView() {
        showInitLoadView(false)
    }

    override fun showTransLoadingView() {
        showTransLoadingView(true)
    }

    override fun hideTransLoadingView() {
        showTransLoadingView(false)
    }

    override fun showNoDataView() {
        showNoDataView(true)
    }

    override fun showNoDataView(resid: Int) {
        showNoDataView(true, resid)
    }

    override fun hideNoDataView() {
        showNoDataView(false)
    }

    override fun hideNetWorkErrView() {
        showNetWorkErrView(false)
    }

    override fun showNetWorkErrView() {
        showNetWorkErrView(true)
    }

    private fun showInitLoadView(show: Boolean) {
        if (mLoadingInitView == null) {
            val view = mViewStubInitLoading.inflate()
            mLoadingInitView = view.findViewById(R.id.view_init_loading)
        }
        mLoadingInitView?.visibility = if (show) View.VISIBLE else View.GONE
        mLoadingInitView?.loading(show)
    }


    private fun showNetWorkErrView(show: Boolean) {
        if (mNetErrorView == null) {
            val view = mViewStubError.inflate()
            mNetErrorView = view.findViewById(R.id.view_net_error)
            mNetErrorView?.setOnClickListener(View.OnClickListener {
                if (!NetUtil.checkNetToast()) {
                    return@OnClickListener
                }
                hideNetWorkErrView()
                initData()
            })
        }
        mNetErrorView?.visibility = if (show) View.VISIBLE else View.GONE
    }


    private fun showNoDataView(show: Boolean) {
        if (mNoDataView == null) {
            val view = mViewStubNoData.inflate()
            mNoDataView = view.findViewById(R.id.view_no_data)
        }
        mNoDataView?.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showNoDataView(show: Boolean, resid: Int) {
        showNoDataView(show)
        if (show) {
            mNoDataView?.setNoDataView(resid)
        }
    }

    private fun showTransLoadingView(show: Boolean) {
        if (mLoadingTransView == null) {
            val view = mViewStubTransLoading.inflate()
            mLoadingTransView = view.findViewById(R.id.view_trans_loading)
        }
        mLoadingTransView?.visibility = if (show) View.VISIBLE else View.GONE
        mLoadingTransView?.loading(show)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun <T> onEvent(event: BaseActivityEvent<T>) {
    }

    companion object {
        protected val TAG = BaseActivity::class.java.getSimpleName()
    }
}