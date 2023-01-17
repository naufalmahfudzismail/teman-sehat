package id.depok.posyandu.ui.posyandu.detail

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import id.depok.posyandu.BuildConfig
import id.depok.posyandu.R
import id.depok.posyandu.base.BaseActivity
import id.depok.posyandu.data.models.request.PosyanduRequest
import id.depok.posyandu.data.preferences.ProfileCache
import id.depok.posyandu.ui.posyandu.PosyanduViewModel
import id.depok.posyandu.ui.posyandu.adapter.SpinnerAdapter
import id.depok.posyandu.ui.posyandu.detail.adapter.KaderSmallAdapter
import id.depok.posyandu.ui.posyandu.detail.adapter.KitSmallAdapter
import id.depok.posyandu.ui.posyandu.detail.adapter.ScheduleSmallAdapter
import id.depok.posyandu.ui.posyandu.detail.adapter.ServiceSmallAdapter
import id.depok.posyandu.utils.DialogGenerator
import id.depok.posyandu.utils.extension.makeGone
import id.depok.posyandu.utils.extension.makeVisible
import id.depok.posyandu.utils.helper.AuthHelper
import id.depok.posyandu.utils.helper.ImageGetter
import kotlinx.android.synthetic.main.activity_posyandu.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.item_list_handle_state.*
import kotlinx.android.synthetic.main.layout_edit_posyandu_data.*
import kotlinx.android.synthetic.main.layout_edit_posyandu_data.imgEditPosyandu
import kotlinx.android.synthetic.main.layout_posyandu_data.*
import timber.log.Timber
import java.io.File

class PosyanduActivity : BaseActivity() {


    private val isEdit by lazy {
        intent.getBooleanExtra("isEdit", false)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[PosyanduViewModel::class.java]
    }

    private val modulId: String? by lazy {
        intent.getStringExtra("modulId")
    }

    private val data by lazy {
        ProfileCache(this).getSavedProfile()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posyandu)

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (isEdit) {
            titleHeader.text = "Edit Data Dasar Posyandu"
        } else {
            titleHeader.text = "Profile Posyandu"
        }

        rlUploadFoto.setOnClickListener {
            showImagePickerDialog()
        }

        btnEdit.setOnClickListener {
            val req = PosyanduRequest().apply {
                this.nama = edtPosyanduName.text.toString()
                this.alamat = edtAdress.text.toString()
                this.tlp = edtPhone.text.toString()
                this.ketua = edtLeader.text.toString()
                this.skl = spinnerSkl.selectedItem.toString()
                this.sk = edtLinkSkl.text.toString()
            }
            viewModel.updatePosyandu(data?.posyandu?.id.toString(), req, file)
        }

        spinnerSkl.adapter = SpinnerAdapter(this, sklData)

