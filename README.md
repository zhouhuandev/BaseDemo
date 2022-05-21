# BaseDemo

## 介绍

🔥🔥🔥 BaseDemo 是Android MVVM + Retrofit + OkHttp + Coroutine 协程 + ViewBinding & DataBinding + Room + 组件化架构的Android应用开发规范化架构，通过不断的升级迭代，目前主要分为两个版本，分别为分支 MVVM+Databinding 组件化版本，分支MVVM+Databinding+Single 单体版本。旨在帮助您快速构建属于自己的APP项目架构，做到快速响应上手，另外再长期的实践经验中汇总了大量的使用工具类，主要放在了项目 `lib_common`组件中，以供大家参考使用。具体使用请开发者工具自己项目需求决定选择如何使用。

如果我的付出可以换来对您的帮助的话，还请您点个start，将会是我不懈更新的动力，万分感谢。如果在使用中有任何问题，请留言

- 电子邮件：zhouhuandev@gmail.com

## 功能演示

软件架构说明

<table>
    <tr>
        <td align="center">
            <h3>上拉加载，下拉刷新</h3>
        </td>
        <td align="center">
            <h3>演示入口</h3>
        </td>
    </tr>
    <tr>
        <td align="center">
            <img src="https://gitee.com/shandong_zhaotai_network_sd_zhaotai/ImageRepo/raw/master/2021/images/161637674837_.pic.jpg" alt="Home" style="zoom:30%;" />
        </td>
        <td align="center">
            <img src="https://gitee.com/shandong_zhaotai_network_sd_zhaotai/ImageRepo/raw/master/2021/images/171637674836_.pic.jpg" alt="Home" style="zoom:30%;" />
        </td>
    </tr>
</table>

<table>
    <tr>
        <td align="center">
            <h3>SaveStateHandler操作示例</h3>
        </td>
        <td align="center">
            <h3>Room操作示例</h3>
        </td>
    </tr>
    <tr>
        <td align="center">
            <img src="https://gitee.com/shandong_zhaotai_network_sd_zhaotai/ImageRepo/raw/master/2021/images/151637674836_.pic.jpg" alt="Home" style="zoom:30%;" />
        </td>
        <td align="center">
            <img src="https://gitee.com/shandong_zhaotai_network_sd_zhaotai/ImageRepo/raw/master/2021/images/141637674835_.pic.jpg" alt="Home" style="zoom:30%;" />
        </td>
    </tr>
</table>

- 扫码体验APK

<img src="https://www.pgyer.com/app/qrcode/tBPE" alt="下载二维码" style="zoom:100%;" />

