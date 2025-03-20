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

class LA_MaterialAdapter(val itemList: ArrayList<clsClasses.clsMaterial>) :
    RecyclerView.Adapter<LA_MaterialAdapter.ViewHolder>() {

    var selectedItemPosition: Int = -1
    lateinit var lay: RelativeLayout

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LA_MaterialAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.lvmaterialitem, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LA_MaterialAdapter.ViewHolder, position: Int) {
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

        fun bindItems(mitem: clsClasses.clsMaterial) {
            val textViewDesc = itemView.findViewById(R.id.textViewProducto) as TextView
            val textViewcant = itemView.findViewById(R.id.textView16) as TextView
            lay = itemView.findViewById(R.id.relitem) as RelativeLayout

            textViewDesc.text = mitem.desclarga
            textViewcant.text = ""+mitem.cant
        }

        fun bind(mitem: clsClasses.clsMaterial, isSelected: Boolean) {
            lay.setBackgroundResource(if (isSelected) R.drawable.frame_round_flatb_sel else R.drawable.frame_round_flatb)
        }


        override fun onClick(p0: View?) {}

    }

}
