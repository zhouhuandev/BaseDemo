# BaseDemo

## 介绍

BaseDemo 是Android MVVM + Retrofit + OkHttp + Coroutine 协程 + 组件化架构的Android应用开发规范化架构，通过不断的升级迭代，目前主要分为两个版本，分别为分支 MVVM+Databinding 组件化版本，分支MVVM+Databinding+Single 单体版本。旨在帮助您快速构建属于自己的APP项目架构，做到快速响应上手，另外再长期的实践经验中汇总了大量的使用工具类，主要放在了项目 `lib_common` 组件中，以供大家参考使用。具体使用请开发者工具自己项目需求决定选择如何使用。

如果我的付出可以换来对您的帮助的话，还请您点个start，将会是我不懈更新的动力，万分感谢。如果在使用中有任何问题，请留言

- 电子邮件：zhouhuan88888@163.com

## 功能演示
软件架构说明
- 主页 上拉刷新，下拉加载，Room操作

![Home](https://github.com/zhouhuandev/BaseDemo/blob/mvvm%2Bdatabinding/image/a29695860836d6dc26f8438785b69af.jpg)

- 中心 添加照片，视频

![Me](https://github.com/zhouhuandev/BaseDemo/blob/mvvm%2Bdatabinding/image/5ee846d27257d5f251e23878355c920.jpg)

## 主要功能

### 核心基础库 lib_base

#### MVVM七大的核心公用基类

##### 基础页面接口
- 视图层核接口 BaseView

```kotlin
interface BaseView : ILoadView, INoDataView, ITransView, INetErrView {
    fun initListener()
    fun initData()
    fun finishActivity()
}
```
- 加载初始化弹窗接口 ILoadView
```kotlin
interface ILoadView {
    //显示初始加载的View，初始进来加载数据需要显示的View
    fun showInitLoadView()

    //隐藏初始加载的View
    fun hideInitLoadView()
}
```
- 显示是否有数据页面接口 INoDataView
```kotlin
interface INoDataView {
    //显示无数据View
    fun showNoDataView()

    //隐藏无数据View
    fun hideNoDataView()

    //显示指定资源的无数据View
    fun showNoDataView(@DrawableRes resid: Int)
}
```
- 显示小菊花View接口 ITransView
```kotlin
interface ITransView {
    //显示背景透明小菊花View,例如删除操作
    fun showTransLoadingView()

    //隐藏背景透明小菊花View
    fun hideTransLoadingView()
}
```
- 显示是否网络错误View接口 INetErrView
```kotlin
interface INetErrView {
    //显示网络错误的View
    fun showNetWorkErrView()

    //隐藏网络错误的View
    fun hideNetWorkErrView()
}
```
- 基础刷新接口 BaseRefreshView
```kotlin
interface BaseRefreshView {

    /**
     * 是否启用下拉刷新
     * @param b
     */
    fun enableRefresh(b: Boolean)

    /**
     * 是否启用上拉加载更多
     */
    fun enableLoadMore(b: Boolean)

    /**
     * 刷新回调
     * 向 ViewModel 发送刷新请求
     */
    fun onRefreshEvent()

    /**
     * 加载更多的回调
     * 向 ViewModel 发送加载更多请求
     */
    fun onLoadMoreEvent()

    /**
     * 自动加载的事件
     * 向 ViewModel 发送自动加载的请求
     */
    fun onAutoLoadEvent()

    /**
     * 停止刷新
     */
    fun stopRefresh()

    /**
     * 停止加载更多
     */
    fun stopLoadMore()

    /**
     * 自动加载数据
     */
    fun autoLoadData()
}
```

##### 基础活动

```kotlin
abstract class BaseActivity : RxAppCompatActivity(), BaseView {
	abstract fun onBindLayout(): Int
	abstract fun initView()
    abstract override fun initData()
    override fun initListener()
}
```

##### BaseMvvmActivity

```kotlin
abstract class BaseMvvmActivity<VM : BaseViewModel> : BaseActivity() {
	/**
     * 绑定 ViewModel
     */
    abstract fun onBindViewModel(): Class<VM>
	
    /**
     * 放置 观察者对象
     */
    abstract fun initViewObservable()
}
```

##### BaseMvvmDataBindingActivity

```kotlin
abstract class BaseMvvmDataBindingActivity<V : ViewDataBinding, VM : BaseViewModel> : BaseMvvmActivity<VM>() {
	abstract fun onBindVariableId(): Int
}
```

##### BaseMvvmRefreshActivity

```kotlin
abstract class BaseMvvmRefreshActivity<T, VM : BaseRefreshViewModel<T>> : BaseMvvmActivity<VM>(), BaseRefreshView {
	protected abstract fun onBindRreshLayout(): Int

    protected abstract fun enableRefresh(): Boolean

    protected abstract fun enableLoadMore(): Boolean
}
```
##### BaseMvvmRefreshDataBindingActivity
```kotlin
abstract class BaseMvvmRefreshDataBindingActivity<T, V : ViewDataBinding, VM : BaseRefreshViewModel<T>> : BaseMvvmDataBindingActivity<V, VM>(), BaseRefreshView {
	protected abstract fun onBindRreshLayout(): Int

    protected abstract fun enableRefresh(): Boolean

    protected abstract fun enableLoadMore(): Boolean
}
```