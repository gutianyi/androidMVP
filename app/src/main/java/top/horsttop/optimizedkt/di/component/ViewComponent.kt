package top.horsttop.optimizedkt.di.component

import dagger.Component
import top.horsttop.appcore.dagger.runtime.PerView
import top.horsttop.optimizedkt.ui.activity.KtActivity
import top.horsttop.optimizedkt.ui.activity.MainActivity
import top.horsttop.optimizedkt.ui.activity.RecyclerActivity

/**
 * Created by horsttop on 2018/4/23.
 */
@PerView
@Component(dependencies = arrayOf(AppComponent::class))
interface ViewComponent {

    fun inject(testActivity: RecyclerActivity)
    fun inject(testActivity: KtActivity)
    fun inject(testActivity: MainActivity)

}