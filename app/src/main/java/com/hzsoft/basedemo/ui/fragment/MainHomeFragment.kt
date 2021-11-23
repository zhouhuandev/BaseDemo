package com.hzsoft.basedemo.ui.fragment

import android.view.View
import com.hzsoft.basedemo.BR
import com.hzsoft.basedemo.R
import com.hzsoft.basedemo.adapter.MainHomeAdapter
import com.hzsoft.basedemo.databinding.FragmentHomeMainBinding
import com.hzsoft.basedemo.ui.fragment.viewmodel.MainHomeViewModel
import com.hzsoft.lib.base.utils.ThreadUtils
import com.hzsoft.lib.base.view.BaseMvvmRefreshDataBindingFragment
import com.hzsoft.lib.common.utils.EnvironmentUtil
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.log.KLog
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.utils.ext.launch
import com.hzsoft.lib.net.utils.ext.observe
import java.util.*

/**
 * Describe:
 * 首页
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
class MainHomeFragment :
    BaseMvvmRefreshDataBindingFragment<Demo, FragmentHomeMainBinding, MainHomeViewModel>() {

    companion object {
        fun newsInstance(): MainHomeFragment {
            return MainHomeFragment()
        }
    }

    private lateinit var mAdapter: MainHomeAdapter

    override fun onBindVariableId(): Int = BR.viewModel

    override fun onBindViewModel(): Class<MainHomeViewModel> = MainHomeViewModel::class.java

    override fun initViewObservable() {
        observe(mViewModel.recipesLiveData, ::handleRecipesList)
    }

    override fun onBindLayout(): Int = R.layout.fragment_home_main

    override fun initView(mView: View) {
        mAdapter = MainHomeAdapter()
        mAdapter.bindSkeletonScreen(
            mBinding.mRecyclerView,
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

    override fun enableLoadMore(): Boolean = false

    override fun enableToolbar(): Boolean = true

    override fun getTootBarTitle(): String = "首页"

    private fun handleRecipesList(resource: Resource<List<Demo>>) {
        resource.launch {
            it?.apply {
                bindListData(recipes = ArrayList(this))
            }
        }
    }

    private fun bindListData(recipes: ArrayList<Demo>) {
        mAdapter.setNewInstance(recipes)
    }
}
