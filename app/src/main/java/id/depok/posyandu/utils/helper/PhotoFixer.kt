package id.depok.posyandu.utils.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.IOException

object PhotoFixer {

    @RequiresApi(Build.VERSION_CODES.N)
    @Throws(IOException::class)
    fun rotateImageIfRequired(context: Context, img: Bitmap, selectedImage: Uri): Bitmap? {
        try {
            val input = context.contentResolver.openInputStream(selectedImage)
            input?.let {
                val ei: ExifInterface = if (Build.VERSION.SDK_INT > 23)
                    ExifInterface(input)
                else ExifInterface(selectedImage.path!!)

                return when (ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90.toFloat())
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180.toFloat())
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270.toFloat())
                    else -> img
                }
            }
            return null
        } catch (e: Exception) {
            return null
        }
    }

    private fun rotateImage(img: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

}