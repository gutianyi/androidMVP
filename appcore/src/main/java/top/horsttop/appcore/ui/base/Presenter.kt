package top.horsttop.appcore.ui.base


/**
 * Presenter层的总父类接口
 * Created by horsttop on 15/12/30.
 */
interface Presenter<in G : MvpView> : IPresenter{


    fun attachView(mvpView: G)

    fun detachView()

}
