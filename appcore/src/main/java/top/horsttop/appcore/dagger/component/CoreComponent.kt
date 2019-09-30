package top.horsttop.appcore.dagger.component

import android.app.Application
import com.squareup.moshi.Moshi
import dagger.Component
import retrofit2.Retrofit
import top.horsttop.appcore.dagger.module.ApplicationModule
import top.horsttop.appcore.dagger.module.NetworkModule
import javax.inject.Singleton

/**
 * Created by horsttop on 2018/4/19.
 */
@Singleton
@Component (modules = arrayOf(ApplicationModule::class,NetworkModule::class))
interface CoreComponent {

    fun application():Application

    fun moshi():Moshi

    fun retrofit():Retrofit

}