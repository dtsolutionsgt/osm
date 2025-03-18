package com.dts.ladapt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dts.osm.R
import com.dts.base.clsClasses

class LA_ProductoAdapter(val itemList: ArrayList<clsClasses.clsProducto>) :
    RecyclerView.Adapter<LA_ProductoAdapter.ViewHolder>() {

    var selectedItemPosition: Int = -1
    lateinit var lay: LinearLayout

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LA_ProductoAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.lvproductoitem, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LA_ProductoAdapter.ViewHolder, position: Int) {
        val item = itemList[position]
        val isSelected = position == selectedItemPosition

        holder.bindItems(itemList[position])

        holder.bind(item, isSelected)

        holder.itemView.setOnClickListener {
            val previousSelectedPosition = selectedItemPosition
            selectedItemPosition = position

            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(position)
        }
    }

    fun setSelectedItem(selpos: Int) {
        val previousSelectedPosition = selectedItemPosition
        selectedItemPosition = selpos

        notifyItemChanged(previousSelectedPosition)
        notifyItemChanged(selpos)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        fun bindItems(mitem: clsClasses.clsProducto) {
            val textViewDesc = itemView.findViewById(R.id.textViewProducto) as TextView
            lay = itemView.findViewById(R.id.relitem) as LinearLayout
            textViewDesc.text = mitem.desclarga

        }

        fun bind(mitem: clsClasses.clsProducto, isSelected: Boolean) {
            lay.setBackgroundResource(R.drawable.frame_round)
        }

        override fun onClick(p0: View?) {}

    }

}
