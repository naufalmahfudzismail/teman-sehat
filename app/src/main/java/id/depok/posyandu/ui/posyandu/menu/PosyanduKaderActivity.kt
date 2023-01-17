package id.depok.posyandu.ui.posyandu.menu

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.depok.posyandu.R
import id.depok.posyandu.base.BaseActivity
import id.depok.posyandu.data.models.Posyandu
import id.depok.posyandu.data.preferences.ProfileCache
import id.depok.posyandu.ui.posyandu.PosyanduViewModel
import id.depok.posyandu.ui.posyandu.menu.adapter.KadersAdapter
import id.depok.posyandu.utils.extension.makeGone
import id.depok.posyandu.utils.extension.makeVisible
import id.depok.posyandu.utils.helper.AuthHelper
import kotlinx.android.synthetic.main.activity_kader.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.item_list_handle_state.*

class PosyanduKaderActivity :  BaseActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this)[PosyanduViewModel::class.java]
    }

    private val adapter by lazy {
        KadersAdapter(this, arrayListOf())
    }

    private val posyandu: Posyandu? by lazy {
        ProfileCache(this).getSavedProfile()?.posyandu
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kader)
        initView()
        bindView()
    }

    private fun initView() {
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        titleHeader.text = "Data Kader"
        rvKaders.layoutManager = LinearLayoutManager(this)
        rvKaders.adapter = adapter
    }

    private fun bindView() {
        posyandu?.let {
            viewModel.getKader(it.id.toString())
        }

        viewModel.kaderListState.observe(this) { state ->
            state.data?.let {
                if(it.isNotEmpty()){
                    tvEmptyData.makeGone()
                    adapter.updateData(it)
                } else {
                    tvEmptyData.makeVisible()
                }
            }

            state.loading.let {
                if(it) {
                    progressData.makeVisible()
                    tvEmptyData.makeGone()
                } else progressData.makeGone()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }

        }
    }
}