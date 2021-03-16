package com.hzsoft.basedemo.ui.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hzsoft.basedemo.BR
import com.hzsoft.basedemo.R
import com.hzsoft.basedemo.adapter.MainHomeAdpater
import com.hzsoft.basedemo.databinding.FragmentHomeMainBinding
import com.hzsoft.basedemo.ui.fragment.viewmodel.MainHomeViewModel
import com.hzsoft.lib.base.utils.log.KLog
import com.hzsoft.lib.base.view.BaseMvvmRefreshDataBindingFragment
import com.hzsoft.lib.common.utils.EnvironmentUtil
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.local.entity.UserTestRoom
import com.hzsoft.lib.net.utils.ext.observe
import com.hzsoft.lib.net.utils.toJson
import kotlinx.android.synthetic.main.fragment_home_main.*

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

    private lateinit var mAdapter: MainHomeAdpater

    override fun onBindVariableId(): Int = BR.viewModel

    override fun onBindViewModel(): Class<MainHomeViewModel> = MainHomeViewModel::class.java

    override fun initViewObservable() {
        observe(mViewModel.recipesLiveData, ::handleRecipesList)
        observe(mViewModel.userTestRoomLiveData, ::handleUserTestRoomList)
    }

    override fun onBindLayout(): Int = R.layout.fragment_home_main

    override fun initView(mView: View) {
        mAdapter = MainHomeAdpater(mContext)
        mBinding.mRecyclerView.adapter = mAdapter
        mBinding.mRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }


    override fun initData() {
        onRefreshEvent()
        KLog.d(TAG, EnvironmentUtil.Storage.getCachePath(mContext))
    }

    override fun initListener() {
        addUser.setOnClickListener(this::onClick)
        selectUser.setOnClickListener(this::onClick)
    }

    var index = 0

    override fun onClick(v: View?) {
        if (beFastClick()) {
            return
        }
        when (v?.id) {
            R.id.addUser -> {
                val userTestRoom = UserTestRoom("zhou", "huan", index)
                mViewModel.insertUserTestRoom(userTestRoom)
                index++
            }
            R.id.selectUser -> {
                mViewModel.getUserTestRoom()
            }
        }
    }

    override fun onRefreshEvent() {
        mViewModel.refreshData()
    }

    override fun onLoadMoreEvent() {
        mViewModel.loadMore()
    }

    override fun onAutoLoadEvent() {

    }

    override fun onBindRreshLayout(): Int = R.id.mDaisyRefreshLayout

    override fun enableRefresh(): Boolean = true

    override fun enableLoadMore(): Boolean = false

    override fun enableToolbar(): Boolean = true

    override fun getTootBarTitle(): String = "Home"

    private fun handleRecipesList(status: Resource<List<Demo>>) {
        when (status) {
            is Resource.Success -> status.data?.let {
                // stopRefresh(it)
                bindListData(recipes = it)
            }
            is Resource.DataError -> {
                status.errorCode?.let { KLog.e("zhouhuan", "--------->$it") }
            }
        }
    }

    private fun handleUserTestRoomList(status: Resource<List<UserTestRoom>>) {
        when (status) {
            is Resource.Success -> status.data?.let { bindListData2(it) }
            is Resource.DataError -> {
                status.errorCode?.let { KLog.e(TAG, "--------->$it") }
            }
        }
    }

    private fun bindListData(recipes: List<Demo>) {
        mAdapter.setNewList(recipes)
    }

    private fun bindListData2(userTestRoom: List<UserTestRoom>) {
        textView2.text = userTestRoom.toJson()
    }
}
