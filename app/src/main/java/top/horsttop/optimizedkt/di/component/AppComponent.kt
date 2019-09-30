package top.horsttop.optimizedkt.di.component

import dagger.Component
import top.horsttop.appcore.dagger.component.CoreComponent
import top.horsttop.appcore.dagger.runtime.PerApplication
import top.horsttop.optimizedkt.core.App
import top.horsttop.optimizedkt.di.module.ApiModule
import top.horsttop.optimizedkt.model.api.HttpApi
import javax.inject.Singleton

/**
 * Created by horsttop on 2018/4/23.
 */
@PerApplication
@Component(dependencies = arrayOf(CoreComponent::class),modules = arrayOf(ApiModule::class))
interface AppComponent {

    fun httpApi():HttpApi

    fun inject(app: App)


}