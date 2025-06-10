package com.dts.ladapt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dts.osm.R
import com.dts.base.clsClasses

class LA_Tiposervicio(val itemList: ArrayList<clsClasses.clsTiposervicio>) :
        RecyclerView.Adapter<LA_Tiposervicio.ViewHolder>() {

        var selectedItemPosition: Int = -1
        lateinit var lay: RelativeLayout

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LA_Tiposervicio.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.lv_tiposervicio, parent, false)
            return ViewHolder(v)
        }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LA_Tiposervicio.ViewHolder, position: Int) {
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

        fun bindItems(mitem: clsClasses.clsTiposervicio) {
            val textViewDesc = itemView.findViewById(R.id.lblV1) as TextView
            lay = itemView.findViewById(R.id.relitem) as RelativeLayout
            textViewDesc.text = mitem.nombre
        }

        fun bind(mitem: clsClasses.clsTiposervicio, isSelected: Boolean) {
            lay.setBackgroundResource(if (isSelected)
               R.drawable.frame_round_sel else R.drawable.frame_round)
        }

        override fun onClick(p0: View?) {}
    }

}