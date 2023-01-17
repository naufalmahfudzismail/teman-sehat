package id.depok.posyandu.ui.posyandu.menu

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import id.depok.posyandu.BuildConfig
import id.depok.posyandu.R
import id.depok.posyandu.base.BaseActivity
import id.depok.posyandu.data.models.Pelayanan
import id.depok.posyandu.data.models.Posyandu
import id.depok.posyandu.data.preferences.ProfileCache
import id.depok.posyandu.ui.posyandu.PosyanduViewModel
import id.depok.posyandu.ui.posyandu.detail.adapter.ServiceSmallAdapter
import id.depok.posyandu.ui.posyandu.menu.adapter.ServiceAdapter
import id.depok.posyandu.utils.DialogGenerator
import id.depok.posyandu.utils.extension.makeGone
import id.depok.posyandu.utils.extension.makeVisible
import id.depok.posyandu.utils.extension.toast
import id.depok.posyandu.utils.helper.AuthHelper
import id.depok.posyandu.utils.helper.ImageGetter
import kotlinx.android.synthetic.main.activity_service.*
import kotlinx.android.synthetic.main.dialog_create_service.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.item_list_handle_state.*
import timber.log.Timber
import java.io.File

class PosyanduServiceActivity : BaseActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this)[PosyanduViewModel::class.java]
    }

    private val adapter by lazy {
        ServiceAdapter(this, arrayListOf()){
            generateCreateServiceDialog(it) { n, f ->
                viewModel.updateService(id = it.id.toString(), name = n, foto = f)
            }
        }
    }

    private val posyandu: Posyandu? by lazy {
        ProfileCache(this).getSavedProfile()?.posyandu
    }

    private var viewImage: ImageView? = null
    private var viewtext: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
        initView()
        bindView()
    }


    private fun initView() {
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        titleHeader.text = "Pelayanan Posyandu"
        subTitleHeader.makeVisible()
        subTitleHeader.text = "Klik Pelayanan untuk mengedit"
        rvServices.layoutManager = LinearLayoutManager(this)
        rvServices.adapter = adapter

        btnAddService.setOnClickListener {
            file = null
            generateCreateServiceDialog { n, f ->
                viewModel.createService(posyandu?.id.toString(), n, f)
            }
        }
    }

    private fun bindView() {
        posyandu?.let {
            viewModel.getService(it.id.toString())
        }
        viewModel.serviceListState.observe(this) { state ->
            state.data?.let {
                if (it.isNotEmpty()) {
                    tvEmptyData.makeGone()
                    adapter.updateData(it)
                } else {
                    tvEmptyData.makeVisible()
                }
            }
            state.loading.let {
                if (it) {
                    progressData.makeVisible()
                    tvEmptyData.makeGone()
                } else progressData.makeGone()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }
        }

        viewModel.serviceState.observe(this) { state ->
            state.data?.let {
                if (it.id != null) {
                    DialogGenerator.generateConfirmationDialog(
                        this,
                        "Berhasil",
                        "Berhasil menambahkan Pelayanan!", cancelAble = true
                    ) {

                    }
                    posyandu?.let { p ->
                        viewModel.getService(p.id.toString())
                    }
                }
            }

            state.loading.let {
                if (it) {
                    showLoading("Membuat Pelayanan...")
                } else hideLoading()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }

        }

        viewModel.updateServiceState.observe(this) { state ->
            state.data?.let {
                if (it.id != null) {
                    DialogGenerator.generateConfirmationDialog(
                        this,
                        "Berhasil",
                        "Berhasil update Pelayanan!", cancelAble = true
                    ) {

                    }
                    posyandu?.let { p ->
                        viewModel.getService(p.id.toString())
                    }
                }
            }

            state.loading.let {
                if (it) {
                    showLoading("Update Pelayanan...")
                } else hideLoading()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }

        }
    }


    private fun generateCreateServiceDialog(
        data: Pelayanan? = null,
        onDialogClicked: (String, file: File?) -> Unit,
    ) {
        try {
            val dialog = Dialog(this)
            val inflate = View.inflate(this, R.layout.dialog_create_service, null)
            dialog.window?.attributes?.windowAnimations = R.style.dialog_animation
            dialog.setContentView(inflate)

            dialog.btnCloseDialog.setOnClickListener {
                dialog.dismiss()
            }

            if (data != null) {
                dialog.tvTitleDialog.text = "Edit data Pelayanan"
                dialog.edtServiceName.setText(data.nama)
                data.foto?.let {
                    Glide.with(this).load(BuildConfig.BASE_IMG_URL + it)
                        .into(dialog.imgAttachedService)
                }
            }

            dialog.btnCreate.setOnClickListener {
                if (dialog.edtServiceName.text.toString().isNotEmpty()) {
                    dialog.dismiss()
                    onDialogClicked(dialog.edtServiceName.text.toString(), file)
                } else {
                    toast("Isi nama pelayanan")
                }
            }

            dialog.rlUploadPhoto.setOnClickListener {
                viewImage = dialog.imgService
                viewtext = dialog.tvUploadImageService
                showImagePickerDialog()
            }

            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            dialog.setCancelable(false)
            dialog.show()
        } catch (e: Exception) {
            AuthHelper.specifyErrorMessage(this, e)
        }
    }

    private fun showImagePickerDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Pilih dari Gallery",
            "Foto dari kamera",
        )
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> galleryPermission()
                1 -> cameraPermission()
            }
        }
        pictureDialog.show()
    }

    private fun galleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ImageGetter.isPermissionGalleryDenied(this)) {
                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO
                    )
                } else {
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
                requestPermissions(permission, PERMISSION_GALLERY)
            } else {
                ImageGetter.pickImageFromGallery(
                    resultImageLauncher
                )
            }
        } else {
            ImageGetter.pickImageFromGallery(
                resultImageLauncher
            )
        }
    }

    private fun cameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                ImageGetter.isPermissionGalleryDenied(this)
            ) {
                val permission =
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, PERMISSION_CAMERA)
            } else {
                uri =
                    ImageGetter.takeImageFromCamera(
                        this,
                        resultImageLauncher
                    )
            }
        } else {
            uri =
                ImageGetter.takeImageFromCamera(this, resultImageLauncher)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImageGetter.takeImageFromCamera(this, resultImageLauncher)
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }

            PERMISSION_GALLERY -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImageGetter.pickImageFromGallery(
                        resultImageLauncher
                    )
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val resultImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                var image: File
                if (uri != null) {
                    uri?.let {
                        viewtext?.makeGone()
                        viewImage?.makeVisible()
                        viewImage?.let { it1 -> Glide.with(this).load(uri).into(it1) }
                        file = File(ImageGetter.getUriRealPathAboveKitkat(this, it))
                        ImageGetter.encodeImage(file!!)
                        //Reset container
                        uri = null
                    }
                } else {
                    data?.let {
                        viewtext?.makeGone()
                        viewImage?.makeVisible()
                        Timber.e("RESULT IMG $data")
                        uri = data.data
                        viewImage?.let { it1 -> Glide.with(this).load(uri).into(it1) }
                        uri?.let { u ->
                            file = File(ImageGetter.getUriRealPathAboveKitkat(this, u))
                            ImageGetter.encodeImage(file!!)
                        }
                    }
                }
            }
        }

    private var file: File? = null
    private var uri: Uri? = null

    companion object {
        private const val PERMISSION_CAMERA = 102
        private const val PERMISSION_GALLERY = 104

    }
}