package id.depok.posyandu.ui.posyandu.detail.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.depok.posyandu.R
import id.depok.posyandu.data.models.Kit
import id.depok.posyandu.utils.extension.inflate
import kotlinx.android.synthetic.main.adapter_kit_posyandu_small.view.*

class KitSmallAdapter(val context: Context, var data: List<Kit>, val onItemClick : (Kit) -> Unit) :
    RecyclerView.Adapter<KitSmallAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.adapter_kit_posyandu_small)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindModel(data[position])
    }

    fun updateData(new : List<Kit>){
        data = new
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        fun bindModel(kit: Kit) {
            view.tvPosyanduName.text = kit.namaKit
            view.tvPosyanduLocation.text = "Stok : ${kit.jumlahStok}"
            view.setOnClickListener {
                onItemClick(kit)
            }
        }
    }

}