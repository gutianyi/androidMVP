package top.horsttop.optimizedkt.ui.presenter

import top.horsttop.appcore.ui.base.BasePresenter
import top.horsttop.optimizedkt.model.api.HttpApi
import top.horsttop.optimizedkt.ui.mvpview.KtMvpView
import javax.inject.Inject

/**
 * Created by horsttop on 2018/4/26.
 */
class KtPresenter @Inject constructor(var aip:HttpApi): BasePresenter<KtMvpView>()