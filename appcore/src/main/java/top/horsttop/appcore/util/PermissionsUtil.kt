package top.horsttop.appcore.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import top.horsttop.appcore.model.constant.GlobalVal
import top.horsttop.appcore.util.alert.AlertHelper

/**
 * Created by horsttop on 07/12/2017.
 */
object PermissionsUtil{

    fun permissionOfCamera(activity:Activity,hasCamera:() -> Unit){
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), GlobalVal.PERMISSIONS_REQUEST_CAMERA)
            } else {
                hasCamera()
            }
        } else {
           hasCamera()
        }
    }


     fun requestPermission(activity: Activity, permission: String, rationale: String, requestCode: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            AlertHelper.showTipDialog(activity, rationale, DialogInterface.OnClickListener { dialogInterface, i ->
                ActivityCompat.requestPermissions(activity,
                        arrayOf(permission), requestCode)
                AlertHelper.dismissTipDialog()
            })
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        }
    }
}