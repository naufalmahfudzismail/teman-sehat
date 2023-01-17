package id.depok.posyandu.ui.posyandu.menu.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.depok.posyandu.R
import id.depok.posyandu.data.models.Kader
import id.depok.posyandu.utils.extension.inflate
import id.depok.posyandu.utils.extension.makeGone
import kotlinx.android.synthetic.main.adapter_posyandu.view.*

class KadersAdapter(val context: Context, var data: List<Kader>) :
    RecyclerView.Adapter<KadersAdapter.ViewHolder>() {

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

    fun updateData(new: List<Kader>) {
        data = new
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bindModel(kader: Kader) {
            view.imgPosyandu.let {
                Glide.with(view.context).load(R.drawable.ic_account_profile).into(it)
            }
            view.tvPosyanduName.text = kader.namaKader
            view.tvPosyanduAddress.text = if (kader.skills.isEmpty()) {
                "Belum Terlatih"
            } else {
                val skill =
                    kader.skills.distinctBy { s -> s.nama }.map { s -> s.nama }.joinToString(", ")
                "Terlatih : $skill"
            }
            view.tvPosyanduLocation.text = "WA : ${kader.noKader}"
            view.tvPosyanduCreated.makeGone()
        }
    }

}