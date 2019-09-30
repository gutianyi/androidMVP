package top.horsttop.optimizedkt.ui.presenter

import javax.inject.Inject
import top.horsttop.appcore.ui.base.BasePresenter
import top.horsttop.optimizedkt.model.api.HttpApi
import top.horsttop.optimizedkt.ui.mvpview.RecyclerMvpView

/**
 * Created by horsttop on 2018/5/23.
 */
class RecyclerPresenter @Inject constructor(var api: HttpApi) : BasePresenter<RecyclerMvpView>() {
}