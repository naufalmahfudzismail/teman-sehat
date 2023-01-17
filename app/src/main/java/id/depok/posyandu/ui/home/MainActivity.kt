package id.depok.posyandu.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import id.depok.posyandu.BuildConfig
import id.depok.posyandu.R
import id.depok.posyandu.base.BaseActivity
import id.depok.posyandu.data.preferences.ProfileCache
import id.depok.posyandu.ui.jentik.JentikFormActivity
import id.depok.posyandu.ui.home.adapter.MainMenu
import id.depok.posyandu.ui.home.adapter.MenuAdapter
import id.depok.posyandu.ui.posyandu.PosyanduMenuActivity
import id.depok.posyandu.ui.posyandu.PosyanduListActivity
import id.depok.posyandu.ui.profile.ProfileActivity
import id.depok.posyandu.utils.DialogGenerator
import id.depok.posyandu.utils.extension.launchActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            val window = this.window
            window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.statusBarColor =
                this.let { ContextCompat.getColor(it, R.color.colorAccentRed) }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                val decor = window.decorView
                decor.systemUiVisibility =
                    decor.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window?.statusBarColor =
                    this.let { ContextCompat.getColor(it, R.color.colorAccentRed) }

            }
        }
        initView()
    }

    private fun initView() {

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                DialogGenerator.generateYesNoConfirmationDialog(
                    this@MainActivity,
                    "Konfimarsi",
                    "Anda yakin ingin keluar aplikasi?",
                    cancelAble = true
                ) {
                    if (it) {
                        finish()
                    }
                }
            }

        })

        val menus: MutableList<MainMenu> = mutableListOf()
        menus.add(MainMenu("Posyandu", R.mipmap.icon_posyandu))
        menus.add(MainMenu("Semua Posyandu", R.mipmap.icon_posyandu_list))
        menus.add(MainMenu("Pemantauan Jentik", R.mipmap.icon_jentik))
        menus.add(MainMenu("Profile", R.mipmap.icon_posyandu_kader))

        rvMainMenu.layoutManager = GridLayoutManager(this, 2)
        rvMainMenu.adapter = MenuAdapter(menus, this) {
            when (it) {
                0 -> launchActivity<PosyanduMenuActivity> { }
                1 -> launchActivity<PosyanduListActivity> { }
                2 -> launchActivity<JentikFormActivity> {}
                3 -> launchActivity<ProfileActivity> { }
                else -> throw Exception()
            }
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        ProfileCache(this).getSavedProfile()?.let { kader ->
            tvAppVersion.text = "Version : ${BuildConfig.VERSION_NAME}"
            tvWelcomingNameUser.text = kader.kader?.namaKader
            tvWelcomingUserDistrict.text =
                "${kader.posyandu?.name}"
            tvUserAddress.text =
                "${kader.posyandu?.address}, ${kader.posyandu?.district}, ${kader.posyandu?.subDistrict}"
        }
    }
}