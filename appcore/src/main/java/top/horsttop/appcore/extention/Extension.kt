package top.horsttop.appcore.extention

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.internal.operators.flowable.FlowableInternalHelper
import io.reactivex.schedulers.Schedulers
import top.horsttop.appcore.ui.base.MvpView

/**
 * Created by horsttop on 2018/4/11.
 */

/*************
 *    Context
 ************/
fun Context.ofColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}


fun Context.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun Context.px2dp(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * 获取屏幕宽度
 */
fun Context.ofScreenWidth(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}

/**
 * 获取屏幕高度
 */
fun Context.ofScreenHeight(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.y
}

/*************
 *   Activity
 ************/

fun Activity.startActivity(view: View, clazz: Class<*>, bundle: Bundle? = null) {
    val intent = Intent(this, clazz)
    if (null != bundle)
        intent.putExtras(bundle)
    val options = ActivityOptionsCompat.makeScaleUpAnimation(view,
            view.width / 2, view.height / 2,
            0, 0)
    ActivityCompat.startActivity(this, intent, options.toBundle())
}

fun Activity.startActivity(holdView: View, clazz: Class<*>, bundle: Bundle?, holdTag: String) {
    val intent = Intent(this, clazz)
    if (null != bundle)
        intent.putExtras(bundle)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val options = ActivityOptions.makeSceneTransitionAnimation(this, holdView, holdTag)
        ActivityCompat.startActivity(this, intent, options.toBundle())
    } else {
        startActivity(holdView, clazz, bundle)
//        activity.overridePendingTransition(R.anim.right_in,R.anim.left_out)
    }
}


/**
 * run on mainThread
 */
fun <T> Flowable<T>.runOnMainThread(): Flowable<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

/**
 * super subscribe
 */
fun <T> Flowable<T>.subscribeX(onNext: Consumer<in T>, mvpView: MvpView? = null,onError:(t:Throwable) ->Unit?={}): Disposable {
    return if (mvpView == null) {
        subscribe(onNext, Consumer<Throwable> {

        }, Functions.EMPTY_ACTION, FlowableInternalHelper.RequestMax.INSTANCE)
    } else {
        subscribe(onNext, Consumer<Throwable> {
            mvpView.onDataError()
            onError(it)
        }, Functions.EMPTY_ACTION, FlowableInternalHelper.RequestMax.INSTANCE)
    }

}

fun <T> Flowable<T>.subscribeX(onNext: Consumer<in T>, onError: Consumer<Throwable>): Disposable {
    return subscribe(onNext, onError, Functions.EMPTY_ACTION, FlowableInternalHelper.RequestMax.INSTANCE)
}