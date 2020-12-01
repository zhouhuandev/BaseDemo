package com.hzsoft.basedemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hzsoft.lib.net.DataRepositorySource
import com.hzsoft.lib.net.dto.Demo
import com.hzsoft.lib.net.dto.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Describe:
 * <p></p>
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
class MainViewModel
@Inject constructor(private val dataRepositoryRepository: DataRepositorySource) : ViewModel() {

    val recipesLiveDataPrivate = MutableLiveData<Resource<List<Demo>>>()
    val recipesLiveData: LiveData<Resource<List<Demo>>> get() = recipesLiveDataPrivate

    fun getRecipes() {
        viewModelScope.launch {
            dataRepositoryRepository.requestRecipes().collect {
                recipesLiveDataPrivate.value = it
            }
        }
    }
}