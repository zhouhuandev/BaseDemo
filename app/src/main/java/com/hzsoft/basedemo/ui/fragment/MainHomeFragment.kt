package com.hzsoft.basedemo.ui.fragment

import android.view.View
import com.hzsoft.basedemo.BR
import com.hzsoft.basedemo.R
import com.hzsoft.basedemo.adapter.MainHomeAdapter
import com.hzsoft.basedemo.databinding.FragmentHomeMainBinding
import com.hzsoft.basedemo.ui.fragment.viewmodel.MainHomeViewModel
import android.view.animation.AnimationUtils
import com.hzsoft.lib.base.utils.ThreadUtils
import com.hzsoft.lib.base.utils.ext.view.showToast
import com.hzsoft.lib.base.view.BaseMvvmRefreshDataBindingFragment
import com.hzsoft.lib.common.utils.EnvironmentUtil
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.log.KLog
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.utils.ext.launch
import com.hzsoft.lib.net.utils.ext.observe
import com.hzsoft.module.home.BR
import com.hzsoft.module.home.R
import com.hzsoft.module.home.adapter.MainHomeAdapter
import com.hzsoft.module.home.databinding.FragmentHomeMainBinding
import com.hzsoft.module.home.viewmodel.MainHomeViewModel
import kotlin.random.Random

/**
 * Describe:
 * 首页
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
class MainHomeFragment :
    BaseMvvmRefreshDataBindingFragment<FragmentHomeMainBinding, MainHomeViewModel>() {

    companion object {
        fun newsInstance(): MainHomeFragment {
            return MainHomeFragment()
        }
    }

    private lateinit var mAdapter: MainHomeAdapter

    override fun onBindVariableId(): MutableList<Pair<Int, Any>> {
        return arrayListOf(BR.viewModel to mViewModel)
    }

    override fun initViewObservable() {
        observe(mViewModel.recipesLiveData, ::handleRecipesList)
    }

    override fun onBindLayout(): Int = R.layout.fragment_home_main

    override fun initView(mView: View) {
        mAdapter = MainHomeAdapter()
        mAdapter.bindSkeletonScreen(
            requireBinding().mRecyclerView,
            com.hzsoft.lib.base.R.layout.skeleton_default_service_item,
            8
        )
    }

    override fun initData() {
        onRefreshEvent()
        KLog.d(TAG, EnvironmentUtil.Storage.getCachePath(mContext))
    }

    var firstLoad = true

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

    override fun onBindRefreshLayout(): Int = R.id.mRefreshLayout

    override fun enableRefresh(): Boolean = true

    override fun enableLoadMore(): Boolean = true

    override fun enableToolbar(): Boolean = true

    override fun getTootBarTitle(): String = getString(R.string.title_home)

    private fun handleRecipesList(resource: Resource<List<Demo>>) {
        resource.launch {
            it?.apply {
                bindListData(recipes = ArrayList(this))
            }
        }
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
            requireBinding().mRecyclerView.apply {
                layoutAnimation =
                    AnimationUtils.loadLayoutAnimation(mContext, R.anim.layout_from_right)
                scheduleLayoutAnimation()
            }
        }
    }
}
