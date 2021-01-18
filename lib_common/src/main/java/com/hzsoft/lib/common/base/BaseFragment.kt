package com.hzsoft.lib.common.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.fly.tour.common.util.log.KLog
import com.hzsoft.lib.common.R
import com.hzsoft.lib.common.event.common.BaseFragmentEvent
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
 *
 * @author zhouhuan
 * @Data 2020/12/3
 */
abstract class BaseFragment : Fragment(), BaseView {

    companion object {
        val TAG = this::class.java.simpleName
    }

    protected lateinit var mContext: Context

    protected lateinit var mActivity: RxAppCompatActivity
    protected lateinit var mView: View

    protected var mTxtTitle: TextView? = null
    protected var tvToolbarRight: TextView? = null
    protected var ivToolbarRight: ImageView? = null
    protected var mToolbar: Toolbar? = null

    protected var mNetErrorView: NetErrorView? = null
    protected var mNoDataView: NoDataView? = null
    protected var mLoadingInitView: LoadingInitView? = null
    protected var mLoadingTransView: LoadingTransView? = null

    protected lateinit var mViewStubToolbar: ViewStub
    protected lateinit var mViewStubContent: ViewStub
    protected lateinit var mViewStubInitLoading: ViewStub
    protected lateinit var mViewStubTransLoading: ViewStub
    protected lateinit var mViewStubNoData: ViewStub
    protected lateinit var mViewStubError: ViewStub
    private var isViewCreated = false
    private var isViewVisable = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = (activity as RxAppCompatActivity?)!!
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)
        KLog.e(TAG, "onCreate: 当前进入的Fragment: $javaClass")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_root, container, false)
        initCommonView(mView)
        return mView
    }

    open fun initCommonView(view: View) {
        mViewStubToolbar = view.findViewById(R.id.view_stub_toolbar)
        mViewStubContent = view.findViewById(R.id.view_stub_content)
        mViewStubInitLoading = view.findViewById(R.id.view_stub_init_loading)
        mViewStubTransLoading = view.findViewById(R.id.view_stub_trans_loading)
        mViewStubNoData = view.findViewById(R.id.view_stub_nodata)
        mViewStubError = view.findViewById(R.id.view_stub_error)

        if (enableToolbar()) {
            mViewStubToolbar.layoutResource = onBindToolbarLayout()
            val viewTooBbar = mViewStubToolbar.inflate()
            initTooBar(viewTooBbar)
        }
        mViewStubContent.layoutResource = onBindLayout()
        mViewStubContent.inflate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initView(mView)
        initListener()

        isViewCreated = true
    }

    /**
     * isVisibleToUser =true的时候代表当前页面可见，false 就是不可见
     * setUserVisibleHint(boolean isVisibleToUser) 是在 Fragment OnCreateView()方法之前调用的
     * 注：FragmentTransaction.setMaxLifecycle 处理 Lifecycle.State.RESUMED 则此函数不进行回调，失效
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isViewVisable = isVisibleToUser
        //如果启用了懒加载就进行懒加载，
        if (enableLazyData() && isViewVisable) {
            lazyLoad()
        }
    }

    /**
     * 懒加载机制 当页面可见的时候加载数据
     * 如果当前 FragmentTransaction.setMaxLifecycle 处理 Lifecycle.State.RESUMED 则 懒加载失效
     * 如果 FragmentTransaction.setMaxLifecycle 传入BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT ，
     * 则只有当前的Fragment处于Lifecycle.State.RESUMED状态。 所有其他片段的上限为Lifecycle.State.STARTED 。
     * 如果传递了BEHAVIOR_SET_USER_VISIBLE_HINT ，则所有片段都处于Lifecycle.State.RESUMED状态，
     * 并且将存在Fragment.setUserVisibleHint(boolean)回调
     */
    private fun lazyLoad() {
        //这里进行双重标记判断,必须确保onCreateView加载完毕且页面可见,才加载数据
        KLog.v("MYTAG", "lazyLoad start...")
        KLog.v("MYTAG", "isViewCreated:$isViewCreated")
        KLog.v("MYTAG", "isViewVisable:$isViewVisable")
        if (isViewCreated && isViewVisable) {
            Log.d(TAG, "lazyLoad: Successful")
            initData()
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false
            isViewVisable = false
        } else {
            Log.d(TAG, "lazyLoad: Fail")
        }
    }

    //默认不启用懒加载
    open fun enableLazyData(): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
        //如果启用了懒加载就进行懒加载，否则就进行预加载
        if (enableLazyData()) {
            lazyLoad()
        } else {
            initData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    protected fun initTooBar(view: View) {
        mToolbar = view.findViewById(R.id.toolbar_root)
        mTxtTitle = view.findViewById(R.id.toolbar_title)
        tvToolbarRight = view.findViewById(R.id.tv_toolbar_right)
        ivToolbarRight = view.findViewById(R.id.iv_toolbar_right)
        if (mToolbar != null) {
            mActivity.setSupportActionBar(mToolbar)
            mActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
            if (enableToolBarLeft()) {
                //设置是否添加显示NavigationIcon.如果要用
                mActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                //设置NavigationIcon的icon.可以是Drawable ,也可以是ResId
                mToolbar?.setNavigationIcon(getToolBarLeftIcon())
                mToolbar?.setNavigationOnClickListener { mActivity.onBackPressed() }
            }
            //当标题栏右边的文字不为空时进行填充文字信息
            if (tvToolbarRight != null && !TextUtils.isEmpty(getToolBarRightTxt())) {
                tvToolbarRight?.setText(getToolBarRightTxt())
                tvToolbarRight?.visibility = View.VISIBLE
                tvToolbarRight?.setOnClickListener(getToolBarRightTxtClick())
            }
            //当标题右边的图标不为 默认0时进行填充图标
            if (ivToolbarRight != null && getToolBarRightImg() != 0) {
                ivToolbarRight?.setImageResource(getToolBarRightImg())
                ivToolbarRight?.visibility = View.VISIBLE
                ivToolbarRight?.setOnClickListener(getToolBarRightImgClick())
            }
        }
        if (mTxtTitle != null) {
            mTxtTitle?.text = getTootBarTitle()
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


    abstract fun onBindLayout(): Int

    // abstract fun initView(mView: View)
    abstract override fun initData()

    override fun initListener() {}

    override fun finishActivity() {
        mActivity.finish()
    }

    open fun enableToolbar(): Boolean {
        return false
    }

    open fun onBindToolbarLayout(): Int {
        return R.layout.common_toolbar
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

    override fun showNetWorkErrView() {
        showNetWorkErrView(true)
    }

    override fun hideNetWorkErrView() {
        showNetWorkErrView(false)
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
    open fun <T> onEvent(event: BaseFragmentEvent<T>) {
    }

    open fun startActivity(clz: Class<*>?, bundle: Bundle?) {
        val intent = Intent(activity, clz)
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
        val intent = Intent(context, T::class.java)
        intent.block()
        startActivity(intent)
    }

    /**
     * 阿里路由跳转
     * open("填入你要跳转的ARouter路径") {
     * withString("你要传递extra的key", "你要传递extra的value")
     * }
     */
    open fun open(path: String, block: Postcard.() -> Unit = {}) {
        val postcard = ARouter.getInstance().build(path)
        postcard.block()
        postcard.navigation()
    }

    /**
     * 阿里路由跳转并结束当前页面
     * 建议：切记当前Activity只有一个Frament方式使用使用
     */
    open fun openWithFinish(path: String, block: Postcard.() -> Unit = {}) {
        open(path, block)
        mActivity.finish()
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