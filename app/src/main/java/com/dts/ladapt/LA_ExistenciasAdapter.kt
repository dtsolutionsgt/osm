package com.dts.ladapt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dts.osm.R
import com.dts.base.clsClasses

class LA_ExistenciasAdapter(val itemList: ArrayList<clsClasses.clsExistencia>) :
    RecyclerView.Adapter<LA_ExistenciasAdapter.ViewHolder>() {

    var selectedItemPosition: Int = -1
    lateinit var lay: LinearLayout

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LA_ExistenciasAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.lv_existencias, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LA_ExistenciasAdapter.ViewHolder, position: Int) {
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

        fun bindItems(mitem: clsClasses.clsExistencia) {
            val textViewDesc = itemView.findViewById(R.id.textViewExistencia) as TextView
            val textViewCantidad = itemView.findViewById<TextView>(R.id.textViewCantidad)
            lay = itemView.findViewById(R.id.relitem) as LinearLayout
            textViewDesc.text = mitem.nombre
            textViewCantidad.text = "${mitem.cant}"

        }

        fun bind(mitem: clsClasses.clsExistencia, isSelected: Boolean) {
            lay.setBackgroundResource(if (isSelected) R.drawable.frame_round_flatb_sel else R.drawable.frame_round_flatb)
        }


        override fun onClick(p0: View?) {}

    }

}
