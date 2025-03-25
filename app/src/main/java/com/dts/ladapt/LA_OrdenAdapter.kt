package com.dts.ladapt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses.clsOrdenlist
import com.dts.osm.R

class LA_OrdenAdapter(val itemList: ArrayList<clsOrdenlist>) : RecyclerView.Adapter<LA_OrdenAdapter.ViewHolder>() {

    var selectedItemPosition: Int = -1
    lateinit var lay: LinearLayout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LA_OrdenAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lv_ordenitem, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LA_OrdenAdapter.ViewHolder, position: Int) {
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

        fun bindItems(mitem: clsOrdenlist) {
            val lbltarea = itemView.findViewById(R.id.textViewUsername) as TextView
            val lblcli = itemView.findViewById(R.id.textViewUsername2) as TextView
            val lblfecha = itemView.findViewById(R.id.textViewUsername3) as TextView
            val lblest = itemView.findViewById(R.id.textViewUsername4) as TextView
            lay = itemView.findViewById(R.id.relitem) as LinearLayout

            lbltarea.text = mitem.tarea
            lblcli.text = mitem.cliente
            lblfecha.text = mitem.fecha
            lblest.text = mitem.estado

            var eres=R.drawable.color_gray_grad
            if (mitem.idestado==4) {
                eres=R.drawable.color_ocra_grad
            } else if (mitem.idestado==5) {
                eres=R.drawable.color_green_grad
            }
            lblest.setBackgroundResource(eres)
        }

        fun bind(mitem: clsOrdenlist, isSelected: Boolean) {
            lay.setBackgroundResource(if (isSelected)
                R.drawable.frame_round_sel else R.drawable.frame_round)
        }

        override fun onClick(p0: View?) {}

    }

}
