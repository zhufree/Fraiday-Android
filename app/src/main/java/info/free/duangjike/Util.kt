package info.free.duangjike

import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by zhufree on 2018/12/13.
 *
 */

object Util {
    /**
     * bitmap保存为一个文件
     * @param bitmap bitmap对象
     * @return 文件对象
     */
    fun saveBitmapFile(bitmap: Bitmap, filename: String): File {
        val filePath = getAlbumStorageDir("DuangJike").path + "/$filename"
        val file = File("$filePath.jpg")
        try {
            val outputStream = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val contentResolver = DuangApplication.context.contentResolver
        val values = ContentValues(4)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.ORIENTATION, 0)
        values.put(MediaStore.Images.Media.TITLE, "friday")
        values.put(MediaStore.Images.Media.DESCRIPTION, "friday")
        values.put(MediaStore.Images.Media.DATA, file.absolutePath)
        values.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000)
        var url: Uri? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DuangApplication.context.grantUriPermission(DuangApplication.context.packageName,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            url = contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) //其实质是返回 Image.Meida.DATA中图片路径path的转变而成的uri
            val imageOut = contentResolver?.openOutputStream(url)
            imageOut?.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOut)
            }

            val id = ContentUris.parseId(url)
            MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MINI_KIND, null)//获取缩略图

        } catch (e: Exception) {
            if (url != null) {
                contentResolver?.delete(url, null, null)
            }
        }
        return file
    }

    fun getAlbumStorageDir(albumName: String): File {
        // Get the directory for the user's public pictures directory.
        val file = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName)
        if (!file.mkdirs() && !file.exists()) {
            Log.e("jike", "Directory not created")
        }
        return file
    }
}