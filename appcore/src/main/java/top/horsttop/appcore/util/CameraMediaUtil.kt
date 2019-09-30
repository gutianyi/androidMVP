package top.horsttop.appcore.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import java.io.File
import android.content.ContentUris
import android.provider.DocumentsContract
import android.support.v4.app.Fragment
import top.horsttop.appcore.model.constant.GlobalVal.PERMISSION_REQUEST_STORAGE_READ_ACCESS
import top.horsttop.appcore.util.PermissionsUtil.requestPermission


/**
 * Created by horsttop on 07/12/2017.
 */
object CameraMediaUtil {

    const val REQUEST_SELECT_PICTURE = 0x01
    const val REQUEST_SELECT_CAMERA = 0x02

    var filePath = ""
        private set

    /**
     * 照相时图片的保存名字
     */
    private val CAMERA_FILE_NAME = "/temp.jpeg"


    fun pickFromCamera(activity: Activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE,
                    "选择图片需要文件读取权限",
                    PERMISSION_REQUEST_STORAGE_READ_ACCESS)
        } else {
            //            Intent intent = new Intent(Intent.ACTION_PICK, null);
            filePath = Environment.getExternalStorageDirectory().path + CAMERA_FILE_NAME
            // 打开照相机
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                val contentUri = FileProvider.getUriForFile(activity, "cn.xiaohuodui.waterservice.fileProvider", File(filePath))
                //  intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
            } else {
                // 设置相片保存路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(File(filePath)))
            }
            activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_SELECT_CAMERA)
        }
    }

    fun pickFromCamera(fragment: Fragment) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && ActivityCompat.checkSelfPermission(fragment.activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(fragment.activity!!, Manifest.permission.READ_EXTERNAL_STORAGE,
                    "选择图片需要文件读取权限",
                    PERMISSION_REQUEST_STORAGE_READ_ACCESS)
        } else {
            //            Intent intent = new Intent(Intent.ACTION_PICK, null);
            filePath = Environment.getExternalStorageDirectory().path + CAMERA_FILE_NAME
            // 打开照相机
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                val contentUri = FileProvider.getUriForFile(fragment.activity!!, "top.horsttop.village.fileProvider", File(filePath))
                //  intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
            } else {
                // 设置相片保存路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(File(filePath)))
            }
            fragment.startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_SELECT_CAMERA)
        }
    }

    fun pickFromGallery(activity: Activity) {
        // 打开图片库
        val intent = Intent(Intent.ACTION_PICK, null)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        activity.startActivityForResult(intent, REQUEST_SELECT_PICTURE)
    }

    fun pickFromGallery(fragment: Fragment) {
        // 打开图片库
        val intent = Intent(Intent.ACTION_PICK, null)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        fragment.startActivityForResult(intent, REQUEST_SELECT_PICTURE)
    }
    fun getRealPathFromUri(context: Context, uri: Uri): String?{
        val sdkVersion = Build.VERSION.SDK_INT
        return if (sdkVersion >= 19) { // api >= 19
            getRealPathFromUriAboveApi19(context, uri)
        } else { // api < 19
            getRealPathFromUriBelowAPI19(context, uri)
        }
    }


    private fun getRealPathFromUriBelowAPI19(context: Context, uri: Uri): String? {
        return getDataColumn(context, uri)
    }

    @SuppressLint("NewApi")
    private fun getRealPathFromUriAboveApi19(context: Context, uri: Uri): String? {
        var filePath: String? = null
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            val documentId = DocumentsContract.getDocumentId(uri)
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                val id = documentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(id)
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs)
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(documentId)!!)
                filePath = getDataColumn(context, contentUri, null, null)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null)
        } else if ("file" == uri.scheme) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.path
        }
        return filePath
    }

    private fun getDataColumn(context: Context, uri: Uri, selection: String? = null, selectionArgs: Array<String>? = null): String? {
        var path: String? = null

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor!!.moveToFirst()) {
                val columnIndex = cursor!!.getColumnIndexOrThrow(projection[0])
                path = cursor!!.getString(columnIndex)
            }
        } catch (e: Exception) {
            if (cursor != null) {
                cursor!!.close()
            }
        }

        return path
    }


    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

}