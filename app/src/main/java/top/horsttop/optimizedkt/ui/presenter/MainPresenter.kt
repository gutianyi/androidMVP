package top.horsttop.optimizedkt.ui.presenter

import io.reactivex.functions.Consumer
import timber.log.Timber
import top.horsttop.appcore.extention.runOnMainThread
import top.horsttop.appcore.extention.subscribeX
import javax.inject.Inject
import top.horsttop.appcore.ui.base.BasePresenter
import top.horsttop.optimizedkt.model.api.HttpApi
import top.horsttop.optimizedkt.ui.mvpview.MainMvpView

/**
 * Created by horsttop on 2018/5/23.
 */
class MainPresenter @Inject constructor(var api: HttpApi

                                            ) : BasePresenter<MainMvpView>() {

    fun test(){

        api.fetchMsg()
                .runOnMainThread()
                .subscribeX(Consumer{ it ->
                    Timber.d(it.toString())
                },mvpView)


    }
}