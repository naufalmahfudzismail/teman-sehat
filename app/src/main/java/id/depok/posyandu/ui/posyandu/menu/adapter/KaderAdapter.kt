package id.depok.posyandu.ui.posyandu.menu.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.depok.posyandu.R
import id.depok.posyandu.data.models.Kader
import id.depok.posyandu.ui.posyandu.detail.adapter.KaderSmallAdapter
import id.depok.posyandu.utils.extension.inflate
import id.depok.posyandu.utils.extension.makeGone
import kotlinx.android.synthetic.main.adapter_posyandu.view.*

class KaderAdapter(val context: Context, val data: List<Kader>) :
    RecyclerView.Adapter<KaderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.adapter_posyandu)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindModel(data[position])
    }

    inner class ViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bindModel(kader: Kader) {
            view.imgPosyandu.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_account_profile))
            view.tvPosyanduName.text = kader.namaKader
            view.tvPosyanduLocation.text = if (kader.skills.isEmpty()) {
                "Tidak terlatih"
            } else {
                kader.skills.joinToString("\n") { s -> "${s.nama} (${if (s.status == "1") "Terlatih" else "Tidak terlatih"})" }
            }
            view.tvPosyanduAddress.makeGone()
            view.tvPosyanduCreated.makeGone()
        }
    }

}