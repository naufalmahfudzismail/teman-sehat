package id.depok.posyandu.ui.posyandu.detail.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.depok.posyandu.R
import id.depok.posyandu.data.models.Jadwal
import id.depok.posyandu.utils.extension.inflate
import kotlinx.android.synthetic.main.adapter_posyandu_small.view.*

class ScheduleSmallAdapter(
    val context: Context,
    var data: List<Jadwal>,
    val onItemClick: (Jadwal) -> Unit
) :
    RecyclerView.Adapter<ScheduleSmallAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.adapter_posyandu_small)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindModel(data[position])
    }

    fun updateData(new: List<Jadwal>) {
        data = new
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bindModel(jadwal: Jadwal) {
            view.imgPosyandu.setImageDrawable(
                ContextCompat.getDrawable(
                    view.context,
                    R.drawable.ic_check
                )
            )

            view.tvPosyanduName.text = jadwal.namaKegiatan
            view.tvPosyanduLocation.text = "${jadwal.hari} | ${jadwal.jamOperasional}"
            view.tvPosyanduAddress.text = jadwal.ket

            view.setOnClickListener {
                onItemClick(jadwal)
            }
        }
    }

}