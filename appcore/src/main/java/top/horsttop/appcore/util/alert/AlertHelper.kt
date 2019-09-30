package top.horsttop.appcore.util.alert

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import top.horsttop.appcore.R


/**
 * Created by horsttop on 16/12/2016.
 */

object AlertHelper {

    /**
     * 等待框
     */
    var sProgressDialog: ProgressDialog? = null

    var sAlertDialog: AlertDialog? = null

    fun showProgressDialog(context: Context, msg: String) {
        if (null == sProgressDialog) {
            sProgressDialog = ProgressDialog(context, R.style.WaitingProgressDialog)
            sProgressDialog!!.setCancelable(false)
        }
        if (!TextUtils.isEmpty(msg))
            sProgressDialog!!.setMessage(msg)
        else
            sProgressDialog!!.setMessage("请等待...")
        sProgressDialog!!.show()
    }

    fun dismissProgressDialog() {
        if (null != sProgressDialog && sProgressDialog!!.isShowing) {
            sProgressDialog!!.dismiss()
            sProgressDialog = null
        }
    }

    fun dropProgressDialog() {
        if (null != sProgressDialog) {
            sProgressDialog!!.dismiss()
            sProgressDialog = null
        }
        if (null != sAlertDialog) {
            sAlertDialog!!.dismiss()
            sAlertDialog = null
        }
    }

    fun showTipDialog(context: Context, tip: String) {
        if (null == sAlertDialog)
            sAlertDialog = AlertDialog.Builder(context)
                    //                .setTitle(R.string.tip)
                    //                .setIcon(R.mipmap.ic_logo)
                    .setCancelable(false)
                    .setMessage(tip)
                    .setPositiveButton(R.string.confirm, DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                    .create()
        sAlertDialog!!.show()
    }

    fun showTipDialog(context: Context, tip: String, confirmClickListener: DialogInterface.OnClickListener) {
        if (null == sAlertDialog)
            sAlertDialog = AlertDialog.Builder(context)
                    //                    .setTitle(R.string.tip)
                    .setCancelable(false)
                    //                    .setIcon(R.mipmap.ic_logo)
                    .setMessage(tip)
                    .setPositiveButton(R.string.confirm, confirmClickListener)
                    .create()
        sAlertDialog!!.show()
    }

    fun showTipDialog(context: Context, tip: String, confirmClickListener: DialogInterface.OnClickListener, cancelClickListener: DialogInterface.OnClickListener) {
        if (null == sAlertDialog)
            sAlertDialog = AlertDialog.Builder(context)
                    //                    .setTitle(R.string.tip)
                    .setCancelable(false)
                    //                    .setIcon(R.mipmap.ic_logo)
                    .setMessage(tip)
                    .setPositiveButton(R.string.confirm, confirmClickListener)
                    .setNegativeButton(R.string.cancel, cancelClickListener)
                    .create()
        sAlertDialog!!.show()
    }

    fun showTipDialog(context: Context, title: String, tip: String, confirmClickListener: DialogInterface.OnClickListener, cancelClickListener: DialogInterface.OnClickListener) {
        if (null == sAlertDialog)
            sAlertDialog = AlertDialog.Builder(context)
                    //                    .setTitle(title)
                    .setCancelable(false)
                    //                    .setIcon(R.mipmap.ic_logo)
                    .setMessage(tip)
                    .setPositiveButton(R.string.confirm, confirmClickListener)
                    .setNegativeButton(R.string.cancel, cancelClickListener)
                    .create()
        sAlertDialog!!.show()
    }

    fun showTipDialogNoTitle(context: Context, tip: String, confirmClickListener: DialogInterface.OnClickListener, cancelClickListener: DialogInterface.OnClickListener) {
        if (null == sAlertDialog)
            sAlertDialog = AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setMessage(tip)
                    .setPositiveButton(R.string.confirm, confirmClickListener)
                    .setNegativeButton(R.string.cancel, cancelClickListener)
                    .create()
        sAlertDialog!!.show()
    }


    fun dismissTipDialog() {
        if (null != sAlertDialog && sAlertDialog!!.isShowing) {
            sAlertDialog!!.dismiss()
            sAlertDialog = null
        }

    }

    fun closeSoftInputWindow(imm: InputMethodManager, activity: Activity) {
        if (activity.currentFocus!!.windowToken != null) {
            imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    fun showPwdDialog(context: Context, view: View) {
        if (null == sAlertDialog)
            sAlertDialog = AlertDialog.Builder(context)
                    //                    .setTitle(R.string.tip)
                    .setCancelable(true)
                    //                    .setIcon(R.mipmap.ic_logo)
                    .setView(view)
                    //.setPositiveButton(R.string.confirm, confirmClickListener)
                    .create()
        sAlertDialog!!.show()
    }
}
