package com.dts.classes

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.classes.RecyclerItemClickListener
import com.dts.osm.R


class extChkListDlg(Lang:Int) {

    var selidx=-1
    var text="";var stext=""
    var lang=Lang

    private var mList: RecyclerView? = null

    private var mTitleLabel: TextView? = null
    private var mBtnLeft: TextView? = null
    private var mBtnMid: TextView? = null
    private var mBtnRight: TextView? = null

    private var mRel: RelativeLayout? = null
    private var mRelTop: RelativeLayout? = null
    private var mRelBot: RelativeLayout? = null
    private var mbuttons: LinearLayout? = null

    private var adapter: Adapter? = null

    private var dialog: Dialog? = null
    private var cont: Context? = null

    private var items = ArrayList<clsListDialogItem>()
    public var data = ArrayList<clsListDialogItem>()

    private var buttonCount = 0
    private var bwidth = 420
    private val bheight = 550
    private val itHeight =48+1
    private var mwidth = 0
    private var mheight = 0
    private var mlines = 6
    private var mminlines = 1
    private var mcloseafterclick = true

    var clickListener: Runnable? = null

    //region Public methods

    fun buildDialogbase( activity: Activity, titletext: String, butleft: String, butmid: String,butright: String ) {
        dialog = Dialog(activity)
        cont = dialog!!.context

        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.extchklistdlg)

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        mRel = dialog!!.findViewById(R.id.extlistdialogrel)
        mRelTop = dialog!!.findViewById(R.id.xdlgreltop)
        mRelBot = dialog!!.findViewById(R.id.xdlgrelbut)
        mbuttons = dialog!!.findViewById(R.id.linbuttons)

        mList = dialog!!.findViewById(R.id.recview1) as RecyclerView
        mList?.layoutManager = LinearLayoutManager(cont, LinearLayoutManager.VERTICAL,false)

        mList?.addOnItemTouchListener(
            RecyclerItemClickListener(
            cont!!, mList!!,
            object : RecyclerItemClickListener.OnItemClickListener {

                override fun onItemClick(view: View, position: Int) {

                    selidx=position

                    if (data[position].checked) {
                        data[position].checked = false
                    } else {
                        data[position].checked = true
                    }

                    adapter?.notifyDataSetChanged()
                    runClickListener()
                }

                override fun onItemLongClick(view: View?, position: Int) {
                    //toast("long click")
                }
            })
        )


        mTitleLabel = dialog!!.findViewById(R.id.lbltitulo);mTitleLabel?.setText(titletext)

        mBtnLeft = dialog!!.findViewById(R.id.btnexit);mBtnLeft?.setText(butleft);
        mBtnLeft?.setOnClickListener(View.OnClickListener { });

        mBtnMid = dialog!!.findViewById(R.id.btndel);mBtnMid?.setText(butmid);
        mBtnMid?.setOnClickListener(View.OnClickListener { })

        mBtnRight = dialog!!.findViewById(R.id.btnadd);mBtnRight?.setText(butright)
        mBtnRight?.setOnClickListener(View.OnClickListener { })

        when (buttonCount) {
            1 -> {
                mBtnMid?.setVisibility(View.GONE)
                mBtnRight?.setVisibility(View.GONE)
                mbuttons?.setWeightSum(1f)
            }

            2 -> {
                mBtnRight?.setVisibility(View.GONE)
                mbuttons?.setWeightSum(2f)
            }

            3 -> mbuttons?.setWeightSum(3f)
        }

        if (butleft=="")  {
            mBtnLeft?.setVisibility(View.GONE);mbuttons?.setWeightSum(2f)
        }