- [演示视频传送门](https://gitee.com/shandong_zhaotai_network_sd_zhaotai/ImageRepo/raw/master/2021/images/1637675109209180.mp4)


## 主要功能

### 核心基础库 lib_base

#### MVVM十大的核心公用基类和六大基础页面接口

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

##### BaseActivity

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
abstract class BaseMvvmDataBindingActivity<V : ViewDataBinding, VM : BaseViewModel> : BaseMvvmActivity<VM>(), ActivityBindingHolder<V> by ActivityBinding() {
    abstract fun onBindVariableId(): MutableList<Pair<Int, Any>>
}
```

##### BaseMvvmRefreshActivity

```kotlin
abstract class BaseMvvmRefreshActivity<T, VM : BaseRefreshViewModel<T>> : BaseMvvmActivity<VM>(), BaseRefreshView {
    protected abstract fun initRefreshView(): Int
    protected abstract fun enableRefresh(): Boolean
    protected abstract fun enableLoadMore(): Boolean
}
```

##### BaseMvvmRefreshDataBindingActivity

```kotlin
abstract class BaseMvvmRefreshDataBindingActivity<T, V : ViewDataBinding, VM : BaseRefreshViewModel<T>> : BaseMvvmDataBindingActivity<V, VM>(), BaseRefreshView {
    protected abstract fun onBindRefreshLayout(): Int
    protected abstract fun enableRefresh(): Boolean
    protected abstract fun enableLoadMore(): Boolean
}
```

##### BaseFragment

```kotlin
    abstract fun onBindLayout(): Int
    abstract fun initView(mView: View)
    abstract override fun initData()
```

##### BaseMvvmFragment

```kotlin
abstract class BaseMvvmFragment<VM : BaseViewModel> : BaseFragment() {
    abstract fun onBindViewModel(): Class<VM>
    abstract fun initViewObservable()
}
```

##### BaseMvvmDataBindingFragment

```kotlin
abstract class BaseMvvmDataBindingFragment<V : ViewDataBinding, VM : BaseViewModel> : BaseMvvmFragment<VM>(), FragmentBindingHolder<V> by FragmentBinding() {
    abstract fun onBindVariableId(): MutableList<Pair<Int, Any>>
}
```

##### BaseMvvmRefreshFragment

```koltin
abstract class BaseMvvmRefreshFragment<T, VM : BaseRefreshViewModel<T>> : BaseMvvmFragment<VM>(), BaseRefreshView {
    protected abstract fun onBindRreshLayout(): Int
    protected abstract fun enableRefresh(): Boolean
    protected abstract fun enableLoadMore(): Boolean
}
```

##### BaseMvvmRefreshDataBindingFragment

```kotlin
abstract class BaseMvvmRefreshDataBindingFragment<T, V : ViewDataBinding, VM : BaseRefreshViewModel<T>> :
    BaseMvvmDataBindingFragment<V, VM>(),
    BaseRefreshView {
    protected abstract fun onBindRreshLayout(): Int
    protected abstract fun enableRefresh(): Boolean
    protected abstract fun enableLoadMore(): Boolean
}
```

- BaseFragmentPagerAdapter
- BaseSkeletonAdapter
- IBaseViewModel
- BaseViewModel
- BaseRefreshViewModel ...

### 功能特色

- 支持是否使用 `ToolBar`

```kotlin
    open fun enableToolbar(): Boolean {
        return true
    }
```

- 支持自定义 `ToolBar`

```kotlin
    open fun onBindToolbarLayout(): Int {
        return R.layout.common_toolbar
    }
```

- 支持标题文字图标信息自定义

```kotlin
    open fun getTootBarTitle(): String {
        return ""
    }

    /**
     * 设置返回按钮的图样，可以是Drawable ,也可以是ResId
     * 注：仅在 enableToolBarLeft 返回为 true 时候有效
     *
     * @return
     */
    open fun getToolBarLeftIcon(): Int {
        return R.drawable.ic_white_black_45dp
    }

    /**
     * 是否打开返回
     *
     * @return
     */
    open fun enableToolBarLeft(): Boolean {
        return false
    }

    /**
     * 设置标题右边显示文字
     *
     * @return
     */
    open fun getToolBarRightTxt(): String {
        return ""
    }

    /**
     * 设置标题右边显示 Icon
     *
     * @return int resId 类型
     */
    open fun getToolBarRightImg(): Int {
        return 0
    }

    /**
     * 右边文字监听回调
     * @return
     */
    open fun getToolBarRightTxtClick(): View.OnClickListener? {
        return null
    }

    /**
     * 右边图标监听回调
     * @return
     */
    open fun getToolBarRightImgClick(): View.OnClickListener? {
        return null
    }
```

- 支持`loading`加载数据

```kotlin
    override fun showInitLoadView() {
        showInitLoadView(true)
    }

    override fun hideInitLoadView() {
        showInitLoadView(false)
    }
```

- 支持透明`loading`的加载数据

```kotlin
    override fun showTransLoadingView() {
        showTransLoadingView(true)
    }

    override fun hideTransLoadingView() {
        showTransLoadingView(false)
    }
```

- 支持是否使用全屏显示

```kotlin
    open fun enableAllowFullScreen(): Boolean {
        return false
    }
```

- 支持显示无数据

```kotlin
    override fun showNoDataView() {
        showNoDataView(true)
    }

    override fun showNoDataView(resid: Int) {
        showNoDataView(true, resid)
    }

    override fun hideNoDataView() {
        showNoDataView(false)
    }
```

- 支持网络网络错误显示

```kotlin
    override fun hideNetWorkErrView() {
        showNetWorkErrView(false)
    }

    override fun showNetWorkErrView() {
        showNetWorkErrView(true)
    }
```

- 支持`Fragment`的懒加载

```kotlin
    /**
     * 懒加载机制 当页面可见的时候加载数据
     * 如果当前 FragmentTransaction.setMaxLifecycle 处理 Lifecycle.State.RESUMED 则 懒加载失效
     * 如果 FragmentTransaction.setMaxLifecycle 传入BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT ，
     * 则只有当前的Fragment处于Lifecycle.State.RESUMED状态。 所有其他片段的上限为Lifecycle.State.STARTED 。
     * 如果传递了BEHAVIOR_SET_USER_VISIBLE_HINT ，则所有片段都处于Lifecycle.State.RESUMED状态，
     * 并且将存在Fragment.setUserVisibleHint(boolean)回调
     */
    private fun lazyLoad() {
        //这里进行双重标记判断,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isViewVisable) {
            Log.d(TAG, "lazyLoad: Successful")
            initData()
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false
            isViewVisable = false
        } else {
            Log.d(TAG, "lazyLoad: Fail")
        }
    }

    //默认不启用懒加载
    open fun enableLazyData(): Boolean {
        return false
    }
