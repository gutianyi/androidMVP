package top.horsttop.appcore.ui.base

/**
 * Created by horsttop on 22/11/2017.
 */
interface MvpView {

    fun setPresenter(presenter: BasePresenter<*>)

    fun onLoading (tip:String = "")

    fun onDataError (error:String = "")

    fun onPageEmpty()

    fun onPageSuccess()

    fun onNetworkError()

    fun onPost(tip:String = "")

    fun onPostEnd()

}