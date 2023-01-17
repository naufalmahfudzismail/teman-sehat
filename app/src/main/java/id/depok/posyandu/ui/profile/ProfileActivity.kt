package id.depok.posyandu.ui.profile

import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import id.depok.posyandu.R
import id.depok.posyandu.base.BaseActivity
import id.depok.posyandu.data.models.request.KaderRequest
import id.depok.posyandu.data.models.request.SkillRequest
import id.depok.posyandu.data.models.response.KaderDetailResponse
import id.depok.posyandu.data.preferences.ProfileCache
import id.depok.posyandu.ui.auth.LoginActivity
import id.depok.posyandu.ui.posyandu.adapter.SpinnerAdapter
import id.depok.posyandu.utils.DialogGenerator
import id.depok.posyandu.utils.extension.*
import id.depok.posyandu.utils.helper.AuthHelper
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.dialog_create_skill.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.item_list_handle_state.*
import timber.log.Timber

class ProfileActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    private val kader: KaderDetailResponse? by lazy {
        ProfileCache(this).getSavedProfile()
    }

    companion object {
        val skills = arrayListOf("KIA", "GIZI", "Jentik")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initView()
        bindView()
    }

    private fun initView() {
        titleHeader.text = "Edit Profile"
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnAddSkill.setOnClickListener {
            generateCreateKitDialog { req ->
                viewModel.createSkill(req)
            }
        }

        btnSubmit.setOnClickListener {
            val pass = edtChangePassword.text.toString()
            if (pass.isEmpty()) {
                val req = KaderRequest().apply {
                    this.nik = edtNik.text.toString()
                    this.noKader = edtPhone.text.toString()
                    this.namaKader = edtName.text.toString()
                }
                viewModel.updateKader(kader?.kader?.id.toString(), req)
            } else {
                if (pass.length < 5) {
                    toast("Ganti password minimal 5 karakter")
                } else {
                    val req = KaderRequest().apply {
                        this.nik = edtNik.text.toString()
                        this.noKader = edtPhone.text.toString()
                        this.namaKader = edtName.text.toString()
                        this.password = pass
                    }
                    viewModel.updateKader(kader?.kader?.id.toString(), req)
                }
            }
        }


        btnLogOut.setOnClickListener {
            ProfileCache(this).clearData()
            launchActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            finish()
        }

    }

    private fun bindView() {
        kader?.let {
            viewModel.getKader(it.kader?.id.toString())
        }

        viewModel.kaderState.observe(this) { state ->
            state.data?.let {
                try {
                    ProfileCache(this).saveProfile(it)
                    scrollContent.makeVisible()
                    tvEmptyData.makeGone()
                    it.kader?.let { k ->
                        edtName.setText(k.namaKader)
                        edtNik.setText(k.nik)
                        edtEmail.setText(k.email)
                        edtPhone.setText(k.noKader)
                    }
                    val strBuilder = StringBuilder()

                    if(it.skill.isEmpty()){
                        tvSkills.text ="*Belum ada pelatihan yang di ikuti*"
                        tvSkills.setTypeface(null, Typeface.ITALIC)
                    } else {
                        tvSkills.makeVisible()
                        tvSkills.setTypeface(null, Typeface.NORMAL)
                        it.skill.distinctBy { s -> s.nama }.forEachIndexed { i, s ->
                            strBuilder.append("${i + 1}. ${s.nama}\n")
                        }
                        tvSkills.text = strBuilder
                    }

                } catch (e: Exception) {
                    Timber.e("ERROR $e")
                }
            }

            state.loading.let {
                if (it) {
                    scrollContent.makeGone()
                    progressData.makeVisible()
                    tvEmptyData.makeGone()
                } else progressData.makeGone()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }
        }

        viewModel.kaderUpdateState.observe(this) { state ->
            state.data?.let {
                DialogGenerator.generateConfirmationDialog(
                    this,
                    "Berhasil",
                    "Berhasil update profil!"
                ) {

                }
                kader?.let {
                    viewModel.getKader(it.kader?.id.toString())
                }
            }

            state.loading.let {
                if (it) {
                    showLoading("Mengupdate data profil ...")
                } else hideLoading()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }
        }

        viewModel.skillState.observe(this) { state ->
            state.data?.let {
                DialogGenerator.generateConfirmationDialog(
                    this,
                    "Berhasil",
                    "Berhasil menambahkan pelatihan!"
                ) {

                }
                kader?.let {
                    viewModel.getKader(it.kader?.id.toString())
                }
            }

            state.loading.let {
                if (it) {
                    showLoading("Menambahkan pelatihan...")
                } else hideLoading()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }
        }
    }

    private fun generateCreateKitDialog(
        onDialogClicked: (SkillRequest) -> Unit,
    ) {
        try {
            val dialog = Dialog(this)
            val inflate = View.inflate(this, R.layout.dialog_create_skill, null)
            dialog.window?.attributes?.windowAnimations = R.style.dialog_animation
            dialog.setContentView(inflate)

            dialog.btnCloseDialog.setOnClickListener {
                dialog.dismiss()
            }

            dialog.tvKaderName.text = kader?.kader?.namaKader
            dialog.spinnerSkill.adapter = SpinnerAdapter(this, skills)
            dialog.btnCreate.makeDisable()
            dialog.cbJoinSkill.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    dialog.btnCreate.makeEnable()
                } else {
                    dialog.btnCreate.makeDisable()
                }
            }

            dialog.btnCreate.setOnClickListener {
                val isPass = if (dialog.cbJoinSkill.isChecked) "1" else "0"
                val req = SkillRequest().apply {
                    this.nama = dialog.spinnerSkill.selectedItem.toString()
                    this.status = isPass
                    this.kaderId = kader?.kader?.id.toString()
                }
                dialog.dismiss()
                onDialogClicked(req)
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