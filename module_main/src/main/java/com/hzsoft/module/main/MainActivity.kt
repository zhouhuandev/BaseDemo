package com.hzsoft.module.main

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.hzsoft.lib.base.view.BaseActivity
import com.hzsoft.lib.base.module.provider.IHomeProvider
import com.hzsoft.lib.base.module.provider.IMeProvider
import com.hzsoft.module.main.entity.MainChannel
import kotlinx.android.synthetic.main.activity_main_index.*

/**
 * Describe:
 * 导航页
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
class MainActivity : BaseActivity() {

    @Autowired(name = "/home/main")
    @JvmField
    var mHomeProvider: IHomeProvider? = null

    @Autowired(name = "/me/main")
    @JvmField
    var mMeProvider: IMeProvider? = null

    private var mHomeFragment: Fragment? = null
    private var mMeFragment: Fragment? = null
    private var mCurrFragment: Fragment? = null

    override fun onBindLayout(): Int = R.layout.activity_main_index

    override fun initView() {

    }

    override fun initData() {
        mHomeFragment = mHomeProvider?.mainHomeFragment
        mMeFragment = mMeProvider?.mainMeFragment

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
