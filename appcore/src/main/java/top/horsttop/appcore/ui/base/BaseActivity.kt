package top.horsttop.appcore.ui.base

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import top.horsttop.appcore.R
import top.horsttop.appcore.core.GenApp
import top.horsttop.appcore.extention.ofColor
import top.horsttop.appcore.load.callback.*
import top.horsttop.appcore.load.core.LoadService
import top.horsttop.appcore.load.core.Loader
import top.horsttop.appcore.statusbar.StatusBarUtil
import top.horsttop.appcore.util.alert.AlertHelper
import top.horsttop.appcore.util.progress.PostDialog
import top.horsttop.core.exception.Cockroach

import java.lang.Exception
import java.lang.NullPointerException
import java.lang.RuntimeException


/**
 * Created by horsttop on 15/12/30.
 */
abstract class BaseActivity : AppCompatActivity(), MvpView, View.OnClickListener {

    /**
     * 根布局
     */
    protected lateinit var mRootView: View


    /**
     * Loader
     */
    protected var mBaseLoadService: LoadService<*>? = null


    /**
     * 引入布局文件
     *
     * @return
     */
    protected abstract val contentViewId: Int


    private var mPresenter: BasePresenter<*>? = null


    /**
     * 界面初始化操作
     */
    protected abstract fun initViews()


    /**
     * 获取Presenter 引入Presenter,在方法体中给mPresenter赋值
     *
     * @return
     */
    protected abstract fun onActivityInject()

    /**
     * 界面唤醒时需要的操作
     */
    protected open fun resumeViews() {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {

            mRootView = View.inflate(this, contentViewId, null)

            setContentView(mRootView)

            initStatusBar()

            GenApp.pushActivity(this)
            onActivityInject()
            initViews()

        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: Exception) {
            throw InitViewException(e.message!!)
        }
    }

    open fun initStatusBar() {
        StatusBarUtil.setColor(this, this.ofColor(R.color.colorPrimary))
    }

    override fun onResume() {
        super.onResume()
        resumeViews()
    }


    protected fun ofLoadingArea(loadingArea:View){
        mBaseLoadService = Loader.getDefault().register(loadingArea) { it ->
            onReload(it)
        }
    }

    override fun setPresenter(presenter: BasePresenter<*>) {
        this.mPresenter = presenter
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
        PostDialog.showProgress(this,tip)
    }

    override fun onPostEnd() {

        PostDialog.dismiss()
    }


    fun onReload(view: View) {

    }


    override fun onDestroy() {
        super.onDestroy()
        GenApp.popActivity(this)
        PostDialog.dismiss()
        mPresenter?.detachView()

    }

    class InitViewException(msg: String) : RuntimeException(msg)


}
