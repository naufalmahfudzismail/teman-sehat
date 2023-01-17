package id.depok.posyandu.ui.posyandu.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.depok.posyandu.BuildConfig
import id.depok.posyandu.R
import id.depok.posyandu.data.models.Posyandu
import id.depok.posyandu.utils.DateUtil
import id.depok.posyandu.utils.extension.inflate
import kotlinx.android.synthetic.main.adapter_posyandu.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PosyanduAdapter(val context: Context, val onItemClick: (Posyandu: Posyandu) -> Unit) :
    RecyclerView.Adapter<PosyanduAdapter.NewsViewHolder>() {

    private var data: List<Posyandu> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = parent.inflate(R.layout.adapter_posyandu)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bindModel(data[position])
    }

    fun updateData(newData: List<Posyandu>) {
        this.data = newData
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bindModel(posyandu: Posyandu) {
            val serverFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val appFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
            /* val defaultFormat = serverFormat.parse(Posyandu.postingDate)*/

            Glide.with(context).load(BuildConfig.BASE_IMG_URL + posyandu.foto)
                .into(view.imgPosyandu)
            view.tvPosyanduName.text = posyandu.name
            view.tvPosyanduLocation.text = "${posyandu.district}. ${posyandu.subDistrict}"
            view.tvPosyanduAddress.text = posyandu.address
            view.tvPosyanduCreated.text = DateUtil.formatDateFromServer(posyandu.creation)
            view.setOnClickListener {
                onItemClick(posyandu)
            }
        }
    }

}