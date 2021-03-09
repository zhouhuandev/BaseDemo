package com.hzsoft.basedemo.ui.activity

import androidx.fragment.app.Fragment
import com.hzsoft.basedemo.R
import com.hzsoft.basedemo.entity.MainChannel
import com.hzsoft.basedemo.ui.fragment.MainHomeFragment
import com.hzsoft.basedemo.ui.fragment.MainMeFragment
import com.hzsoft.lib.base.view.BaseActivity
import kotlinx.android.synthetic.main.activity_main_index.*

/**
 * Describe:
 * 导航页
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
class MainActivity : BaseActivity() {

    private var mHomeFragment: Fragment? = null
    private var mMeFragment: Fragment? = null
    private var mCurrFragment: Fragment? = null

    override fun onBindLayout(): Int = R.layout.activity_main_index

    override fun initView() {

    }

    override fun initData() {
        mHomeFragment = MainHomeFragment.newsInstance()
        mMeFragment = MainMeFragment.newsInstance()

        mCurrFragment = mHomeFragment
        if (mHomeFragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_content, mHomeFragment as Fragment, MainChannel.HOME.name)
                .commit()
        }
    }

    override fun initListener() {
        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    switchContent(mCurrFragment, mHomeFragment, MainChannel.HOME.name)
                    mCurrFragment = mHomeFragment
                }
                R.id.navigation_me -> {
                    switchContent(mCurrFragment, mMeFragment, MainChannel.ME.name)
                    mCurrFragment = mMeFragment
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun switchContent(from: Fragment?, to: Fragment?, tag: String) {
        if (from == null || to == null) {
            return
        }
        val beginTransaction = supportFragmentManager.beginTransaction()
        if (!to.isAdded) {
            beginTransaction.hide(from).add(R.id.frame_content, to, tag).commit()
        } else {
            beginTransaction.hide(from).show(to).commit()
        }
    }

    override fun enableToolbar(): Boolean {
        return false
    }
}
