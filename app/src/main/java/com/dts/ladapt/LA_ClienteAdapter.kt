package com.dts.ladapt

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dts.osm.R
import com.dts.base.clsClasses

class LA_ClienteAdapter(val itemList: ArrayList<clsClasses.clsCliente>) : RecyclerView.Adapter<LA_ClienteAdapter.ViewHolder>() {

    var selectedItemPosition: Int = -1
    lateinit var lay: LinearLayout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LA_ClienteAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lv_clienteitem, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LA_ClienteAdapter.ViewHolder, position: Int) {
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

    fun setSelectedItem(selpos:Int) {
        val previousSelectedPosition = selectedItemPosition
        selectedItemPosition = selpos

        notifyItemChanged(previousSelectedPosition)
        notifyItemChanged(selpos)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bindItems(mitem: clsClasses.clsCliente) {
            val textViewName = itemView.findViewById(R.id.textViewUsername) as TextView
            lay = itemView.findViewById(R.id.relitem) as LinearLayout
            textViewName.text = mitem.nombre+" ["+mitem.nit+"]"
        }

        fun bind(mitem: clsClasses.clsCliente, isSelected: Boolean) {
            if (isSelected) {
                lay.setBackgroundColor(Color.parseColor("#9CD0F4"))
            } else {
                lay.setBackgroundResource(R.drawable.frame_round)
            }
        }

        override fun onClick(p0: View?) {}

    }

}
