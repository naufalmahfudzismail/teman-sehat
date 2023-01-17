package id.depok.posyandu.ui.jentik

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import id.depok.posyandu.R
import id.depok.posyandu.base.BaseActivity
import id.depok.posyandu.data.models.request.PjbRequest
import id.depok.posyandu.data.preferences.ProfileCache
import id.depok.posyandu.utils.DialogGenerator
import id.depok.posyandu.utils.extension.makeGone
import id.depok.posyandu.utils.extension.makeVisible
import id.depok.posyandu.utils.extension.toast
import id.depok.posyandu.utils.helper.AuthHelper
import kotlinx.android.synthetic.main.activity_batik.*
import kotlinx.android.synthetic.main.header.*

class JentikFormActivity : BaseActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this)[JentikViewModel::class.java]
    }

    private var kk = ""
    private var kkFound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batik)
        initView()
        bindView()
    }

    private fun initView() {
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        titleHeader.text = "Kampung Berbatik"

        edtNik.addTextChangedListener {
            kk = it.toString()
            if (kk.length == 16) {
                viewModel.getKeluarga(kk)
            }
        }

        btnSubmit.setOnClickListener {
            if(kk.length != 16){
                toast("Nomor KK haurs 16 digit")
            } else {
                if (!kkFound) {
                    val req = PjbRequest().apply {
                        this.noKK = kk
                        this.kaderId =
                            ProfileCache(this@JentikFormActivity).getSavedProfile()?.kader?.id.toString()
                        this.statusJentikId = if (rbYes.isChecked) "1" else "2"
                    }
                    viewModel.createPJb(req)
                } else {
                    toast("Data PJB keluarga sudah terdaftar!, silahkan Input keluarga lain")
                }
            }

        }
    }

    private fun bindView() {
        viewModel.keluargaState.observe(this) { state ->
            state.data?.let {
                kkFound = true
                tvFindKK.makeVisible()
                tvFindKK.text = "Data Kartu keluarga sudah terdaftar!"
            }
            state.loading.let {
                if (it) {
                    loadingKk.makeVisible()
                } else loadingKk.makeGone()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }
        }

        viewModel.pjbState.observe(this) { state ->
            state.data?.let {
                DialogGenerator.generateConfirmationDialog(
                    this,
                    "Berhasil",
                    "berhasil mendata PJB!",
                    "OK",
                    false
                ) {
                    finish()
                }
            }
            state.loading.let {
                if (it) {
                    showLoading("Mendata PJB...")
                } else hideLoading()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }
        }
    }
}