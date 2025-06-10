package com.dts.ladapt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses.clsOrdenSupHora
import com.dts.osm.R

class LA_SuperFechaAdapter(val itemList: ArrayList<clsOrdenSupHora>) : RecyclerView.Adapter<LA_SuperFechaAdapter.ViewHolder>() {

    var selectedItemPosition: Int = -1
    lateinit var lay: LinearLayout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LA_SuperFechaAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lv_super_asigna_fecha, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LA_SuperFechaAdapter.ViewHolder, position: Int) {
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

        fun bindItems(mitem: clsOrdenSupHora) {
            try {

                val lbluser = itemView.findViewById(R.id.textViewUsername6) as TextView
                val lblnum = itemView.findViewById(R.id.textViewUsername5) as TextView
                val lbltarea = itemView.findViewById(R.id.textViewUsername) as TextView
                val lblcli = itemView.findViewById(R.id.textViewUsername2) as TextView
                val lblini = itemView.findViewById(R.id.textViewUsername3) as TextView
                val lblfin = itemView.findViewById(R.id.textViewUsername7) as TextView
                lay = itemView.findViewById(R.id.relitem) as LinearLayout

                lbluser.text = mitem.NOMBRE
                lblnum.text = mitem.NUMERO+"  "
                lbltarea.text = mitem.TIPO
                lblcli.text = mitem.CLIENTE
                lblini.text = mitem.SFECHAINI
                lblfin.text = mitem.SFECHAFIN

            } catch (e: Exception) {
                var ss=e.message
                ss=ss+""
            }
        }

        fun bind(mitem: clsOrdenSupHora, isSelected: Boolean) {
            lay.setBackgroundResource(if (isSelected)
                R.drawable.frame_round_sel else R.drawable.frame_round)
        }

        override fun onClick(p0: View?) {}

    }

}
