package top.horsttop.appcore.ui.widget

import android.content.Context
import android.view.View

/**
 * Created by horsttop on 03/08/2017.
 */

open class AlertSheet(var mContext:Context) {


    protected var mSheet: BottomSheet? = null

//    protected var mContentView: View? = null


    open fun show() {
        if (null != mSheet)
            mSheet!!.show()
    }

    fun dismiss() {
        if (null != mSheet)
            mSheet!!.dismiss()
    }
}
