package top.horsttop.appcore.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.horsttop.appcore.load.callback.EmptyCallback
import top.horsttop.appcore.load.callback.ErrorCallback
import top.horsttop.appcore.load.callback.LoadingCallback
import top.horsttop.appcore.load.callback.TimeoutCallback
import top.horsttop.appcore.load.core.LoadService
import top.horsttop.appcore.load.core.Loader
import top.horsttop.appcore.util.alert.AlertHelper
import top.horsttop.appcore.util.progress.PostDialog


/**
 * Created by horsttop on 15/12/30.
 */
abstract class BaseFragment : Fragment(), MvpView {
    /**
     * 引入布局文件
     * @return
     */
    protected abstract val contentViewId: Int

    protected lateinit var rootView: View

    /**
     * contentView
     */
    protected  var mLoadingArea : View ?= null
        private set

    /**
     * Loader
     */
    protected  var mBaseLoadService : LoadService<*>?= null


    private var mPresenter: BasePresenter<*>? = null


    /**
     * 获取Presenter 引入Presenter,在方法体中给mPresenter赋值
     *
     * @return
     */
    protected abstract fun onFragmentInject()

    /**
     * 界面初始化操作
     */
    protected abstract fun initViews()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onFragmentInject()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        try {
            rootView = inflater.inflate(contentViewId, container, false)


        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: Exception) {
            throw BaseActivity.InitViewException(e.message!!)
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        if (mLoadingArea != null) {
            mBaseLoadService = Loader.getDefault().register(mLoadingArea) { it ->
                onReload(it)
            }
        }
    }

    override fun setPresenter(presenter: BasePresenter<*>) {
        this.mPresenter = presenter
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        AlertHelper.dismissTipDialog()
    }

    fun setUpLoadingArea(view: View){
        mLoadingArea = view
    }

    override fun onLoading(tip: String) {
        mBaseLoadService?.showCallback(LoadingCallback::class.java)
    }

    override fun onDataError(error: String) {
        mBaseLoadService?.showCallback(ErrorCallback::class.java)
    }

    override fun onNetworkError() {
        mBaseLoadService?.showCallback(TimeoutCallback::class.java)
    }

    override fun onPageEmpty() {
        mBaseLoadService?.showCallback(EmptyCallback::class.java)
    }

    override fun onPageSuccess() {
        mBaseLoadService?.showSuccess()
    }

    override fun onPost(tip: String) {
        PostDialog.showProgress(context!!,tip)
    }

    override fun onPostEnd() {
        PostDialog.dismiss()
    }

    open fun onReload(view: View){

    }

    override fun onDestroy() {
        mPresenter?.detachView()
        PostDialog.dismiss()
        super.onDestroy()

    }

}
