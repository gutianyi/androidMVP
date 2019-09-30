package top.horsttop.optimizedkt.core

import timber.log.Timber
import top.horsttop.appcore.core.GenApp
import top.horsttop.optimizedkt.di.component.AppComponent
import top.horsttop.optimizedkt.di.component.DaggerAppComponent
import top.horsttop.optimizedkt.di.module.ApiModule
import top.horsttop.optimizedkt.model.api.HttpApi

/**
 * Created by horsttop on 2018/4/13.
 */
class App : GenApp(){


    override fun onCreate() {
        super.onCreate()

        Timber.d("to inject")
        appComponent = DaggerAppComponent.builder()
                .coreComponent(coreComponent)
                .build()
        appComponent.inject(this)

    }

    companion object {

        @JvmStatic lateinit var appComponent: AppComponent
    }

}