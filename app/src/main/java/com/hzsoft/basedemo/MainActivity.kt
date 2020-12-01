package com.hzsoft.basedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hzsoft.lib.common.mvvm.factory.ViewModelFactory
import com.hzsoft.lib.common.utils.showToast
import com.hzsoft.lib.net.dto.Demo
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.utils.observe
import com.wx.jetpack.core.utils.toJson
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var recipesListViewModel: RecipesListViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var viewModelFactory1 = ViewModelFactory()
        recipesListViewModel = viewModelFactory.create(RecipesListViewModel::class.java)
        observe(recipesListViewModel.recipesLiveData, ::handleRecipesList)
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