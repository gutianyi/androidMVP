package top.horsttop.optimizedkt.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_recycler.*
import top.horsttop.appcore.model.constant.GlobalVal
import top.horsttop.appcore.ui.base.BaseActivity
import top.horsttop.appcore.ui.recyclerview.ProgressStyle
import top.horsttop.appcore.ui.recyclerview.XRecyclerView
import top.horsttop.optimizedkt.R
import top.horsttop.optimizedkt.core.App
import top.horsttop.optimizedkt.di.component.DaggerViewComponent
import top.horsttop.optimizedkt.ui.adapter.RecyclerAdapter
import top.horsttop.optimizedkt.ui.mvpview.RecyclerMvpView
import top.horsttop.optimizedkt.ui.presenter.RecyclerPresenter
import javax.inject.Inject

/**
 * Created by horsttop on 2018/5/23.
 */
class RecyclerActivity : BaseActivity(), RecyclerMvpView,XRecyclerView.LoadingListener {
    override fun onRefresh() {
        mPage = 0
        recycler.postDelayed({
            recycler.refreshComplete()
        },1000)
    }

    override fun onLoadMore() {
        mPage++
        recycler.postDelayed({
            setUpData()
            mAdapter.notifyDataSetChanged()
            recycler.loadMoreComplete()
        },1000)
    }

    @Inject
    lateinit var mPresenter: RecyclerPresenter

    override val contentViewId: Int = R.layout.activity_recycler

    lateinit var mAdapter:RecyclerAdapter

    private val  strList = arrayListOf<String>()

    private var mPage = 0


    override fun initViews() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUpData()
        mAdapter = RecyclerAdapter(strList)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader)
        recycler.setLoadingMoreProgressStyle(ProgressStyle.BallRotate)
        recycler.adapter = mAdapter
        recycler.setLoadingListener(this)
    }

    override fun onClick(v: View?) {

    }


    private fun setUpData(){

        for (i in mPage*GlobalVal.PAGE_LIMIT..(mPage+1)*GlobalVal.PAGE_LIMIT){
            strList.add("这是第${i}个元素")
        }
    }

    override fun onActivityInject() {
        DaggerViewComponent.builder()
                .appComponent(App.appComponent)
                .build()
                .inject(this)
        mPresenter.attachView(this)
    }


}