package top.horsttop.appcore.core

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.squareup.moshi.Moshi
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import top.horsttop.appcore.BuildConfig
import top.horsttop.appcore.dagger.component.CoreComponent
import top.horsttop.appcore.dagger.component.DaggerCoreComponent
import top.horsttop.appcore.dagger.module.ApplicationModule
import top.horsttop.appcore.load.callback.EmptyCallback
import top.horsttop.appcore.load.callback.ErrorCallback
import top.horsttop.appcore.load.callback.LoadingCallback
import top.horsttop.appcore.load.callback.TimeoutCallback
import top.horsttop.appcore.load.core.Loader
import top.horsttop.appcore.util.PreferencesHelper
import top.horsttop.appcore.util.toast.ToastUtil
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject

/**
 * Created by horsttop on 2018/4/13.
 */
open class GenApp : Application() {

    companion object {
        //platformStatic allow access it from java code
        @JvmStatic lateinit var coreComponent: CoreComponent
        @JvmStatic lateinit var instance: GenApp
        lateinit var bus:EventBus
        lateinit var moshi:Moshi

        private val sActivityStack = Stack<Activity>()

        /**
         * 退出应用时销毁所有的Activity
         */
        fun clear() {
            try {
                for (activity in sActivityStack) {
                    activity?.finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                //System.exit(0);
            }
        }

        /**
         * Activity 压栈
         *
         * @param activity Activity
         */
        fun pushActivity(activity: Activity) {
            sActivityStack.add(activity)
        }

        /**
         * 出栈
         *
         * @param activity
         */
        fun popActivity(activity: Activity) {
            if (sActivityStack.contains(activity))
                sActivityStack.remove(activity)
        }

        private var currentTime = 0L
        private var lastTime = 0L
        fun exit() {
            currentTime = Date().time
            if (currentTime - lastTime < 2000) {
                GenApp.clear()
            }
            lastTime = currentTime
            ToastUtil.showShort("再按一次退出")
        }
    }

    @Inject
    public lateinit var preferencesHelper: PreferencesHelper


    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        instance = this
        bus = EventBus()

        initDagger()

        Loader.beginBuilder()
                .addCallback(ErrorCallback())
                .addCallback(EmptyCallback())
                .addCallback(LoadingCallback())
                .addCallback(TimeoutCallback())
                .setDefaultCallback(LoadingCallback::class.java)
                .commit()
    }


    private fun initDagger() {
        coreComponent = DaggerCoreComponent.builder().applicationModule(ApplicationModule(this)).build()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}