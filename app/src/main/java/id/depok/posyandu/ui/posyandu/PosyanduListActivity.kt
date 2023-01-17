package id.depok.posyandu.ui.posyandu

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.depok.posyandu.R
import id.depok.posyandu.base.BaseActivity
import id.depok.posyandu.ui.posyandu.adapter.PosyanduAdapter
import id.depok.posyandu.ui.posyandu.adapter.SpinnerAdapter
import id.depok.posyandu.ui.posyandu.detail.PosyanduActivity
import id.depok.posyandu.utils.extension.launchActivity
import id.depok.posyandu.utils.extension.makeGone
import id.depok.posyandu.utils.extension.makeVisible
import id.depok.posyandu.utils.helper.AuthHelper
import kotlinx.android.synthetic.main.activity_posyandu_list.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.item_list_handle_state.*

class PosyanduListActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[PosyanduViewModel::class.java]
    }

    private val adapter by lazy {
        PosyanduAdapter(this) {
            launchActivity<PosyanduActivity> {
                putExtra("modulId", it.id.toString())
            }
        }
    }

    private val districtAdapter by lazy {
        SpinnerAdapter(this, arrayListOf())
    }

    private val subDistrictAdapter by lazy {
        SpinnerAdapter(this, arrayListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posyandu_list)
        initView()
        bindView()
    }

    private fun initView() {

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        titleHeader.text = "Data Posyandu"
        rvPosyandu.layoutManager = LinearLayoutManager(this)
        rvPosyandu.adapter = adapter

        spinnerSubDistrict.adapter = subDistrictAdapter
        spinnerDistrict.adapter = districtAdapter
        spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                adapter.updateData(arrayListOf())
                val kec = viewModel.kecamatanListState.value?.data
                kec?.let {
                    val id = kec[spinnerDistrict.selectedItemPosition].id.toString()
                    viewModel.kelurahanListState.value?.data?.let { kel ->
                        val data = kel.filter { k -> k.kecamatanId == id }
                        subDistrictAdapter.updateData(data.map { k -> k.nama } as ArrayList<String>)
                        spinnerSubDistrict.setSelection(0)
                        viewModel.getListPosyandu(
                            kecamatan = spinnerDistrict.selectedItem.toString(),
                            kelurahan = spinnerSubDistrict.selectedItem.toString()
                        )
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        spinnerSubDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                adapter.updateData(arrayListOf())
                viewModel.getListPosyandu(
                    kecamatan = spinnerDistrict.selectedItem.toString(),
                    kelurahan = spinnerSubDistrict.selectedItem.toString()
                )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun bindView() {
        viewModel.getKecamatan()


        viewModel.kecamatanListState.observe(this) { state ->
            state.data?.let {
                districtAdapter.updateData(it.map { k -> k.nama } as ArrayList<String>)

                viewModel.getKelurahan()
            }
            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }

            state.loading.let {
                if (it) showLoading("loading...")

            }
        }

        viewModel.kelurahanListState.observe(this) { state ->
            state.data?.let {
                subDistrictAdapter.updateData(it.map { k -> k.nama } as ArrayList<String>)
                val kec = viewModel.kecamatanListState.value?.data
                kec?.let {
                    val id = kec[spinnerDistrict.selectedItemPosition].id.toString()
                    viewModel.kelurahanListState.value?.data?.let { kel ->
                        val data = kel.filter { k -> k.kecamatanId == id }
                        subDistrictAdapter.updateData(data.map { k -> k.nama } as ArrayList<String>)
                        spinnerSubDistrict.setSelection(0)
                        viewModel.getListPosyandu(
                            kecamatan = spinnerDistrict.selectedItem.toString(),
                            kelurahan = spinnerSubDistrict.selectedItem.toString()
                        )
                    }
                }
            }
            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }
            state.loading.let {
                if (!it) hideLoading()
            }
        }

        viewModel.posyanduListState.observe(this) { state ->
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

    }
}