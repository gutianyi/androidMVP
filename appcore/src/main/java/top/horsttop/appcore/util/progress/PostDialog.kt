package top.horsttop.appcore.util.progress

import android.content.Context
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import top.horsttop.appcore.R

/**
 * Created by horsttop on 2018/5/4.
 */
object PostDialog {
    private var mAlertDialog: AlertDialog? = null


    fun showProgress(context: Context, tip:String = ""){
        if(mAlertDialog==null)
            mAlertDialog = AlertDialog.Builder(context, R.style.ProgressDialog).create()
        val loadView = LayoutInflater.from(context).inflate(R.layout.dialog_progress,null)
        mAlertDialog!!.setView(loadView,0,0,0,0)
        mAlertDialog!!.setCanceledOnTouchOutside(false)
        mAlertDialog!!.show()
        if(TextUtils.isEmpty(tip).not()){
            loadView.findViewById<TextView>(R.id.txt_tip).text = tip
        } else {
            loadView.findViewById<TextView>(R.id.txt_tip).visibility = View.GONE
        }

    }



    fun dismiss(){
        if(mAlertDialog!=null&&mAlertDialog!!.isShowing){
            mAlertDialog?.dismiss()
            mAlertDialog = null
        }

    }
}