```

- 支持`DataBinding`

  **for Activity**

  ```kotlin
  import com.hzsoft.lib.base.view.databinding.ActivityBinding
  import com.hzsoft.lib.base.view.databinding.ActivityViewBinding
  
  // 原始页面使用DataBinding
  class HomeActivity : AppCompatActivity(), ActivityBindingHolder<ActivityHomeBinding> by ActivityBinding(R.layout.activity_home) {
      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          // replace setContentView(), and hold binding instance
          inflate(/* option: */ onClear = { it.onClear() }) {
              // init binding, views and states here
          }
      }
  
      // Optional: perform clear binding
      private fun ActivityHomeBinding.onClear() {
          // clear something.
      }
  }
  // ViewStub页面使用DataBinding
  // 请参考 #BaseMvvmDataBindingActivity
  ```

  **for Fragment**

  ```kotlin
  import com.hzsoft.lib.base.view.databinding.FragmentBinding
  import com.hzsoft.lib.base.view.databinding.FragmentViewBinding
  
  // 原始页面使用DataBinding
  class HomeFragment : Fragment(), FragmentBindingHolder<FragmentHomeBinding> by FragmentBinding() {
      
      override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
      ): View {
          return inflate(
              inflater = inflater,
              root = container,
              attachToRoot = false,
              /* option: */ onClear = { it.onClear() }
          ) {
              // init binding, views and states here
          }
      }
  
      // Optional: perform clear binding
      private fun FragmentHomeBinding.onClear() {
          // clear something.
      }
  }
  // ViewStub页面使用DataBinding
  // 请参考 #BaseMvvmDataBindingFragment
  ```

- 支持`ViewBinding`

  **for Activity**

  ```kotlin
  import com.hzsoft.lib.base.view.viewbinding.ActivityBinding
  import com.hzsoft.lib.base.view.viewbinding.ActivityViewBinding
  
  // 原始页面使用ViewBinding
  class HomeActivity : AppCompatActivity(), ActivityViewBinding<ActivityHomeBinding> by ActivityBinding() {
      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          // replace setContentView(), and hold binding instance
          inflate(
              inflate = { ActivityHomeBinding.inflate(layoutInflater) },
              /* 原始页面此处默认为 true */ isRoot = true,
              /* option: */ onClear = { it.onClear() }
          ) {
              // init with binding
          }
      }
  
      // Optional: perform clear binding
      private fun ActivityHomeBinding.onClear() {
          // clear something.
      }
  }
  // ViewStub页面使用ViewBinding
  // 请参考 #SaveStateTestActivity
  ```

  **for Fragment**

  ```kotlin
  import com.hzsoft.lib.base.view.viewbinding.FragmentBinding
  import com.hzsoft.lib.base.view.viewbinding.FragmentViewBinding
  
  // 原始页面使用ViewBinding
  class HomeFragment : Fragment(), FragmentViewBinding<FragmentHomeBinding> by FragmentBinding() {
  
      override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
      ): View {
          return inflate(
              inflate = { FragmentHomeBinding.inflate(inflater, container, false) },
              /* option: */ onClear = { it.onClear() }
          ) {
              // init binding, views and states here
          }
      }
  
      // Optional: perform clear binding
      private fun FragmentHomeBinding.onClear() {
          // clear something.
      }
  }
  // ViewStub页面使用ViewBinding
  // 请参考 #MainMeFragment
  ```

- 封装`UIChangeLiveData`、`UIChangeRefreshLiveData`

- `ViewModel` lazy加载 ...

### 上拉下拉功能组件 lib_refresh_layout（已废弃，改用SmartRefreshLayout代替）

- 支持最基本的下拉刷新、上拉加载更多
- 支持自定义HeadView和FootView
- 支持自动刷新
- 支持启用、禁用下拉刷新
- 支持启用、进攻上拉加载更多
- 通用小菊花样式DaisyRefreshLayout
- 通用小箭头样式ArrowRefreshLayout

### 项目架构

![项目架构](https://gitee.com/shandong_zhaotai_network_sd_zhaotai/ImageRepo/raw/master/2021/images/20210312211114.png)

- 集成模式：所有的业务组件被“app壳工程”依赖，组成一个完整的APP；
- 组件模式：可以独立开发业务组件，每一个业务组件就是一个APP；
- app壳工程：负责管理各个业务组件，和打包apk，没有具体的业务功能；
- 业务组件：根据公司具体业务而独立形成一个的工程；
- 功能组件：提供开发APP的某些基础功能，例如打印日志、下拉刷新控件等；
- Main组件：属于业务组件，指定APP启动页面、主界面；
- Common组件：属于功能组件，支撑业务组件的基础，提供多数业务组件需要的功能

### MVVM架构

#### BaseMVVM架构

![](https://gitee.com/shandong_zhaotai_network_sd_zhaotai/ImageRepo/raw/master/2021/images/20210312211118.png)

#### 官方指导MVVM架构

![MVVM架构示意图](https://gitee.com/shandong_zhaotai_network_sd_zhaotai/ImageRepo/raw/master/2021/images/20210312211213.png)

- View层类关系图

![](https://gitee.com/shandong_zhaotai_network_sd_zhaotai/ImageRepo/raw/master/2021/images/20210312211131.png)

- ViewModel层类关系图

![](https://gitee.com/shandong_zhaotai_network_sd_zhaotai/ImageRepo/raw/master/2021/images/20210312211135.png)

### 组件化实现

基于阿里 `ARouter` 作为路由，实现组件与组件的通信跳转

https://github.com/alibaba/ARoute

### 集成模式和组件模式转换

Module的属性是在每个组件的 build.gradle 文件中配置的，当我们在组件模式开发时，业务组件应处于application属性，这时的业务组件就是一个 Android
App，可以独立开发和调试；而当我们转换到集成模式开发时，业务组件应该处于 library 属性，这样才能被我们的“app壳工程”所依赖，组成一个具有完整功能的APP

先打开 `BaseDemo` 工程的根目录下找到 `gradle.properties` 文件，然后将 `isModule` 改为你需要的开发模式（true/false），
然后点击 `Sync Project` 按钮同步项目

```properties
isModule=false
```

![](https://gitee.com/shandong_zhaotai_network_sd_zhaotai/ImageRepo/raw/master/2021/images/20210312211140.jpg)

```gradle
if (isModule.toBoolean()) {
    apply plugin: 'com.android.application'
} else {
    apply plugin: 'com.android.library'
}
```

![](https://gitee.com/shandong_zhaotai_network_sd_zhaotai/ImageRepo/raw/master/2021/images/20210312211145.jpg)

### 组件之间AndroidManifest合并问题

我们可以为组件开发模式下的业务组件再创建一个
AndroidManifest.xml，然后根据isModule指定AndroidManifest.xml的文件路径，让业务组件在集成模式和组件模式下使用不同的AndroidManifest.xml，这样表单冲突的问题就可以规避了
已module_main组件为例配置如下：

![](https://gitee.com/shandong_zhaotai_network_sd_zhaotai/ImageRepo/raw/master/2021/images/20210312211150.jpg)

```gradle
sourceSets {
        main {
            jniLibs.srcDirs = ['libs']
            if (isModule.toBoolean()) {
                manifest.srcFile 'src/main/module/AndroidManifest.xml'
            } else {
                manifest.srcFile 'src/main/AndroidManifest.xml'
                java {
                    exclude 'debug/**'
                }
            }
        }
    }
