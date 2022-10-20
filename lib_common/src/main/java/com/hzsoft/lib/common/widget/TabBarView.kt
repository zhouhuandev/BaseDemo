package com.hzsoft.lib.common.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringDef
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.tabs.TabLayout
import com.hzsoft.lib.base.utils.ToastUtil
import com.hzsoft.lib.common.R
import com.hzsoft.lib.common.utils.InfoVerify
import com.hzsoft.lib.common.utils.VibrateTool
import com.hzsoft.lib.common.utils.ext.getCompatColor
import com.hzsoft.lib.common.utils.ext.loadImgFile
import com.hzsoft.lib.common.utils.ext.view.toVisibleOrGone
import com.hzsoft.lib.log.KLog
import kotlinx.coroutines.*
import java.io.File

/**
 * TabBarView
 *
 * @author [zhouhuan](mailto:zhouhuandev@gmail.com)
 * @since 2022/8/20 14:43
 */
class TabBarView(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    companion object {
        private const val TAG = "TabBarView"
        const val TAB_BAR_BORDER_STYLE_WHITE = "white"
        const val TAB_BAR_BORDER_STYLE_DEFAULT = "black"
    }

    @StringDef(TAB_BAR_BORDER_STYLE_WHITE, TAB_BAR_BORDER_STYLE_DEFAULT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class BorderStyle

    private val tabLayout: TabLayout by lazy { findViewById(R.id.tab_navigation) }
    private val viewLineTop: View by lazy { findViewById(R.id.view_line_top) }
    private lateinit var tabBarConfig: TabBarBean

    private var onSelected: ((TabBarBean.ItemBean) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_tab_bar, this)
        gravity = Gravity.BOTTOM
        // 隐藏底部点击水波纹
        tabLayout.setTabRippleColorResource(R.color.transparent)
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        tabLayout.tabMode = TabLayout.MODE_FIXED
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            var firstSelected = true
            override fun onTabReselected(tab: TabLayout.Tab?) {
                KLog.d(
                    TAG,
                    "[TabBarView]: onTabReselected:${tab?.position}, from:${tab.toString()}"
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                KLog.d(
                    TAG,
                    "[TabBarView]: onTabUnselected:${tab?.position}, from::${tab.toString()}"
                )
                tab?.let {
                    val tabBarItemView = tab.customView as TabBarItemView
                    val listBean = tab.tag as? TabBarBean.ItemBean ?: return
                    tabBarItemView.setTabItem(tabBarConfig, listBean, "")
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                KLog.d(TAG, "[TabBarView]: onTabSelected:${tab?.position}, from::${tab.toString()}")
                tab?.let {
                    val tabBarItemView = tab.customView as TabBarItemView
                    val listBean = tab.tag as? TabBarBean.ItemBean ?: return
                    tabBarItemView.setTabItem(tabBarConfig, listBean, listBean.id)
                    if (!firstSelected) {
                        VibrateTool.vibrateOnce(50)
                    }
                    firstSelected = false
                    onSelected?.invoke(listBean)
                }
            }
        })
    }

    fun switchCurrentTab(id: String?) {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if ((tab?.tag as? TabBarBean.ItemBean)?.id == id) {
                switchCurrentTab(i)
                break
            }
        }
    }

    private fun switchCurrentTab(position: Int) {
        tabLayout.let {
            if (it.tabCount > position) {
                it.getTabAt(position)?.select()
            }
        }
    }

    fun setUnreadVisible(visible: Boolean, id: String?) {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if ((tab?.tag as? TabBarBean.ItemBean)?.id == id) {
                tab.let {
                    val tabBarItemView = tab?.customView as? TabBarItemView
                    tabBarItemView?.setUnreadVisible(visible)
                }
                break
            }
        }
    }

    fun setSelectListener(onSelected: ((TabBarBean.ItemBean) -> Unit)?) {
        this.onSelected = onSelected
    }

    fun setTabs(tabBarConfig: TabBarBean, onSelected: ((TabBarBean.ItemBean) -> Unit)? = null) {
        this.tabBarConfig = tabBarConfig
        this.onSelected = onSelected
        tabLayout.removeAllTabs()
        tabBarConfig.run {
            items.also { tabs ->
                if (tabs.size !in 2..5) {
                    ToastUtil.showToastCenter("Tab bar view numbers is between 2 and 5")
                    KLog.e(TAG, "[TabBarView]: Tab bar view is between 2 and 5")
                    return
                }
                tabs.forEach { tab ->
                    TabBarItemView(context, null).let { tabItem ->
                        tabItem.setTabItem(tabBarConfig, tab, tabs[0].id)
                        tabLayout.addTab(
                            tabLayout.newTab().setCustomView(tabItem).apply { tag = tab }
                        )
                    }
                }
            }
            tabLayout.setBackgroundColor(backgroundColor)
            viewLineTop.setBackgroundColor(
                context.getCompatColor(if (borderStyle == TAB_BAR_BORDER_STYLE_WHITE) R.color.default_tab_bar_title_white_color else R.color.default_tab_bar_title_black_color)
            )
        }
    }
}

class TabBarItemView(
    context: Context,
    attrs: AttributeSet?
) :
    RelativeLayout(context, attrs) {

    companion object {
        private const val TAG = "TabBarView"
    }

    private val ivTabIcon: AppCompatImageView by lazy { findViewById(R.id.iv_tab_icon) }
    private val tvTabName: AppCompatTextView by lazy { findViewById(R.id.tv_tab_name) }
    private val ivRed: AppCompatImageView by lazy { findViewById(R.id.iv_red) }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_tab_item, this)
        gravity = Gravity.CENTER
    }

    fun setUnreadVisible(visible: Boolean) {
        ivRed.toVisibleOrGone(visible)
    }

    fun setTabItem(
        tabBarConfig: TabBarBean,
        tab: TabBarBean.ItemBean,
        selectId: String
    ) {
        tab.let {
            if (it.isCdn) {
                requestDrawable(
                    if (selectId == it.id) it.selectedIconPath else it.iconPath
                ) { drawable ->
                    ivTabIcon.setImageDrawable(drawable)
                }
            } else {
                ivTabIcon.setImageResource(if (selectId == it.id) it.selectedIconId else it.iconId)
            }
            tvTabName.text = it.text
            tvTabName.setTextColor(if (selectId == it.id) tabBarConfig.selectedColor else tabBarConfig.color)
        }
    }

    private fun requestDrawable(
        url: String?,
        block: ((Drawable?) -> Unit)? = null
    ) {
        if (!url.isNullOrEmpty() && InfoVerify.isUrl(url)) {
            CoroutineScope(Dispatchers.IO).launch {
                val file: File? = context.loadImgFile(url)
                launch(Dispatchers.Main) {
                    block?.invoke(Drawable.createFromPath(file?.absolutePath))
                }
            }
            return
        }
        block?.invoke(null)
    }
}

data class TabBarBean(
    @ColorInt
    val color: Int,
    @ColorInt
    val selectedColor: Int,
    @ColorInt
    val backgroundColor: Int = 0,
    @TabBarView.BorderStyle
    val borderStyle: String = TabBarView.TAB_BAR_BORDER_STYLE_WHITE,
    val items: ArrayList<ItemBean>,
) {
    data class ItemBean(
        val id: String,
        val isCdn: Boolean = false,
        val text: String,
        val iconPath: String? = null,
        val selectedIconPath: String? = null,
        @DrawableRes
        val iconId: Int = 0,
        @DrawableRes
        val selectedIconId: Int = 0
    )
}