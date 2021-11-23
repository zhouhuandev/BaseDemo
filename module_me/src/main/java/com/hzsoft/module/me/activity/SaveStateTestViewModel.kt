package com.hzsoft.module.me.activity

import androidx.lifecycle.SavedStateHandle
import com.hzsoft.lib.base.mvvm.viewmodel.BaseViewModel

/**
 *
 *
 * @author zhouhuan
 * @time 2021/11/23
 */
class SaveStateTestViewModel(state: SavedStateHandle) :
    BaseViewModel() {

    // 将Key声明为常量
    companion object {
        const val JUMP_PAGE = "jump_page"
        const val COUNT = "count"
    }

    val savedStateHandle = state

    /**
     * 获取页面跳转信息，可以进行网络请求等内容信息
     */
    fun getJumpPage(): String {
        return savedStateHandle.get<String>(JUMP_PAGE).orEmpty()
    }

    fun saveCount(count: Int) {
        // 存储 count 对应的数据
        savedStateHandle.set(COUNT, count)
    }

    fun getCount(): Int {
        // 从 saveStateHandle 中取出当前 count 信息
        return savedStateHandle.get<Int>(COUNT) ?: 0
    }
}