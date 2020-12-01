package com.hzsoft.basedemo.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hzsoft.basedemo.R
import com.hzsoft.basedemo.di.holder.NetComponentHolder
import com.hzsoft.basedemo.viewmodel.MainViewModel
import com.hzsoft.lib.common.mvvm.factory.ViewModelFactory
import com.hzsoft.lib.net.dto.Demo
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.utils.observe
import com.wx.jetpack.core.utils.toJson
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        NetComponentHolder.netComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = viewModelFactory.create(mainViewModel::class.java)
        mainViewModel.getRecipes()
        observe(mainViewModel.recipesLiveData, ::handleRecipesList)
    }

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