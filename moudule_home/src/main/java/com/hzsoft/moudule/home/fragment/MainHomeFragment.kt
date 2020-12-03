package com.hzsoft.moudule.home.fragment

import android.util.Log
import com.hzsoft.lib.common.base.BaseMvvmFragment
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.utils.observe
import com.hzsoft.moudule.home.R
import com.hzsoft.moudule.home.viewmodel.MainHomeViewModel
import com.wx.jetpack.core.utils.toJson
import kotlinx.android.synthetic.main.fragment_home_main.*

/**
 * Describe:
 * 首页
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
class MainHomeFragment : BaseMvvmFragment<MainHomeViewModel>() {

    companion object {
        fun newsInstance(): MainHomeFragment {
            return MainHomeFragment()
        }
    }

    override fun onBindViewModel(): Class<MainHomeViewModel> = MainHomeViewModel::class.java

    override fun initViewObservable() {
        observe(mViewModel.recipesLiveData, ::handleRecipesList)
    }

    override fun onBindLayout(): Int = R.layout.fragment_home_main

    override fun initData() {
        mViewModel.getRecipes()
    }

    override fun enableToolbar(): Boolean = true

    override fun getTootBarTitle(): String = resources.getString(R.string.module_home)

    private fun handleRecipesList(status: Resource<List<Demo>>) {
        when (status) {
            is Resource.Success -> status.data?.let { bindListData(recipes = it) }
            is Resource.DataError -> {
                status.errorCode?.let { Log.e("zhouhuan", "--------->$it") }
            }
        }
    }

    private fun bindListData(recipes: List<Demo>) {
        textView.text = recipes.toJson()
    }
}