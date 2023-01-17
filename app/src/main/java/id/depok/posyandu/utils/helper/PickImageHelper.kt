package id.depok.posyandu.utils.helper

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import id.depok.posyandu.utils.helper.ImageGetter
import timber.log.Timber

class PickImageHelper(
    private val fragment: Fragment,
    private val permissionCamera: Int,
    private val permissionGallery: Int,
    private val resultCamera: Int,
    private val resultGallery: Int
) {

    var uri: Uri? = null

    fun getterDialog() {
        val pictureDialog = AlertDialog.Builder(fragment.requireContext())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> takeFromGallery()
                1 -> takeFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun takeFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Timber.e("PERMISSION FILES : ${ImageGetter.isPermissionGalleryDenied(fragment)}")
            if (ImageGetter.isPermissionGalleryDenied(fragment)) {
                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO
                    )
                } else {
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
                fragment.activity?.requestPermissions(
                    permission,
                    permissionGallery
                )
            } else {
                pickFromGallery()
            }
        } else {
            pickFromGallery()
        }
    }

    private fun pickFromGallery() {
        ImageGetter.pickImageFromGallery(fragment, resultGallery)
    }

    fun takeFromCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ImageGetter.isPermissionGalleryDenied(fragment) ||
                fragment.activity?.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            ) {
                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.CAMERA
                    )
                } else {
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    )
                }
                fragment.activity?.requestPermissions(
                    permission,
                    permissionCamera
                )
            } else {
                fromCamera()
            }
        } else {
            fromCamera()
        }
    }

    private fun fromCamera() {
        val contentResolver = fragment.activity?.contentResolver
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        uri = contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

        fragment.startActivityForResult(cameraIntent, resultCamera)
    }


}