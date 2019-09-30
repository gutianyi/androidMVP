package top.horsttop.appcore.ui.base

import java.lang.ref.WeakReference


/**
 * Presenter层的基础实现类
 * Created by horsttop on 15/12/30.
 */
open class BasePresenter<V : MvpView>: Presenter<V> {

    private var weakReference: WeakReference<V>? = null

    override fun attachView(view: V) {
        if (!isViewAttached) {
            weakReference = WeakReference(view)
            view.setPresenter(this)
        }
    }

    override fun detachView() {
        weakReference?.clear()
        weakReference = null
    }

    val mvpView: V?
        get() = weakReference?.get()

    private val isViewAttached: Boolean
        get() = weakReference != null && weakReference!!.get() != null


}
