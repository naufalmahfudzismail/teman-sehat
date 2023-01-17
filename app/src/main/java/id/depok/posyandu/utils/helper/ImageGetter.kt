package id.depok.posyandu.utils.helper

import android.Manifest
import android.Manifest.permission.*
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.util.Base64OutputStream
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.obsez.android.lib.filechooser.ChooserDialog
import id.depok.posyandu.utils.extension.toast
import id.depok.posyandu.utils.helper.FileUtils.*
import timber.log.Timber
import java.io.*


object ImageGetter {

    fun isPermissionGalleryDenied(fragment: Fragment): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.M) {
            if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                 (fragment.activity?.checkSelfPermission(
                    READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_DENIED) || (fragment.activity?.checkSelfPermission(
                    READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_DENIED)
            } else {
                fragment.activity?.checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            }
        } else {
            return false
        }
    }

    fun isPermissionGalleryDenied(activity: Activity): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.M) {
            if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) { (activity.checkSelfPermission(
                    READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_DENIED) || (activity.checkSelfPermission(
                    READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_DENIED)
            } else {
                activity.checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            }
        } else {
            return false
        }
    }

    fun pickImageFromGallery(fragment: Fragment, requestCode: Int) {
        val intent = if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent(MediaStore.ACTION_PICK_IMAGES)
        } else {
            Intent(Intent.ACTION_PICK)
        }
        intent.type = "image/*"
        fragment.startActivityForResult(intent, requestCode)
    }

    fun pickMultipleImageFromGallery(fragment: Fragment, requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.action = Intent.ACTION_GET_CONTENT;
        fragment.startActivityForResult(intent, requestCode)
    }


    fun pickMultipleImageFromGallery(activity: Activity, requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.putExtra("multi-pick", true)
        activity.startActivityForResult(intent, requestCode)
    }


    fun pickMultipleImageFromFolder(activity: Activity, requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.putExtra("multi-pick", true)
        activity.startActivityForResult(intent, requestCode)
    }

    fun pickImageFromGallery(fragment: Activity, requestCode: Int) {
        val intent = if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent(MediaStore.ACTION_PICK_IMAGES)
        } else {
            Intent(Intent.ACTION_PICK)
        }
        intent.type = "image/*"
        fragment.startActivityForResult(intent, requestCode)
    }

    fun pickImageFromGallery(launcher: ActivityResultLauncher<Intent>) {
        val intent = if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent(MediaStore.ACTION_PICK_IMAGES)
        } else {
            Intent(Intent.ACTION_PICK)
        }
        intent.type = "image/*"
        launcher.launch(intent)
    }

    fun takeImageFromCamera(fragment: Fragment, requestCode: Int): Uri? {
        val contentResolver = fragment.requireActivity().contentResolver
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        fragment.startActivityForResult(cameraIntent, requestCode)
        return uri
    }

    fun takeImageFromCamera(fragment: Activity, requestCode: Int): Uri? {
        val contentResolver = fragment.contentResolver
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        fragment.startActivityForResult(cameraIntent, requestCode)
        return uri
    }

    fun takeImageFromCamera(fragment: Activity, launcher: ActivityResultLauncher<Intent>): Uri? {
        val contentResolver = fragment.contentResolver
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        launcher.launch(cameraIntent)
        return uri
    }

    fun takeMultipleImageFromCamera(activity: Activity, requestCode: Int): Uri? {
        val contentResolver = activity.contentResolver
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        cameraIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        activity.startActivityForResult(cameraIntent, requestCode)
        return uri
    }

    fun getImageFromFolder(context: Activity, requestCode: Int) {
        // Implicitly allow the user to select a particular kind of data
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        // The MIME data type filter
        intent.type = "*/*"
        // Only return URIs that can be opened with ContentResolver
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        context.startActivityForResult(intent, requestCode)
    }

    fun showFileChooserDialog(context: Context, onItemSelected: (file: File) -> Unit) {
        val initialFolder = File(Environment.getExternalStorageDirectory(), "Download")
        ChooserDialog(context).withStartFile(initialFolder.path).withFilter(false, false, "jpg")
            .withChosenListener { _, file ->
                onItemSelected(file)
            }.withOnCancelListener { dialog ->
                dialog.cancel()
            }.displayPath(false).build().show()
    }

    fun encodeBitmap(bitmap: Bitmap): String {

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byte = byteArrayOutputStream.toByteArray()
        val encImage = Base64.encodeToString(byte, Base64.DEFAULT)

        return encImage
    }

    fun encodeImage(imageFile: File): String {

        var fileInputStream: FileInputStream? = null
        fileInputStream = FileInputStream(imageFile)

        val bm = BitmapFactory.decodeStream(fileInputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byte = byteArrayOutputStream.toByteArray()
        val encImage = Base64.encodeToString(byte, Base64.DEFAULT)

        return encImage
    }

    fun encodeFile(file: File): String {

        var inputStream: InputStream? = null//You can get an inputStream using any IO API
        inputStream = FileInputStream(file.absolutePath)
        val buffer = ByteArray(8192)
        var bytesRead: Int? = null
        val output = ByteArrayOutputStream()
        val output64 = Base64OutputStream(output, Base64.DEFAULT)
        try {

            while (run {
                    bytesRead = inputStream.read(buffer)
                    bytesRead
                } != -1) {
                output64.write(buffer, 0, bytesRead!!)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        output64.close()
        return output.toString()
    }

    fun getUriRealPathAboveKitkat(ctx: Context, uri: Uri): String {
        try {
            var ret = ""

            if (isContentUri(uri)) {
                ret = if (isGooglePhotoDoc(uri.authority!!)) {
                    uri.lastPathSegment!!
                } else {
                    if (isDocumentUri(ctx, uri)) {

                        // Get uri related document id.
                        val documentId = DocumentsContract.getDocumentId(uri)

                        // Get uri authority.
                        val uriAuthority = uri.authority

                        if (isMediaDoc(uriAuthority)) {
                            val idArr = documentId.split(":")
                            if (idArr.size == 2) {
                                // First item is document type.
                                val docType = idArr[0]

                                // Second item is document real id.
                                val realDocId = idArr[1]

                                // Get content uri by document type.
                                var mediaContentUri =
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                when (docType) {
                                    "image" -> mediaContentUri =
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    "video" -> mediaContentUri =
                                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                                    "audio" -> mediaContentUri =
                                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                                }

                                // Get where clause with real document id.

                                // Get where clause with real document id.
                                val whereClause =
                                    MediaStore.Images.Media._ID + " = " + realDocId

                                ret = getImageRealPath(
                                    ctx.contentResolver,
                                    mediaContentUri,
                                    whereClause
                                )
                            }

                        } else if (isDownloadDoc(uriAuthority!!)) {
                            // Build download uri.
                            val downloadUri = Uri.parse("content://downloads/public_downloads")
                            val str = downloadUri.toString()

                            // Append download document id at uri end.
                            val downloadUriAppendId =
                                ContentUris.withAppendedId(downloadUri, documentId.toLong())

                            ret =
                                getImageRealPath(ctx.contentResolver, downloadUriAppendId, null)

                        } else if (isExternalStoreDoc(uriAuthority)) {
                            val idArr = documentId.split(":")
                            if (idArr.size == 2) {
                                val type = idArr[0]
                                val realDocId = idArr[1]

                                if ("primary" == type.lowercase()) {
                                    ret =
                                        "" + Environment.getExternalStorageDirectory() + "/" + realDocId
                                }

                                if ("80ff-1d07" == type.lowercase()) {
                                    ret =
                                        "" + Environment.getExternalStorageDirectory() + "/" + realDocId
                                }
                            }
                        }
                    }
                    getImageRealPath(ctx.contentResolver, uri, null)
                }
            } else if (isFileUri(uri)) {
                ret = uri.path!!
            } else if (isDocumentUri(ctx, uri)) {

                // Get uri related document id.
                val documentId = DocumentsContract.getDocumentId(uri)

                // Get uri authority.
                val uriAuthority = uri.getAuthority()

                if (isMediaDoc(uriAuthority)) {
                    val idArr = documentId.split(":")
                    if (idArr.size == 2) {
                        // First item is document type.
                        val docType = idArr[0]

                        // Second item is document real id.
                        val realDocId = idArr[1]

                        // Get content uri by document type.
                        var mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        when {
                            "image".equals(docType) -> mediaContentUri =
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            "video".equals(docType) -> mediaContentUri =
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            "audio".equals(docType) -> mediaContentUri =
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }

                        // Get where clause with real document id.

                        // Get where clause with real document id.
                        val whereClause = MediaStore.Images.Media._ID + " = " + realDocId

                        ret =
                            getImageRealPath(ctx.contentResolver, mediaContentUri, whereClause)
                    }

                } else if (isDownloadDoc(uriAuthority!!)) {
                    // Build download uri.
                    val downloadUri = Uri.parse("content://downloads/public_downloads")
                    val str = downloadUri.toString()

                    // Append download document id at uri end.
                    val downloadUriAppendId =
                        ContentUris.withAppendedId(downloadUri, documentId.toLong())

                    ret = getImageRealPath(ctx.contentResolver, downloadUriAppendId, null)

                } else if (isExternalStoreDoc(uriAuthority)) {
                    val idArr = documentId.split(":")
                    if (idArr.size == 2) {
                        val type = idArr[0]
                        val realDocId = idArr[1]

                        if ("primary" == type.lowercase()) {
                            ret =
                                "" + Environment.getExternalStorageDirectory() + "/" + realDocId
                        }
                    }
                }
            }
            return ret
        } catch (e: Exception) {
            Timber.e(e)
            ctx.toast("Gagal untuk memilih file")
            return ""
        }
    }

    fun getPath(context: Context, uri: Uri): String? {

        /* if (DEBUG)
             Timber.d(
                 "Authority: " + uri.authority +
                         ", Fragment: " + uri.fragment +
                         ", Port: " + uri.port +
                         ", Query: " + uri.query +
                         ", Scheme: " + uri.scheme +
                         ", Host: " + uri.host +
                         ", Segments: " + uri.pathSegments.toString()
             )*/


        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // LocalStorageProvider
            if (isLocalStorageDocument(uri)) {
                // The path is the id
                return DocumentsContract.getDocumentId(uri)
            } else if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split =
                    docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return (Environment.getExternalStorageDirectory()).toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split =
                    docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                when (type) {
                    "image" -> {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
            // ExternalStorageProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )

        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    /* Check whether current android os version is bigger than kitkat or not. */
    fun isAboveKitKat(): Boolean {
        var ret = false
        ret = SDK_INT >= Build.VERSION_CODES.KITKAT
        return ret
    }

    /* Check whether this uri represent a document or not. */
    fun isDocumentUri(ctx: Context, uri: Uri): Boolean {
        var ret = false
        if (ctx != null && uri != null) {
            ret = DocumentsContract.isDocumentUri(ctx, uri)
        }
        return ret
    }

    /* Check whether this uri is a content uri or not.
    *  content uri like content://media/external/images/media/1302716
    *  */
    fun isContentUri(uri: Uri): Boolean {
        var ret = false
        if (uri != null) {
            val uriSchema = uri.scheme
            if ("content" == uriSchema?.lowercase()) {
                ret = true
            }
        }
        return ret
    }

    /* Check whether this uri is a file uri or not.
    *  file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
    * */
    fun isFileUri(uri: Uri): Boolean {
        var ret = false
        if (uri != null) {
            val uriSchema = uri.scheme
            if ("file" == uriSchema!!.lowercase()) {
                ret = true
            }
        }
        return ret
    }


    /* Check whether this document is provided by ExternalStorageProvider. */
    fun isExternalStoreDoc(uriAuthority: String): Boolean {
        var ret = false
        if ("com.android.externalstorage.documents".equals(uriAuthority)) {
            ret = true
        }

        return ret
    }

    /* Check whether this document is provided by DownloadsProvider. */
    fun isDownloadDoc(uriAuthority: String): Boolean {
        var ret = false

        if ("com.android.providers.downloads.documents".equals(uriAuthority)) {
            ret = true
        }

        return ret
    }

    /* Check whether this document is provided by MediaProvider. */
    fun isMediaDoc(uriAuthority: String?): Boolean {
        var ret = false
        if ("com.android.providers.media.documents".equals(uriAuthority)) {
            ret = true
        }

        return ret
    }

    /* Check whether this document is provided by google photos. */
    private fun isGooglePhotoDoc(uriAuthority: String): Boolean {
        var ret = false
        if ("com.google.android.apps.photos.content" == uriAuthority) {
            ret = true
        }
        return ret
    }

    /* Return uri represented document file real local path.*/
    fun getImageRealPath(contentResolver: ContentResolver, uri: Uri, whereClause: String?): String {
        var ret = ""
        // Query the uri with condition.
        val cursor = contentResolver.query(uri, null, whereClause, null, null)

        if (cursor != null) {
            val moveToFirst = cursor.moveToFirst()
            if (moveToFirst) {

                // Get columns name by uri type.
                var columnName = MediaStore.Images.Media.DATA

                when (uri) {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI -> columnName =
                        MediaStore.Images.Media.DATA
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI -> columnName =
                        MediaStore.Audio.Media.DATA
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI -> columnName =
                        MediaStore.Video.Media.DATA
                }

                // Get column index.

                // Get column value which is the uri related file local path.

                // Get column index.
                val imageColumnIndex = cursor.getColumnIndex(columnName)

                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex)
            }
        }
        cursor?.close()
        return ret
    }

    fun showDialogPickImage(context: Activity) {
        val pictureDialog = AlertDialog.Builder(context)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems =
            arrayOf(
                "Select photo from gallery",
                "Capture photo from camera",
                "Select photo from Folder"
            )
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> permissionFromGallery(context)
                1 -> permissionFromCamera(context)
                2 -> permissionFromFolder(context)
            }
        }
        pictureDialog.show()
    }

    const val DEFAULT_GALLERY_REQUEST_CODE = 1001
    const val DEFAULT_CAMERA_REQUEST_CODE = 1002
    const val DEFAULT_FOLDER_REQUEST_CODE = 1003
    const val DEFAULT_GALLERY_PERMISSION_CODE = 1004
    const val DEFAULT_CAMERA_PERMISSION_CODE = 1005
    const val DEFAULT_FOLDER_PERMISSION_CODE = 1006

    private var uriCamera: Uri? = null

    private fun permissionFromGallery(context: Activity) {
        if (SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(
                        READ_EXTERNAL_STORAGE,
                        READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO
                    )
                } else {
                    arrayOf(READ_EXTERNAL_STORAGE)
                }
                context.requestPermissions(
                    permission,
                    DEFAULT_GALLERY_PERMISSION_CODE
                )
            } else {
                pickImageFromGallery(context, DEFAULT_GALLERY_REQUEST_CODE)
            }
        } else {
            pickImageFromGallery(context, DEFAULT_GALLERY_REQUEST_CODE)
        }
    }

    private fun permissionFromCamera(context: Activity) {
        if (SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                isPermissionGalleryDenied(context)
            ) {
                val permission =
                    arrayOf(Manifest.permission.CAMERA, WRITE_EXTERNAL_STORAGE)
                context.requestPermissions(permission, DEFAULT_CAMERA_PERMISSION_CODE)
            } else {
                uriCamera = takeImageFromCamera(context, DEFAULT_CAMERA_REQUEST_CODE)
            }
        } else {
            uriCamera = takeImageFromCamera(context, DEFAULT_CAMERA_REQUEST_CODE)
        }
    }

    private fun permissionFromFolder(context: Activity) {
        if (SDK_INT >= Build.VERSION_CODES.M) {
            if (isPermissionGalleryDenied(context)) {
                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(
                        READ_EXTERNAL_STORAGE,
                        READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO
                    )
                } else {
                    arrayOf(READ_EXTERNAL_STORAGE)
                }
                context.requestPermissions(
                    permission,
                    DEFAULT_FOLDER_PERMISSION_CODE
                )
            } else {
                getImageFromFolder(context, DEFAULT_FOLDER_REQUEST_CODE)
            }
        } else {
            getImageFromFolder(context, DEFAULT_FOLDER_REQUEST_CODE)
        }
    }

    fun checkPermissionTakeImage(
        context: Activity,
        requestCode: Int,
        grantResults: IntArray,
        actionDenied: () -> Unit,
    ) {
        when (requestCode) {
            DEFAULT_GALLERY_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery(context, DEFAULT_GALLERY_REQUEST_CODE)
                } else {
                    actionDenied()
                }
            }

            DEFAULT_CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeImageFromCamera(context, DEFAULT_CAMERA_REQUEST_CODE)
                } else {
                    actionDenied()
                }
            }

            DEFAULT_FOLDER_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImageFromFolder(context, DEFAULT_FOLDER_REQUEST_CODE)
                } else {
                    actionDenied()
                }
            }
        }
    }

    fun onResultPickImage(requestCode: Int, resultCode: Int, data: Intent?): Uri? {
        return when (requestCode) {
            DEFAULT_GALLERY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) data?.data
                else null
            }
            DEFAULT_CAMERA_REQUEST_CODE -> {
                uriCamera
            }
            DEFAULT_FOLDER_PERMISSION_CODE -> {
                if (resultCode == RESULT_OK) data?.data
                else null
            }
            else -> {
                null
            }
        }
    }


    fun getTypeOfFile(uri: Uri): String? {
        val path = uri.toString()

        return when {
            path.endsWith(".pdf") -> "pdf"
            path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png") -> "image"
            else -> null
        }
    }
}