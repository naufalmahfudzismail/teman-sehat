package id.depok.posyandu.ui.posyandu.menu

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.depok.posyandu.R
import id.depok.posyandu.base.BaseActivity
import id.depok.posyandu.data.models.Jadwal
import id.depok.posyandu.data.models.Posyandu
import id.depok.posyandu.data.models.request.JadwalRequest
import id.depok.posyandu.data.models.request.UpdateJadwalRequest
import id.depok.posyandu.data.preferences.ProfileCache
import id.depok.posyandu.ui.posyandu.PosyanduViewModel
import id.depok.posyandu.ui.posyandu.adapter.SpinnerAdapter
import id.depok.posyandu.ui.posyandu.detail.adapter.ScheduleSmallAdapter
import id.depok.posyandu.ui.posyandu.menu.adapter.ScheduleAdapter
import id.depok.posyandu.utils.DialogGenerator
import id.depok.posyandu.utils.extension.getData
import id.depok.posyandu.utils.extension.makeGone
import id.depok.posyandu.utils.extension.makeVisible
import id.depok.posyandu.utils.extension.toast
import id.depok.posyandu.utils.helper.AuthHelper
import kotlinx.android.synthetic.main.activity_schedule.*
import kotlinx.android.synthetic.main.dialog_create_schedule.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.item_list_handle_state.*

class PosyanduScheduleActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[PosyanduViewModel::class.java]
    }

    private val adapter by lazy {
        ScheduleAdapter(this, arrayListOf()) {
            generateCreateScheduleDialog(it) { req ->
                viewModel.updateSchedule(it.id.toString(), UpdateJadwalRequest().apply {
                    this.hari = req.hari
                    this.namaKegiatan = req.namaKegiatan
                    this.ket = req.ket
                    this.jamOperasional = req.jamOperasional
                })
            }
        }
    }

    private val posyandu: Posyandu? by lazy {
        ProfileCache(this).getSavedProfile()?.posyandu
    }

    companion object {
        val days = arrayListOf(
            "Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu", "Minggu"
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        titleHeader.text = "Jadwal Posyandu"
        subTitleHeader.makeVisible()
        subTitleHeader.text = "Klik Jadwal untuk mengedit"


        rvSchedule.layoutManager = LinearLayoutManager(this)
        rvSchedule.adapter = adapter
        btnAddSchedule.setOnClickListener {
            generateCreateScheduleDialog { req ->
                viewModel.createSchedule(req)
            }
        }
        bindView()
    }

    private fun bindView() {
        posyandu?.let {
            viewModel.getSchedule(it.id.toString())
        }

        viewModel.scheduleListState.observe(this) { state ->
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

        viewModel.scheduleState.observe(this) { state ->
            state.data?.let {
                if (it.id != null) {
                    DialogGenerator.generateConfirmationDialog(
                        this,
                        "Berhasil",
                        "Berhasil menambahkan Jadwal!", cancelAble = true
                    ) {

                    }
                    posyandu?.let { p ->
                        viewModel.getSchedule(p.id.toString())
                    }
                }
            }
            state.loading.let {
                if (it) {
                    showLoading("Menambahkan Jadwal...")
                } else hideLoading()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }
        }


        viewModel.updateScheduleState.observe(this) { state ->
            state.data?.let {
                if (it.id != null) {
                    DialogGenerator.generateConfirmationDialog(
                        this,
                        "Berhasil",
                        "Berhasil update Jadwal!", cancelAble = true
                    ) {

                    }
                    posyandu?.let { p ->
                        viewModel.getSchedule(p.id.toString())
                    }
                }
            }
            state.loading.let {
                if (it) {
                    showLoading("Updating Jadwal...")
                } else hideLoading()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }
        }
    }

    private fun generateCreateScheduleDialog(
        jadwal: Jadwal? = null,
        onDialogClicked: (JadwalRequest) -> Unit,
    ) {
        try {
            val dialog = Dialog(this)
            val inflate = View.inflate(this, R.layout.dialog_create_schedule, null)
            dialog.window?.attributes?.windowAnimations = R.style.dialog_animation
            dialog.setContentView(inflate)

            dialog.btnCloseDialog.setOnClickListener {
                dialog.dismiss()
            }

            dialog.spinnerDay.adapter = SpinnerAdapter(this, days)

            jadwal?.let {
                dialog.tvTitleDialog.text = "Edit Jadwal"
                dialog.edtScheduleName.setText(it.namaKegiatan)
                dialog.edtScheduleRemark.setText(it.ket)
                dialog.edtHour.setText(it.jamOperasional)
                dialog.spinnerDay.setSelection(dialog.spinnerDay.getData().indexOf(it.hari))
            }


            dialog.btnCreate.setOnClickListener {
                val name = dialog.edtScheduleName.text.toString()
                val ket = dialog.edtScheduleRemark.text.toString()
                val pickedTime = dialog.edtHour.text.toString()
                if (name.isEmpty() || pickedTime.isEmpty() || pickedTime.isEmpty()) {
                    toast("Isi semua isian!")
                } else {
                    dialog.dismiss()
                    val req = JadwalRequest().apply {
                        this.hari = dialog.spinnerDay.selectedItem.toString()
                        this.jamOperasional = pickedTime
                        this.modulId = posyandu?.id.toString()
                        this.namaKegiatan = name
                        this.ket = ket
                    }
                    onDialogClicked(req)
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