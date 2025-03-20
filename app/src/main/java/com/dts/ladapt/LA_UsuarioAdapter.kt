package com.dts.ladapt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dts.osm.R
import com.dts.base.clsClasses

class LA_UsuarioAdapter(val itemList: ArrayList<clsClasses.clsUsuario>) : RecyclerView.Adapter<LA_UsuarioAdapter.ViewHolder>() {

    var selectedItemPosition: Int = -1
    lateinit var lay: LinearLayout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LA_UsuarioAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lv_usuarioitem, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LA_UsuarioAdapter.ViewHolder, position: Int) {
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

        fun bindItems(mitem: clsClasses.clsUsuario) {
            val textViewName = itemView.findViewById(R.id.textViewExistencia) as TextView
            lay = itemView.findViewById(R.id.relitem) as LinearLayout
            textViewName.text = mitem.nombre
        }

        fun bind(mitem: clsClasses.clsUsuario, isSelected: Boolean) {
            lay.setBackgroundResource(if (isSelected)
                R.drawable.frame_round_flatb_sel else R.drawable.frame_round_flatb)
        }

        override fun onClick(p0: View?) {}

    }

}
