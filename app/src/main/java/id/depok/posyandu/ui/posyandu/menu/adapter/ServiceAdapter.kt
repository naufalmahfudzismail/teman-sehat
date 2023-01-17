package id.depok.posyandu.ui.posyandu.menu.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.depok.posyandu.BuildConfig
import id.depok.posyandu.R
import id.depok.posyandu.data.models.Pelayanan
import id.depok.posyandu.ui.posyandu.detail.adapter.ServiceSmallAdapter
import id.depok.posyandu.utils.extension.inflate
import kotlinx.android.synthetic.main.adapter_service_posyandu.view.*

class ServiceAdapter(val context: Context, var data: List<Pelayanan>, val onItemClick : (Pelayanan) -> Unit) :
    RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.adapter_service_posyandu)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindModel(data[position])
    }

    fun updateData(new: List<Pelayanan>) {
        data = new
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bindModel(pelayanan: Pelayanan) {
            view.imgPosyandu.let {
                if(pelayanan.foto.toString().isEmpty()){
                    it.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_img))
                } else {
                    Glide.with(it).load(BuildConfig.BASE_IMG_URL + pelayanan.foto.toString()).into(it)
                }
            }
            view.tvPosyanduName.text = pelayanan.nama
            view.setOnClickListener {
                onItemClick(pelayanan)
            }
        }
    }

}