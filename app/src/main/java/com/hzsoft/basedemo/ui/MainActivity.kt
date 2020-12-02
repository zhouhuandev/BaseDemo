package com.hzsoft.basedemo.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.hzsoft.basedemo.R
import com.hzsoft.basedemo.viewmodel.MainViewModel
import com.hzsoft.lib.domain.entity.Demo
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.utils.observe
import com.wx.jetpack.core.utils.toJson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel.getRecipes()
        observe(mainViewModel.recipesLiveData, ::handleRecipesList)
    }

    private fun handleRecipesList(status: Resource<List<com.hzsoft.lib.domain.entity.Demo>>) {
        when (status) {
            is Resource.Success -> status.data?.let { bindListData(recipes = it) }
            is Resource.DataError -> {
                status.errorCode?.let { Log.e("zhouhuan", "--------->$it") }
            }
        }
    }

    private fun bindListData(recipes: List<com.hzsoft.lib.domain.entity.Demo>) {
        textView.text = recipes.toJson()
    }

}