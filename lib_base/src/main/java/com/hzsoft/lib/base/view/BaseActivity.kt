package com.hzsoft.lib.base.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.View
import android.view.ViewStub
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.hzsoft.lib.base.R
import com.hzsoft.lib.base.event.common.BaseActivityEvent
import com.hzsoft.lib.base.mvvm.view.BaseView
import com.hzsoft.lib.base.utils.NetUtil
import com.hzsoft.lib.base.widget.LoadingInitView
import com.hzsoft.lib.base.widget.LoadingTransView
import com.hzsoft.lib.base.widget.NetErrorView
import com.hzsoft.lib.base.widget.NoDataView
import com.hzsoft.lib.log.KLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 基础页面
 * @author zhouhuan
 * @time 2020/12/2
 */
abstract class BaseActivity : AppCompatActivity(), BaseView {

    companion object {
        val TAG: String = this::class.java.simpleName
    }

    protected lateinit var mContext: Context

    protected var mTxtTitle: TextView? = null
    protected var tvToolbarRight: TextView? = null
    protected var ivToolbarRight: ImageView? = null
    protected var mToolbar: Toolbar? = null

    private var mNetErrorView: NetErrorView? = null
    private var mNoDataView: NoDataView? = null
    private var mLoadingInitView: LoadingInitView? = null
    private var mLoadingTransView: LoadingTransView? = null

    private lateinit var mViewStubToolbar: ViewStub
    private lateinit var mViewStubContent: ViewStub
    private lateinit var mViewStubInitLoading: ViewStub
    private lateinit var mViewStubTransLoading: ViewStub
    private lateinit var mViewStubNoData: ViewStub
    private lateinit var mViewStubError: ViewStub

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        mContext = newBase ?: this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startTime = SystemClock.elapsedRealtime()
        initFullScreen()
        setContentView(R.layout.activity_root)
        init()
        initBundle()
        initCommonView()
        initView()
        initListener()

        val totalTime = SystemClock.elapsedRealtime() - startTime
        KLog.e(TAG, "onCreate: 当前进入的Activity: $localClassName 初始化时间:$totalTime ms")
    }

    protected open fun initFullScreen() {
        if (enableAllowFullScreen()) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }

    protected open fun initCommonView() {

        mViewStubToolbar = findViewById(R.id.view_stub_toolbar)
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
        initContentView(mViewStubContent)
    }

    open fun initContentView(mViewStubContent: ViewStub) {
        mViewStubContent.layoutResource = onBindLayout()
        mViewStubContent.inflate()
    }

    protected fun initToolbar(view: View) {
        mToolbar = view.findViewById(R.id.toolbar_root)
        mTxtTitle = view.findViewById(R.id.toolbar_title)
        tvToolbarRight = view.findViewById(R.id.tv_toolbar_right)
        ivToolbarRight = view.findViewById(R.id.iv_toolbar_right)
        mToolbar?.apply {
            setSupportActionBar(this)
            //是否显示标题
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            setNavigationOnClickListener { onBackPressed() }

            if (enableToolBarLeft()) {
                //设置是否添加显示NavigationIcon.如果要用
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                //设置NavigationIcon的icon.可以是Drawable ,也可以是ResId
                setNavigationIcon(getToolBarLeftIcon())
            }
            //当标题栏右边的文字不为空时进行填充文字信息
            if (getToolBarRightTxt().isNotBlank()) {
                tvToolbarRight?.apply {
                    text = getToolBarRightTxt()
                    visibility = View.VISIBLE
                    setOnClickListener(getToolBarRightTxtClick())
                }
            }
            //当标题右边的图标不为 默认0时进行填充图标
            if (getToolBarRightImg() != 0) {
                ivToolbarRight?.apply {
                    setImageResource(getToolBarRightImg())
                    visibility = View.VISIBLE
                    setOnClickListener(getToolBarRightImgClick())
                }
            }
        }
    }

    override fun onTitleChanged(title: CharSequence, color: Int) {
        super.onTitleChanged(title, color)
        if (mTxtTitle != null && !TextUtils.isEmpty(title)) {
            mTxtTitle?.text = title
        }
        //可以再次覆盖设置title
        val tootBarTitle = getTootBarTitle()
        if (mTxtTitle != null && !TextUtils.isEmpty(tootBarTitle)) {
            mTxtTitle?.text = tootBarTitle
        }
    }

    override fun onResume() {
        super.onResume()
        initData()
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
        return R.drawable.navbar_icon_return
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
     * 是否打开EventBus
     */
    open fun enableEventBus(): Boolean = false

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
        if (enableEventBus()) EventBus.getDefault().unregister(this)
    }

    private fun init() {
        if (enableEventBus()) EventBus.getDefault().register(this)
    }

    open fun initBundle() {

    }

    abstract fun onBindLayout(): Int

    abstract fun initView()
    abstract override fun initData()

    override fun initListener() {}

    override fun finishActivity() {
        finish()
    }

    open fun enableAllowFullScreen(): Boolean {
        return false
    }

    open fun enableToolbar(): Boolean {
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

    open fun showInitLoadView(show: Boolean) {
        if (mLoadingInitView == null) {
            val view = mViewStubInitLoading.inflate()
            mLoadingInitView = view.findViewById(R.id.view_init_loading)
        }
        mLoadingInitView?.visibility = if (show) View.VISIBLE else View.GONE
        mLoadingInitView?.loading(show)
    }


    open fun showNetWorkErrView(show: Boolean) {
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


    open fun showNoDataView(show: Boolean) {
        if (mNoDataView == null) {
            val view = mViewStubNoData.inflate()
            mNoDataView = view.findViewById(R.id.view_no_data)
        }
        mNoDataView?.visibility = if (show) View.VISIBLE else View.GONE
    }

    open fun showNoDataView(show: Boolean, resid: Int) {
        showNoDataView(show)
        if (show) {
            mNoDataView?.setNoDataView(resid)
        }
    }

    open fun showTransLoadingView(show: Boolean) {
        if (mLoadingTransView == null) {
            val view = mViewStubTransLoading.inflate()
            mLoadingTransView = view.findViewById(R.id.view_trans_loading)
        }
        mLoadingTransView?.visibility = if (show) View.VISIBLE else View.GONE
        mLoadingTransView?.loading(show)


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun <T> onEvent(event: BaseActivityEvent<T>) {
    }

    open fun startActivity(clz: Class<*>?, bundle: Bundle?) {
        val intent = Intent(this, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * 跳转方式
     * 使用方法 startActivity<TargetActivity> { putExtra("param1", "data1") }
     */
    inline fun <reified T> startActivity(block: Intent.() -> Unit) {
        val intent = Intent(this, T::class.java)
        intent.block()
        startActivity(intent)
    }

    private var mLastButterKnifeClickTime: Long = 0

    /**
     * 是否快速点击
     *
     * @return true 是
     */
    open fun beFastClick(): Boolean {
        val currentClickTime = System.currentTimeMillis()
        val flag = currentClickTime - mLastButterKnifeClickTime < 400L
        mLastButterKnifeClickTime = currentClickTime
        return flag
    }

    open fun onClick(v: View?) {

    }
}
