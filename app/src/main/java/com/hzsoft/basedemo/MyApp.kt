package com.hzsoft.basedemo

import com.hzsoft.basedemo.di.AppComponent
import com.hzsoft.basedemo.di.DaggerAppComponent
import com.hzsoft.basedemo.di.holder.NetComponentHolder
import com.hzsoft.basedemo.di.provider.NetComponentProvider
import com.hzsoft.lib.common.BaseApplication
import com.hzsoft.lib.net.config.NetConfig
import com.hzsoft.lib.net.di.NetComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * Describe:
 * App
 *
 * @author zhouhuan
 * @Date 2020/12/1
 */
class MyApp : BaseApplication(), HasAndroidInjector, NetComponentProvider {

    companion object {
        lateinit var instance: MyApp

        fun getContext() = instance.applicationContext
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector


    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        initDagger()
        initNet()
    }


    private fun initNet() {
        val config = NetConfig.Builder()
            .setBaseUrl(URL_BASE)
            .build()
        config.initContext(this)
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.factory().create(this)
    }

    override fun provideNetComponent(): NetComponent {
        return appComponent.netComponentFactory().create(this)
    }
}