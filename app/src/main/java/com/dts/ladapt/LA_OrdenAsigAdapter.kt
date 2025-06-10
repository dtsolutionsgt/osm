package com.dts.ladapt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses.clsOrdenSupAsig
import com.dts.osm.R

class LA_OrdenAsigAdapter(val itemList: ArrayList<clsOrdenSupAsig>) : RecyclerView.Adapter<LA_OrdenAsigAdapter.ViewHolder>() {

    var selectedItemPosition: Int = -1
    lateinit var lay: LinearLayout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LA_OrdenAsigAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lv_orden_asig_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LA_OrdenAsigAdapter.ViewHolder, position: Int) {
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

        fun bindItems(mitem: clsOrdenSupAsig) {

            val lblnum = itemView.findViewById(R.id.textViewUsername5) as TextView
            val lbltarea = itemView.findViewById(R.id.textViewUsername) as TextView
            val lblcli = itemView.findViewById(R.id.textViewUsername2) as TextView
            val lblfecha = itemView.findViewById(R.id.textViewUsername3) as TextView
            lay = itemView.findViewById(R.id.relitem) as LinearLayout

            lblnum.text = mitem.NUMERO+"  "
            lblcli.text = mitem.CLIENTE
            lbltarea.text = mitem.TIPO
            lblfecha.text = mitem.SFECHA

        }

        fun bind(mitem: clsOrdenSupAsig, isSelected: Boolean) {
            lay.setBackgroundResource(if (isSelected)
                R.drawable.frame_round_sel else R.drawable.frame_round)
        }

        override fun onClick(p0: View?) {}

    }

}