        initView()

    }

    private fun initView() {
        ProfileCache(this).getSavedProfile()?.let {
            if (modulId != null) {
                viewModel.getPosyandu(modulId.toString())
            } else {
                it.posyandu?.let { p ->
                    viewModel.getPosyandu(p.id.toString())
                }
            }
        }

        viewModel.posyanduState.observe(this) { state ->
            state.data?.let { response ->
                tvEmptyData.makeGone()
                if (isEdit) {
                    layoutEdit.makeVisible()
                    layoutData.makeGone()
                    response.posyandu?.let {
                        edtPosyanduName.setText(it.name)
                        edtPhone.setText(it.tlp)
                        edtAdress.setText(it.address)
                        edtLeader.setText(it.leader)
                    }
                    response.posyandu?.let {
                        Glide.with(this).load(BuildConfig.BASE_IMG_URL + it.foto.toString())
                            .into(imgEditPosyandu)
                        imgEditPosyandu.setOnClickListener { v ->
                            if (uri != null) {
                                uri?.let { u ->
                                    DialogGenerator.generatePreviewImageDialog(
                                        context = this,
                                        uriImg = u
                                    )
                                }
                            } else {
                                DialogGenerator.generatePreviewImageDialog(
                                    context = this,
                                    urlImg = it.foto.toString()
                                )
                            }

                        }
                    }

                } else {
                    layoutData.makeVisible()
                    layoutEdit.makeGone()
                    response.posyandu?.let {
                        tvPosyanduName.setText(it.name)
                        tvSkl.setText(it.skl)
                        tvStrata.setText(it.strata)
                        tvPhoneNumber.setText(it.tlp)
                        tvAddress.setText(it.address)
                        tvLeader.setText(it.leader)
                        Glide.with(this).load(BuildConfig.BASE_IMG_URL + it.foto.toString())
                            .into(imgPosyandu)
                        imgPosyandu.setOnClickListener { v ->
                            DialogGenerator.generatePreviewImageDialog(this, it.foto.toString())
                        }
                        it.koordinat?.let { k ->
                            btnLocation.setOnClickListener {
                                startActivity(Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse(k)
                                })
                            }
                        }
                    }
                    response.jadwal.let {
                        if (it.isEmpty()) {
                            tvEmptySchedule.makeVisible()
                        } else {
                            tvEmptySchedule.makeGone()
                            rvSchedule.layoutManager = LinearLayoutManager(this)
                            rvSchedule.adapter = ScheduleSmallAdapter(this, it) {

                            }
                        }
                    }
                    response.kit.let {
                        if (it.isEmpty()) {
                            tvEmptykit.makeVisible()
                        } else {
                            tvEmptykit.makeGone()
                            rvKit.layoutManager = LinearLayoutManager(this)
                            rvKit.adapter = KitSmallAdapter(this, it) {

                            }
                        }
                    }
                    response.pelayanan.let {
                        if (it.isEmpty()) {
                            tvEmptyService.makeVisible()
                        } else {
                            tvEmptyService.makeGone()
                            rvService.layoutManager = LinearLayoutManager(this)
                            rvService.adapter = ServiceSmallAdapter(this, it) { p ->
                                DialogGenerator.generatePreviewImageDialog(this, p.foto.toString())
                            }
                        }
                    }
                    response.kader.let {
                        if (it.isEmpty()) {
                            tvEmptyKader.makeVisible()
                        } else {
                            tvEmptyKader.makeGone()
                            rvKader.layoutManager = LinearLayoutManager(this)
                            rvKader.adapter = KaderSmallAdapter(this, it)
                        }
                    }
                }
            }

            state.loading.let {
                if (it) {
                    progressData.makeVisible()
                    layoutEdit.makeGone()
                    layoutData.makeGone()
                    tvEmptyData.makeGone()
                } else progressData.makeGone()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }

        }

        viewModel.posyanduUpdateState.observe(this) { state ->
            state.data?.let {
                if (it.id != null) {
                    data?.let { d ->
                        d.posyandu = it
                        ProfileCache(this).saveProfile(d)
                        viewModel.getPosyandu(it.id.toString())
                    }
                    DialogGenerator.generateConfirmationDialog(
                        this,
                        "Berhasil",
                        "Data Posyandu berhasil di update!"
                    ) {

                    }
                }
            }

            state.loading.let {
                if (it) {
                    showLoading("Updating Posyandu....")
                } else hideLoading()
            }
            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }

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
                        tvUploadBank.makeGone()
                        imgEditPosyandu.makeVisible()
                        Glide.with(this).load(uri).into(imgEditPosyandu)
                        file = File(ImageGetter.getUriRealPathAboveKitkat(this, it))
                        ImageGetter.encodeImage(file!!)

                        //Reset container
                        uri = null
                    }
                } else {
                    data?.let {
                        tvUploadBank.makeGone()
                        imgEditPosyandu.makeVisible()
                        Timber.e("RESULT IMG $data")
                        uri = data.data
                        Glide.with(this).load(uri).into(imgEditPosyandu)
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
        val sklData = arrayListOf("Milik Sendiri", "Menumpang", "Menyewa")
        private const val PERMISSION_CAMERA = 102
        private const val PERMISSION_GALLERY = 104

    }

}