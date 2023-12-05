package com.hzsoft.module.me.activity

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.hzsoft.lib.base.module.constons.ARouteConstants
import com.hzsoft.lib.base.utils.ThreadUtils
import com.hzsoft.lib.base.view.BaseFragment
import com.hzsoft.lib.base.view.BaseMvvmRefreshViewBindingActivity
import com.hzsoft.lib.common.utils.VibrateTool
import com.hzsoft.lib.common.widget.CommonDialogFragment
import com.hzsoft.lib.log.KLog
import com.hzsoft.lib.net.dto.Resource
import com.hzsoft.lib.net.local.entity.UserTestRoom
import com.hzsoft.lib.net.utils.ext.observe
import com.hzsoft.module.me.R
import com.hzsoft.module.me.adapter.RoomTestAdapter
import com.hzsoft.module.me.databinding.ActivityRoomTestBinding
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * 测试 Room，使用了DataBinding示例
 *
 * @author zhouhuan
 * @time 2021/11/23
 */
@Route(path = ARouteConstants.Me.ME_ROOT_TEST, name = "Room测试")
class RoomTestActivity : BaseMvvmRefreshViewBindingActivity<ActivityRoomTestBinding, RoomTestViewModel>() {

    companion object {
        fun start(context: Context) {
            ARouter.getInstance().build(ARouteConstants.Me.ME_ROOT_TEST)
                .navigation(context)
        }
    }

    private val image = listOf(
        "https://img1.baidu.com/it/u=3179708835,3698124440&fm=253&fmt=auto&app=138&f=JPEG?w=300&h=300",
        "https://img0.baidu.com/it/u=2471949583,3673126699&fm=253&fmt=auto&app=120&f=JPEG?w=640&h=640",
        "https://img2.baidu.com/it/u=2816231141,758525652&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400",
        "https://img0.baidu.com/it/u=2091479077,2764156209&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400",
        "https://img0.baidu.com/it/u=1352764236,394136942&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400",
        "https://img2.baidu.com/it/u=3667472213,444327283&fm=253&fmt=auto&app=138&f=JPEG?w=502&h=500",
        "https://img2.baidu.com/it/u=2260667029,973961386&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",
        "https://img1.baidu.com/it/u=3963690806,2679586746&fm=253&fmt=auto&app=138&f=JPEG?w=440&h=440",
        "https://img0.baidu.com/it/u=1125089834,1274132805&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=800",
        "https://img2.baidu.com/it/u=4005667788,1674871828&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",

        "https://img1.baidu.com/it/u=419279256,2658411497&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",
        "https://img1.baidu.com/it/u=939040886,184290657&fm=253&fmt=auto&app=138&f=JPEG?w=360&h=360",
        "https://img1.baidu.com/it/u=3486047981,4277840304&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400",
        "https://img0.baidu.com/it/u=3467758223,1877547427&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",
        "https://img0.baidu.com/it/u=3047251996,1492990240&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",
        "https://img0.baidu.com/it/u=4138593849,3244966158&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",
        "https://img0.baidu.com/it/u=676478628,2285505817&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",
        "https://img1.baidu.com/it/u=1780714482,544116432&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=800",
        "https://img2.baidu.com/it/u=3751284523,2644256892&fm=253&fmt=auto&app=138&f=JPEG?w=300&h=300",
        "https://img2.baidu.com/it/u=1613844048,2554101245&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",

        "https://img1.baidu.com/it/u=3865475840,2267602486&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",
        "https://img1.baidu.com/it/u=2813198016,1637799007&fm=253&fmt=auto&app=138&f=JPEG?w=360&h=360",
        "https://img1.baidu.com/it/u=2157898634,3153346870&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"
    )

