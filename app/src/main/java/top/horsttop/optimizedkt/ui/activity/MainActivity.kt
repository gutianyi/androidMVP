package top.horsttop.optimizedkt.ui.activity

import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import top.horsttop.appcore.extention.startActivity
import top.horsttop.appcore.ui.base.BaseActivity
import top.horsttop.appcore.util.net.NetworkStateReceiver
import top.horsttop.optimizedkt.R
import top.horsttop.optimizedkt.core.App
import top.horsttop.optimizedkt.di.component.DaggerViewComponent
import top.horsttop.optimizedkt.ui.mvpview.MainMvpView
import top.horsttop.optimizedkt.ui.presenter.MainPresenter
import javax.inject.Inject

/**
 * Created by horsttop on 2018/5/23.
 */
class MainActivity : BaseActivity(), MainMvpView {

    @Inject
    lateinit var mPresenter: MainPresenter

    override val contentViewId: Int = R.layout.activity_main

    override fun initViews() {
        btn_1.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_1 ->{
                startActivity(btn_1,RecyclerActivity::class.java)
            }
        }
    }

    override fun onActivityInject() {
        DaggerViewComponent.builder()
                .appComponent(App.appComponent)
                .build()
                .inject(this)
        mPresenter.attachView(this)
    }

    override fun onStart() {
        super.onStart()
        NetworkStateReceiver.registerNetworkStateReceiver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        NetworkStateReceiver.unRegisterNetworkStateReceiver(this)
    }
}