        mwidth = 0;mheight = 0;mlines = 0
        items.clear()
    }

    fun buildDialog(activity: Activity, titletext: String) {
        buttonCount = 1
        buildDialogbase(activity, titletext, if (lang==0) "Back" else "Salir", "", "")
    }

    fun buildDialog(activity: Activity, titletext: String, butleft: String) {
        buttonCount = 1
        buildDialogbase(activity, titletext, butleft, "", "")
    }

    fun buildDialog(activity: Activity, titletext: String, butleft: String, butmid: String) {
        buttonCount = 2
        buildDialogbase(activity, titletext, butleft, butmid, "")
    }

    fun buildDialog(activity: Activity,titletext: String,butleft: String,
        butmid: String,butright: String)
    {
        buttonCount = 3
        buildDialogbase(activity, titletext, butleft, butmid, butright)
    }

    fun setTopRightPosition() {
        val window = dialog!!.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.TOP or Gravity.RIGHT
        window.attributes = wlp
    }

    fun setCenterScreenPosition() {
        val window = dialog!!.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        window.attributes = wlp
    }

    fun setTopCenterPosition() {
        val window = dialog!!.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        window.attributes = wlp
    }

    fun setBottomRightPosition() {
        val window = dialog!!.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.BOTTOM or Gravity.RIGHT
        window.attributes = wlp
    }

    fun setBottomCenterPosition() {
        val window = dialog!!.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.BOTTOM or Gravity.CENTER
        window.attributes = wlp
    }

    fun dismiss() {
        dialog!!.dismiss()
    }

    fun setOnLeftClick(l: View.OnClickListener?) {
        mBtnLeft!!.setOnClickListener(l)
    }

    fun setOnMiddleClick(l: View.OnClickListener?) {
        mBtnMid!!.setOnClickListener(l)
    }

    fun setOnRightClick(l: View.OnClickListener?) {
        mBtnRight!!.setOnClickListener(l)
    }

    fun clear() {
        items.clear()
    }

    fun setWidth(pWidth: Int) {
        mwidth = pWidth
        if (mwidth < 100) {
            if (mwidth>=0) {
                mwidth = 0
            } else {
                val displayMetrics = cont!!.resources.displayMetrics
                var dispw = displayMetrics.widthPixels
                mwidth = (0.95 * dispw).toInt()
            }
        }
    }

    fun measureWidth(border: Int) {
        var mw = 100
        var mwi: Int
        if (items.size == 0) return
        for (ii in items.indices) {
            mwi = mTitleLabel!!.paint.measureText(items[ii].text).toInt()
            if (mwi > mw) mw = mwi
        }
        setWidth(mw + border)
    }

    fun measureWidth(border: Int, parts: Int) {
        var mw = 100
        var mwi: Int
        val minwidth: Int
        if (items.size == 0) return
        for (ii in items.indices) {
            mwi = mTitleLabel!!.paint.measureText(items[ii].text).toInt()
            if (mwi > mw) mw = mwi
        }
        minwidth = screenWidth(parts)
        mw += border
        if (mw < minwidth) mw = minwidth
        setWidth(mw)
    }

    fun screenWidth(parts: Int): Int {
        var parts = parts
        if (parts < 0) parts = 1
        val displayMetrics = cont!!.resources.displayMetrics
        return (displayMetrics.widthPixels / parts)
    }

    fun setHeight(pHeight: Int) {
        mheight = pHeight
        if (mheight < 100) mheight = 0
    }

    fun setLines(pLines: Int) {
        mlines = pLines
        if (mlines < 1) mlines = 0
        if (mlines > 0) mheight = 0
    }

    fun setMinLines(pLines: Int) {
        mminlines = pLines
        if (mminlines < 1) mminlines = 1
    }

    fun disableCloseAfterClick() {
        mcloseafterclick=false
    }

    fun getText(index: Int): String? {
        return try {
            data[index].text
        } catch (e: Exception) {
            ""
        }
    }

    fun getCodigo(index: Int): String? {
        return try {
            data[index].codigo
        } catch (e: Exception) {
            ""
        }
    }

    fun getCodigoInt(index: Int): Int {
        return try {
            data[index].codigo!!.toInt()
        } catch (e: Exception) {
            -1
        }
    }

    fun getChecked(index: Int): Boolean {
        return try {
            data[index].checked
        } catch (e: Exception) {
            false
        }
    }

    fun addData(ss: String, checked: Boolean) {
        data.add(clsListDialogItem(0, "", ss,checked))
    }

    fun addData(id:Int,ss: String, checked: Boolean) {
        data.add(clsListDialogItem(0, ""+id, ss, checked))
    }

    fun addData(id:String,ss: String, checked: Boolean) {
        data.add(clsListDialogItem(0, ""+id, ss,checked))
    }

    fun show() {
        var fwidth: Int;var fheight: Int;var icount: Int
        val rlcount: Int;val itemHeight: Int;val headerHeight: Int;val footerHeight: Int

        fwidth = bwidth;fheight = bheight

        adapter = Adapter(data)
        mList?.adapter = adapter

        icount = data!!.size
        rlcount = icount
        if (icount < 1) return

        if (mlines > 0) {
            icount = mlines;mheight = 0
        }

        if (icount > rlcount) icount = rlcount
        if (icount < mminlines) icount = mminlines
        fwidth = mwidth
        if (fwidth == 0) fwidth = bwidth
        if (mheight > 0) {
             fheight = mheight
        } else {
            try {
                itemHeight = dpToPx(itHeight)
                headerHeight = mRelTop!!.layoutParams.height + 15
                footerHeight = mRelBot!!.layoutParams.height + 15
                fheight = icount * itemHeight + headerHeight + footerHeight
            } catch (e: Exception) {
                 fheight = bheight
            }
        }

        val displayMetrics = cont!!.resources.displayMetrics
        var dispw = displayMetrics.widthPixels
        dispw = (0.9 * dispw).toInt()

        var disph = displayMetrics.heightPixels
        disph = (0.9 * disph).toInt()

        if (fwidth > dispw) fwidth = dispw
        if (fheight > disph) fheight = disph

        //lateinit var dwparams: WindowManager.LayoutParams

        var dwparams=dialog!!.window!!.attributes
        dwparams?.width = fwidth
        dwparams?.height = fheight

        //dwparams?.width = 1200
        //dwparams?.height = 1200

        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)

        mRel?.getLayoutParams()?.width =RelativeLayout.LayoutParams.MATCH_PARENT
        mRel?.getLayoutParams()?.height =RelativeLayout.LayoutParams.MATCH_PARENT

        //dialog!!.window!!.setLayout(fwidth, fheight)

        dialog?.window?.attributes   = dwparams as WindowManager.LayoutParams

        dialog!!.show()

    }

    //endregion

    //region Private

    private fun runClickListener() {
        if (clickListener != null) {
            val cbhandler = Handler()
            cbhandler.postDelayed( { clickListener!!.run() }, 50)
        }
    }

    private fun dpToPx(dp: Int): Int {
        val scale= cont?.resources?.displayMetrics?.density
        return (dp * scale!! + 0.5f).toInt()
    }

    data class clsListDialogItem (
        var idresource: Int = 0,
        var codigo: String? = null,
        var text: String? = null,
        var checked: Boolean = false,
    )

    class Adapter(val itemList: ArrayList<clsListDialogItem>)
                     : RecyclerView.Adapter<Adapter.ViewHolder>() {

        var selectedItemPosition: Int = -1
        lateinit var lay: RelativeLayout

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.extchklistdlgitem, parent, false)
            return ViewHolder(v)
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        override fun onBindViewHolder(holder: Adapter.ViewHolder, position: Int) {
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

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            init {
                itemView.setOnClickListener(this)
            }

            fun bindItems(mitem: clsListDialogItem) {
                lay=itemView.findViewById(R.id.relldmm) as RelativeLayout

                val textViewName = itemView.findViewById(R.id.lbltext) as TextView
                val textViewName2 = itemView.findViewById(R.id.textView284) as TextView
                val icon = itemView.findViewById(R.id.imgicon) as ImageView

                textViewName.text = mitem?.text
                textViewName2!!.visibility = View.GONE

                if (mitem?.checked!!) {
                    icon.setImageResource(R.drawable.btn_save_blue)
                } else {
                    icon.setImageResource(R.drawable.blank)
                }

            }

            fun bind(mitem: clsListDialogItem, isSelected: Boolean) {
                lay.setBackgroundColor(if (isSelected)
                    Color.parseColor("#9CD0F4") else Color.TRANSPARENT)
            }

            override fun onClick(p0: View?) {
                var ss:String
                ss=""
            }
        }

    }

}