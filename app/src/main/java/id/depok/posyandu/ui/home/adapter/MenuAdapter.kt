package id.depok.posyandu.ui.home.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.depok.posyandu.R
import id.depok.posyandu.utils.extension.inflate
import kotlinx.android.synthetic.main.adapter_dashboard_menu.view.*

class MenuAdapter(
    var data: List<MainMenu>,
    private val context: Context,
    private val itemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = parent.inflate(R.layout.adapter_dashboard_menu, false)
        return MenuViewHolder(view, context, itemClick)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bindData(data[position], position)
    }

    inner class MenuViewHolder(
        itemView: View,
        private val context: Context,
        private val itemClick: (position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {

        fun bindData(mainMenu: MainMenu, position: Int) {
            itemView.tvMenuTitle.text = mainMenu.menuTitle
            Glide.with(context).load(mainMenu.menuIcon).override(128)
                .into(itemView.ivMenuIcon as ImageView)

            itemView.frameMenu.setOnClickListener {
                itemClick(position)
            }
        }
    }

}