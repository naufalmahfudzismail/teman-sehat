package id.depok.posyandu.ui.posyandu.menu

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.depok.posyandu.R
import id.depok.posyandu.base.BaseActivity
import id.depok.posyandu.data.models.Kit
import id.depok.posyandu.data.models.request.KitRequest
import id.depok.posyandu.data.models.request.UpdateKitRequest
import id.depok.posyandu.data.preferences.ProfileCache
import id.depok.posyandu.ui.posyandu.PosyanduViewModel
import id.depok.posyandu.ui.posyandu.adapter.SpinnerAdapter
import id.depok.posyandu.ui.posyandu.detail.adapter.KitSmallAdapter
import id.depok.posyandu.ui.posyandu.menu.adapter.KitAdapter
import id.depok.posyandu.utils.DialogGenerator
import id.depok.posyandu.utils.extension.getData
import id.depok.posyandu.utils.extension.makeDisable
import id.depok.posyandu.utils.extension.makeGone
import id.depok.posyandu.utils.extension.makeVisible
import id.depok.posyandu.utils.helper.AuthHelper
import kotlinx.android.synthetic.main.activity_kit.*
import kotlinx.android.synthetic.main.dialog_create_kit.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.item_list_handle_state.*

class PosyanduKitActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[PosyanduViewModel::class.java]
    }

    private val adapter by lazy {
        KitAdapter(this, arrayListOf()) {
            generateCreateKitDialog(it) { kitId, jumlah ->
                viewModel.updateKit(kitId, UpdateKitRequest().apply {
                    this.jumlahStok = jumlah
                })
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kit)
        initView()
        bindView()
    }

    private fun initView() {
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        titleHeader.text = "Kit Posyandu"
        subTitleHeader.makeVisible()
        subTitleHeader.text = "Klik Kit untuk mengedit"

        rvKit.layoutManager = LinearLayoutManager(this)
        rvKit.adapter = adapter

        btnAddKit.setOnClickListener {
            generateCreateKitDialog { kitId, jumlah ->
                viewModel.createKit(KitRequest().apply {
                    this.modulId =
                        ProfileCache(this@PosyanduKitActivity).getSavedProfile()?.posyandu?.id.toString()
                    this.jumlahStok = jumlah
                    this.kitPosyanduId = kitId
                })
            }
        }
    }

    private fun bindView() {

        ProfileCache(this).getSavedProfile()?.posyandu?.let {
            viewModel.getKitRef()
            viewModel.getKit(it.id.toString())
        }

        viewModel.kitRefListState.observe(this) {
            it.data?.let { data ->
                if (data.isNotEmpty()) {
                    btnAddKit.makeVisible()
                } else {
                    btnAddKit.makeGone()
                }
            }
        }

        viewModel.kitListState.observe(this) { state ->
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

        viewModel.kitState.observe(this) { state ->
            state.data?.let {
                if (it.id != null) {
                    DialogGenerator.generateConfirmationDialog(
                        this,
                        "Berhasil",
                        "Berhasil menambahkan Kit!", cancelAble = true
                    ) {

                    }
                    ProfileCache(this).getSavedProfile()?.posyandu?.let { p ->
                        viewModel.getKit(p.id.toString())
                    }
                }
            }
            state.loading.let {
                if (it) {
                    showLoading("Menambahkan Kit")
                } else hideLoading()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }
        }

        viewModel.updateKitState.observe(this) { state ->
            state.data?.let {
                if (it.id != null) {
                    DialogGenerator.generateConfirmationDialog(
                        this,
                        "Berhasil",
                        "Berhasil update Kit!", cancelAble = true
                    ) {

                    }
                    ProfileCache(this).getSavedProfile()?.posyandu?.let { p ->
                        viewModel.getKit(p.id.toString())
                    }
                }
            }
            state.loading.let {
                if (it) {
                    showLoading("Updating Kit...")
                } else hideLoading()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }
        }

    }

    private fun generateCreateKitDialog(
        data: Kit? = null,
        onDialogClicked: (String, String) -> Unit,
    ) {
        try {
            val dialog = Dialog(this)
            val inflate = View.inflate(this, R.layout.dialog_create_kit, null)
            dialog.window?.attributes?.windowAnimations = R.style.dialog_animation
            dialog.setContentView(inflate)


            dialog.btnCloseDialog.setOnClickListener {
                dialog.dismiss()
            }

            viewModel.kitRefListState.value?.data?.let { kit ->
                dialog.spinnerKit.adapter = SpinnerAdapter(this, kit.map { k -> k.nama.toString() })
                if (data != null) {
                    dialog.tvTitleDialog.text = "Edit data Kit"
                    dialog.spinnerKit.setSelection(
                        dialog.spinnerKit.getData().indexOf(data.namaKit)
                    )
                    dialog.spinnerKit.makeDisable()
                    dialog.edtStockNumber.setText(data.jumlahStok)
                    dialog.btnCreate.setOnClickListener {
                        dialog.dismiss()
                        onDialogClicked(
                            data.id.toString(),
                            dialog.edtStockNumber.text.toString()
                        )
                    }
                } else {
                    dialog.tvTitleDialog.text = "Tambah kit Baru"
                    dialog.btnCreate.setOnClickListener {
                        dialog.dismiss()
                        onDialogClicked(
                            kit[dialog.spinnerKit.selectedItemPosition].id.toString(),
                            dialog.edtStockNumber.text.toString()
                        )
                    }
                }

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
}