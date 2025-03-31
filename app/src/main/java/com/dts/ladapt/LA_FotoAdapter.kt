package com.dts.ladapt

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.osm.R
import java.io.File


class LA_FotoAdapter(val itemList: ArrayList<clsClasses.clsOrdenfoto>, val picturedir:String) : RecyclerView.Adapter<LA_FotoAdapter.ViewHolder>() {

    var selectedItemPosition: Int = -1
    lateinit var lay: LinearLayout

    var picdir=picturedir

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LA_FotoAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lv_fotoitem, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LA_FotoAdapter.ViewHolder, position: Int) {
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

        fun bindItems(mitem: clsClasses.clsOrdenfoto) {
            val textViewName = itemView.findViewById(R.id.textViewUsername) as TextView
            val img1 = itemView.findViewById(R.id.imageView7) as ImageView
            lay = itemView.findViewById(R.id.relitem) as LinearLayout

            textViewName.text = mitem.nota

            try {
                var fbm= File(picdir,mitem.nombre)
                if (fbm.exists()) {
                    val fbmp = BitmapFactory.decodeFile(fbm.absolutePath)
                    img1?.setImageBitmap(fbmp)
                }
            } catch (e: Exception) {}

        }

        fun bind(mitem: clsClasses.clsOrdenfoto, isSelected: Boolean) {
            lay.setBackgroundResource(if (isSelected)
                R.drawable.frame_btn_sel else R.drawable.frame_btn)
        }

        override fun onClick(p0: View?) {}

    }

}