```

### 组件模式下的Application

在每个组件的debug目录下创建一个Application并在module下的AndroidManifest.xml进行配置

![](https://gitee.com/shandong_zhaotai_network_sd_zhaotai/ImageRepo/raw/master/2021/images/20210312211206.jpg)

### 集成开发模式下的Application

排除掉 `debug/**` 下面的Application，使用 `moudle_app ` 壳子下面的 Application,其必须继承 `ModuleApplication`

```kotlin
class MyApp : ModuleApplication()
```

### build.gradle 管理

- 第三方 lib 源码库 且都需引入 `lib.build.gradle`,每个单独 moudle 都需要引入 `module.build.gradle`
- `versions.gradle` 依赖三方库版本统一管理
- `base.build.gradle` 基础编译版本统一管理

### 开发环境

- Android studio：3.6、4.0
- gradle版本：.6.5
- gradle android plugin：4.0.2

### 版本规范

- minSdkVersion(最低适配版本)：21 (Android 5.0)
- targetSdkVersion(最高适配版本)：30 (Adnroid 10.0)
- compileSdkVersion(编译版本)：30 (Adnroid 10.0)
- buildToolsVersion(SDK 构建版本)：30.0.2

## 问题反馈

欢迎 Start ，打call https://github.com/zhouhuandev/BaseDemo 在使用中有任何问题，请留言

邮箱：zhouhuandev@gmail.com

## 关于作者

```
Name : "zhouhuandev",
Blog : "https://blog.csdn.net/youxun1312"
```

## 微信交流群

搜索 `zhouhuandev` 加我微信好友，注意请添加备注：昵称-BaseDemo用户，否则你的申请将不会被验证通过。

![微信二维码](https://gitee.com/zhouhuandev/ImageRepo/raw/master/2022/images/20220409-090729.jpeg)

![BaseDemo交流群](https://gitee.com/zhouhuandev/ImageRepo/raw/master/2022/images/20220409-093018.jpeg)

## 感谢第三方开源

- [YImagePicker](https://github.com/yangpeixing/YImagePicker)
- [KLog](https://github.com/ZhaoKaiQiang/KLog)
- [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)
- [BRVAH](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
- [android-zanpakuto](https://github.com/alvince/android-zanpakuto)

注：还有一大部分对此项目提供帮助的开源大佬们，就不一一列出，在此衷心感谢，若是有不足地方麻烦指导改正。

## 日志

- 2021.8.14 优化整体架构，升级依赖版本
- 2021.11.23 优化架构，更新刷新框架、Adapter适配器，修复已知问题
- 2022.01.14 优化`ViewBinding` & `DataBinding`、新增部分拓展函数
- 2022.05.21 重构依赖架构，修复 Binding 问题

## License

```
Copyright (C)  zhouhuan, BaseDemo Framework Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```