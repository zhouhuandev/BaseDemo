package com.hzsoft.basedemo.di

import com.hzsoft.basedemo.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModuleBuilder {

    @ContributesAndroidInjector()
    abstract fun contributeMainActivity(): MainActivity
}
