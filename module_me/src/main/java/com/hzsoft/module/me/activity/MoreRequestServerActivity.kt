package com.hzsoft.module.me.activity

import android.content.Context
import android.view.animation.AnimationUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.hzsoft.lib.base.module.constons.ARouteConstants
import com.hzsoft.lib.base.utils.ThreadUtils
import com.hzsoft.lib.base.utils.ext.view.showToast
import com.hzsoft.lib.base.view.BaseMvvmRefreshDataBindingActivity
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.utils.ext.launch
import com.hzsoft.module.me.BR
import com.hzsoft.module.me.R
import com.hzsoft.module.me.activity.viewmodel.MoreRequestServerViewModel
import com.hzsoft.module.me.adapter.MoreRequestDemoAdapter
import com.hzsoft.module.me.databinding.ActivityMoreRequestServerBinding
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 *
 *
 * @author <a href="mailto:zhouhuandev@gmail.com" rel="nofollow">zhouhuan</a>
 * @since 2024/3/15 13:06
 */
@Route(path = ARouteConstants.Me.ME_MORE_REQUEST_SERVER, name = "多请求服务域名示例")
class MoreRequestServerActivity :
    BaseMvvmRefreshDataBindingActivity<ActivityMoreRequestServerBinding, MoreRequestServerViewModel>() {

    companion object {
        fun start(context: Context) {
            ARouter.getInstance().build(ARouteConstants.Me.ME_MORE_REQUEST_SERVER)
                .navigation(context)
        }
    }

    private lateinit var mAdapter: MoreRequestDemoAdapter

    override fun getTootBarTitle(): String = "多域名请求示例"

    override fun enableToolBarLeft(): Boolean = true

    override fun onBindRefreshLayout(): Int = R.id.mRefreshLayout

    override fun enableRefresh(): Boolean = true

    override fun enableLoadMore(): Boolean = true

    override fun onBindVariableId(): MutableList<Pair<Int, Any>> {
        return arrayListOf(BR.viewModel to mViewModel)
    }

    override fun ActivityMoreRequestServerBinding.onClear() {

    }

    override fun initViewObservable() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.recipesFlow.collect { resource ->
                    resource.launch {
                        it?.apply {
                            bindListData(recipes = ArrayList(this))
                        }
                    }
                }
            }
        }
    }

    override fun onBindLayout(): Int = R.layout.activity_more_request_server

    override fun initView() {
        mAdapter = MoreRequestDemoAdapter()
        mAdapter.bindSkeletonScreen(
            requireBinding().mRecyclerView,
            com.hzsoft.lib.base.R.layout.skeleton_default_service_item,
            8
        )
    }

    override fun initData() {
        onRefreshEvent()
    }

    private var firstLoad = true

    override fun onRefreshEvent() {
        // 为了展示骨架屏
        if (firstLoad) {
            firstLoad = false
            ThreadUtils.runOnUiThread({ mViewModel.refreshData() }, 1000)
        } else {
            mViewModel.refreshData()
        }
    }

    override fun onLoadMoreEvent() {
        mViewModel.loadMore()
    }

    private fun bindListData(recipes: ArrayList<Demo>) {
        if (isRefresh) {
            val first = Random.nextInt(0, recipes.size / 2)
            val second = Random.nextInt(recipes.size / 2, recipes.size)
            val cache = ArrayList<Demo>()
            for (i in first.coerceAtMost(second)..first.coerceAtLeast(second)) {
                cache.add(recipes[i])
            }
            mViewModel.itemCount = cache.size
            mAdapter.setNewInstance(cache)
            // 执行列表动画
            requireBinding().mRecyclerView.apply {
                layoutAnimation =
                    AnimationUtils.loadLayoutAnimation(mContext, R.anim.layout_fall_down)
                scheduleLayoutAnimation()
            }
        } else {
            val first = Random.nextInt(0, recipes.size)
            val second = Random.nextInt(0, recipes.size)
            val cache = ArrayList<Demo>()
            for (i in first.coerceAtMost(second)..first.coerceAtLeast(second)) {
                cache.add(recipes[i])
            }
            mViewModel.itemCount += cache.size
            mAdapter.addData(cache)
            "加载了${cache.size}条数据".showToast()
            // 执行列表动画
            // requireBinding().mRecyclerView.apply {
            //     layoutAnimation =
            //         AnimationUtils.loadLayoutAnimation(mContext, R.anim.layout_from_right)
            //     scheduleLayoutAnimation()
            // }
        }
    }
}