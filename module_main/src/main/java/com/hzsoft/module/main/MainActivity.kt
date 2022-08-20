package com.hzsoft.module.main

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.hzsoft.lib.base.module.constons.ARouteConstants
import com.hzsoft.lib.base.module.provider.IHomeProvider
import com.hzsoft.lib.base.module.provider.IMeProvider
import com.hzsoft.lib.base.view.BaseActivity
import com.hzsoft.lib.common.utils.ext.getCompatColor
import com.hzsoft.lib.common.widget.TabBarBean
import com.hzsoft.lib.common.widget.TabBarView
import com.hzsoft.module.main.entity.MainChannel

/**
 * Describe:
 * 导航页
 *
 * @author zhouhuan
 * @Date 2020/12/3
 */
class MainActivity : BaseActivity() {

    @Autowired(name = ARouteConstants.Main.HOME_MAIN)
    @JvmField
    var mHomeProvider: IHomeProvider? = null

    @Autowired(name = ARouteConstants.Me.ME_MAIN)
    @JvmField
    var mMeProvider: IMeProvider? = null

    private var mHomeFragment: Fragment? = null
    private var mMeFragment: Fragment? = null
    private var mCurrFragment: Fragment? = null

    override fun onBindLayout(): Int = R.layout.activity_main_index

    override fun initView() {
        mHomeFragment = mHomeProvider?.mainHomeFragment
        mMeFragment = mMeProvider?.mainMeFragment
    }

    override fun initData() {

    }

    override fun initListener() {
        val tabBarView = findViewById<TabBarView>(R.id.tab_bar_view)
        tabBarView.setTabs(
            TabBarBean(
                color = mContext.getCompatColor(R.color.color_92969e),
                selectedColor = mContext.getCompatColor(R.color.color_191f2b),
                backgroundColor = mContext.getCompatColor(R.color.white),
                borderStyle = TabBarView.TAB_BAR_BORDER_STYLE_WHITE,
                items = arrayListOf(
                    TabBarBean.ItemBean(
                        id = ARouteConstants.Main.HOME_MAIN,
                        text = getString(R.string.title_home),
                        iconId = R.mipmap.tabbar_icon_home_normal,
                        selectedIconId = R.mipmap.tabbar_icon_home_selected,
                        iconPath = "",
                        selectedIconPath = "",
                        isCdn = false
                    ),
                    TabBarBean.ItemBean(
                        id = ARouteConstants.Me.ME_MAIN,
                        text = getString(R.string.title_mine),
                        iconId = R.mipmap.tabbar_icon_me_normal,
                        selectedIconId = R.mipmap.tabbar_icon_me_selected,
                        iconPath = "",
                        selectedIconPath = "",
                        isCdn = false
                    ),
                ),
            )
        ) {
            when (it.id) {
                ARouteConstants.Main.HOME_MAIN -> {
                    switchContent(mCurrFragment, mHomeFragment, MainChannel.HOME.description)
                    mCurrFragment = mHomeFragment
                }
                ARouteConstants.Me.ME_MAIN -> {
                    switchContent(mCurrFragment, mMeFragment, MainChannel.ME.description)
                    mCurrFragment = mMeFragment
                }
            }
        }
    }

    private fun switchContent(from: Fragment?, to: Fragment?, tag: String) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        if (to?.isAdded != true) {
            beginTransaction.apply {
                from?.let {
                    hide(from)
                }
                to?.let {
                    add(R.id.frame_content, to, tag).commit()
                }
            }
        } else {
            beginTransaction.apply {
                from?.let {
                    hide(from)
                }
                show(to).commit()
            }
        }
    }

    override fun enableToolbar(): Boolean {
        return false
    }
}
