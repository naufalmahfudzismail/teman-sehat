package id.depok.posyandu.ui.posyandu

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import id.depok.posyandu.R
import id.depok.posyandu.base.BaseActivity
import id.depok.posyandu.data.preferences.ProfileCache
import id.depok.posyandu.ui.home.adapter.MainMenu
import id.depok.posyandu.ui.home.adapter.MenuAdapter
import id.depok.posyandu.ui.posyandu.detail.PosyanduActivity
import id.depok.posyandu.ui.posyandu.menu.PosyanduKaderActivity
import id.depok.posyandu.ui.posyandu.menu.PosyanduKitActivity
import id.depok.posyandu.ui.posyandu.menu.PosyanduServiceActivity
import id.depok.posyandu.ui.posyandu.menu.PosyanduScheduleActivity
import id.depok.posyandu.utils.extension.launchActivity
import kotlinx.android.synthetic.main.activity_posyandu_detail.*
import kotlinx.android.synthetic.main.header.*

class PosyanduMenuActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posyandu_detail)

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        ProfileCache(this).getSavedProfile()?.let {
            it.posyandu?.let { p ->
                titleHeader.text = p.name
            }
        }

        val menus: MutableList<MainMenu> = mutableListOf()
        menus.add(MainMenu("Profile Posyandu", R.mipmap.icon_posyandu))
        menus.add(MainMenu("Data Dasar Posyandu", R.mipmap.icon_posyandu_detail))
        menus.add(MainMenu("Jadwal Posyandu", R.mipmap.icon_posyandu_schedule))
        menus.add(MainMenu("Data Kit Posyandu", R.mipmap.icon_posyandu_kit))
        menus.add(MainMenu("Data Kader Posyandu", R.mipmap.icon_posyandu_kader))
        menus.add(MainMenu("Pelayanan Posyandu", R.mipmap.icon_posyandu_service))

        rvPosyanduMenu.layoutManager = GridLayoutManager(this, 2)
        rvPosyanduMenu.adapter = MenuAdapter(menus, this) {
            when (it) {
                0 -> launchActivity<PosyanduActivity> { putExtra("isEdit", false) }
                1 -> launchActivity<PosyanduActivity> { putExtra("isEdit", true) }
                2 -> launchActivity<PosyanduScheduleActivity> {}
                3 -> launchActivity<PosyanduKitActivity> { }
                4 -> launchActivity<PosyanduKaderActivity> { }
                5 -> launchActivity<PosyanduServiceActivity> { }
                else -> throw Exception()
            }
        }
    }
}