package top.horsttop.optimizedkt.ui.activity

import android.view.View
import top.horsttop.appcore.ui.base.BaseActivity
import top.horsttop.optimizedkt.R
import top.horsttop.optimizedkt.core.App
import top.horsttop.optimizedkt.di.component.DaggerViewComponent
import top.horsttop.optimizedkt.ui.mvpview.KtMvpView
import top.horsttop.optimizedkt.ui.presenter.KtPresenter
import javax.inject.Inject

/**
 * Created by horsttop on 2018/4/26.
 */
class KtActivity : BaseActivity(), KtMvpView {
    override val contentViewId: Int
        = R.layout.activity_test

    override fun initViews() {
    }

    override fun onClick(v: View?) {
    }

    @Inject
    lateinit var mPresenter: KtPresenter

    override fun onActivityInject() {
        DaggerViewComponent.builder()
                        .appComponent(App.appComponent)
                        .build()
                        .inject(this)
        mPresenter.attachView(this)
    }


}