    private val firstName = listOf(
        "赵",
        "钱",
        "孙",
        "李",
        "周",
        "吴",
        "郑",
        "王",
        "冯",
        "陈",
        "褚",
        "卫",
        "蒋",
        "沈",
        "韩",
        "杨",
        "朱",
        "秦",
        "尤",
        "许",
        "何",
        "吕",
        "施",
        "张",
        "孔",
        "曹",
        "严",
        "华",
        "金",
        "魏",
        "陶",
        "姜"
    )
    private val lastName = listOf("先生", "小姐", "夫人", "大夫", "同志", "同学", "老师", "老板", "经理")

    private lateinit var mAdapter: RoomTestAdapter

    override fun getTootBarTitle(): String = "Room"

    override fun enableToolBarLeft(): Boolean = true

    override fun onBindLayout(): Int = R.layout.activity_room_test

    override fun initView() {
        mAdapter = RoomTestAdapter()
        mAdapter.bindSkeletonScreen(
            requireBinding().mRecyclerView,
            com.hzsoft.lib.base.R.layout.skeleton_default_service_item,
            8
        )
        mAdapter.setOnItemLongClickListener { _, _, position ->
            VibrateTool.vibrateOnce(100)
            CommonDialogFragment.Builder()
                .setTitle("温馨提示")
                .setDescribe(
                    "您确认要删除%s%s吗？".format(
                        mAdapter.getItem(position).firstName,
                        mAdapter.getItem(position).lastName
                    )
                )
                .setLeftBtn("取消")
                .setRightBtn("确认")
                .setOnDialogClickListener(object : CommonDialogFragment.OnDialogClickListener {
                    override fun onLeftBtnClick(view: View) {

                    }

                    override fun onRightBtnClick(view: View) {
                        mViewModel.deleteUserTestRoom(mAdapter.getItem(position))
                    }
                })
                .build()
                .show(supportFragmentManager, "deleteDialog")

            false
        }
    }

    override fun initListener() {
        super.initListener()
        requireBinding().addUser.setOnClickListener(this::onClick)
        requireBinding().selectUser.setOnClickListener(this::onClick)
    }

    override fun initData() {
        onRefreshEvent()
    }

    override fun initViewObservable() {
        observe(mViewModel.userTestRoomLiveData, ::handleUserTestRoomList)
    }

    override fun onClick(v: View?) {
        if (beFastClick()) {
            return
        }
        when (v?.id) {
            R.id.addUser -> {
                val userTestRoom = UserTestRoom(
                    image = image.random(),
                    firstName = firstName.random(),
                    lastName = lastName.random(),
                    age = Random.nextInt(IntRange(18, 50))
                )
                mViewModel.insertUserTestRoom(userTestRoom)
                mAdapter.addData(0, userTestRoom)
                // 滚动回第一个位置
                requireBinding().mRecyclerView.scrollToPosition(0)
            }

            R.id.selectUser -> {
                onRefreshEvent()
            }
        }
    }

    private var firstLoad = true

    override fun onRefreshEvent() {
        // 为了展示骨架屏
        if (firstLoad) {
            firstLoad = false
            ThreadUtils.runOnUiThread({ mViewModel.refreshData() }, 1000)
        } else {
            mViewModel.refreshData()
        }
    }

    override fun onLoadMoreEvent() {
        mViewModel.loadMore()
    }

    override fun onBindRefreshLayout(): Int = R.id.mRefreshLayout

    override fun enableRefresh(): Boolean = true

    override fun enableLoadMore(): Boolean = false

    private fun handleUserTestRoomList(status: Resource<List<UserTestRoom>>) {
        when (status) {
            is Resource.Success -> status.data?.let { bindListData2(ArrayList(it)) }
            is Resource.DataError -> {
                status.errorCode.let { KLog.e(BaseFragment.TAG, "--------->$it") }
            }

            else -> {}
        }
    }

    private fun bindListData2(userTestRoom: ArrayList<UserTestRoom>) {
        mAdapter.setNewInstance(userTestRoom)
        // 执行列表动画
        requireBinding().mRecyclerView.apply {
            layoutAnimation =
                AnimationUtils.loadLayoutAnimation(mContext, R.anim.layout_fall_down)
            scheduleLayoutAnimation()
        }
    }
}