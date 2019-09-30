package top.horsttop.appcore.extention

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.internal.operators.flowable.FlowableInternalHelper
import io.reactivex.schedulers.Schedulers
import top.horsttop.appcore.ui.base.MvpView
import java.util.regex.Pattern

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

fun Activity.startActivityForResult(view: View, clazz: Class<*>, bundle: Bundle? = null) {
    val intent = Intent(this, clazz)
    if (null != bundle)
        intent.putExtras(bundle)
    val options = ActivityOptionsCompat.makeScaleUpAnimation(view,
            view.width / 2, view.height / 2,
            0, 0)
    ActivityCompat.startActivityForResult(this, intent,1, options.toBundle())
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
fun <T> Flowable<T>.subscribeX(onNext: Consumer<in T>, mvpView: MvpView? = null, onError:(t:Throwable) ->Unit?={}): Disposable {
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

fun onViewsClick(listener: View.OnClickListener, vararg views: View?) {
    views.forEach {
        it?.onClick(listener)
    }
}

fun setEditViewText(et: EditText,text:String){
    et.text.clear()
    et.text.append(text)
}

fun nullBackStr(str:String?):String{
    return if (str == null)
        ""
    else
        str
}

fun getNotNullExtresString(ex:Bundle, key:String):String{
    return if (ex.getString(key) == null)
        ""
    else
        ex.getString(key)
}


/*
        扩展点击事件
     */
fun View.onClick(listener: View.OnClickListener): View {
    setOnClickListener(listener)
    return this
}

/*
    扩展点击事件，参数为方法
 */
fun View.onClick(method: () -> Unit): View {
    setOnClickListener { method() }
    return this
}

/**
 * 判断String中是否包含中文
 * @param str
 * @return
 */
fun isContainChinese(str: String): Boolean {

    val p = Pattern.compile("[\u4e00-\u9fa5]")
    val m = p.matcher(str)
    return if (m.find()) {
        true
    } else false
}


fun fetchVersionCode(context: Context): Int {

    val pm = context.packageManager//context为当前Activity上下文
    try {
        val pi = pm.getPackageInfo(context.packageName, 0)
        return pi.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        return 0
    }

}


fun fetchVersionName(context: Context): String {

    val pm = context.packageManager//context为当前Activity上下文
    try {
        val pi = pm.getPackageInfo(context.packageName, 0)
        return pi.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        return ""
    }

}