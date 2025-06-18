package com.dts.ladapt

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.osm.R

class LA_ordendet(val itemList: ArrayList<clsClasses.clsOrdendet>) : RecyclerView.Adapter<LA_ordendet.ViewHolder>() {

    var selectedItemPosition: Int = -1
    lateinit var lay: RelativeLayout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LA_ordendet.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lv_ordendet, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LA_ordendet.ViewHolder, position: Int) {
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

        fun bindItems(mitem: clsClasses.clsOrdendet) {
            try {
                val lbl1 = itemView.findViewById(R.id.lblV1) as TextView
                val lbl2 = itemView.findViewById(R.id.lblV2) as TextView
                val img1 = itemView.findViewById(R.id.imageView19) as ImageView
                lay = itemView.findViewById(R.id.relitem) as RelativeLayout

                lbl1.text = mitem.descripcion
                lbl2.text =getStatName(mitem)
                lbl2.setBackgroundResource(getStatRes(mitem))
                img1.isVisible=mitem.idnoaten>0

            } catch (e: Exception) { }

        }

        fun bind(mitem: clsClasses.clsOrdendet, isSelected: Boolean) {
            if (isSelected) {
                lay.setBackgroundColor(Color.parseColor("#9CD0F4"))
            } else {
                lay.setBackgroundResource(R.drawable.frame_round)
            }
        }

        override fun onClick(p0: View?) {}

        private fun getStatName(item: clsClasses.clsOrdendet) : String {
            var ename="Pendiente"

            if (item.idnoaten>0) return "ANULADO"

            if (item?.activo==1) {
                if (item?.realizado==1) {
                    ename="Completo"
                } else {
                    ename="En progreso"
                }
            }

            return ename
        }

        private fun getStatRes(item: clsClasses.clsOrdendet) : Int {
            var eres=R.drawable.color_gray_grad

            if (item.idnoaten>0) return R.drawable.blank

            if (item?.activo==1) {
                if (item?.realizado==1) {
                    eres=R.drawable.color_green_grad
                } else {
                    eres=R.drawable.color_ocra_grad
                }
            }

            return eres
        }
    }

}
