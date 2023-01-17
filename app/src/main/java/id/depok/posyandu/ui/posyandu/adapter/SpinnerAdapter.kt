package id.depok.posyandu.ui.posyandu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import id.depok.posyandu.R

class SpinnerAdapter(val context: Context, var data: List<String>) : BaseAdapter() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder

        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_spinner, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

        vh.label.text = data[position]

        return view
    }

    fun updateData(customerList: ArrayList<String>) {
        this.data = customerList
        notifyDataSetChanged()
    }

    class ItemRowHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.tvOptionSpinnerItem) as TextView
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return data.size
    }
}