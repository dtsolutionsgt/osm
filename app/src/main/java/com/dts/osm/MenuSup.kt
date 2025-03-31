package com.dts.osm

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.clsUsuarioObj
import com.dts.classes.extListDlg
import com.dts.fbase.fbServicio
import com.dts.ladapt.LA_OrdenSupAdapter


class MenuSup : PBase() {

    var recview: RecyclerView? = null
    var lbluser: TextView? = null
    var lblfilter: TextView? = null
    var lblproc: TextView? = null
    var lblpend: TextView? = null
    var lblcomp: TextView? = null
    var pbar: ProgressBar? = null

    var fbsa : fbServicio? = null
    var fbsc : fbServicio? = null

    var adapter: LA_OrdenSupAdapter? = null

    val items = ArrayList<clsClasses.clsOrdenlist>()
    val fitems = ArrayList<clsClasses.clsOrdenlist>()
    var item = clsClasses.clsOrdenlist()

    var saveselidx:Int=-1
    var idle=true
    var filter=4

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_menu_sup)

            super.initbase(savedInstanceState)

            recview = findViewById<View>(R.id.recview) as RecyclerView
            recview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            lbluser = findViewById(R.id.textView15);lbluser?.text=gl?.nuser!!+"   "
            lblfilter = findViewById(R.id.textView41);lblfilter?.text="En proceso"
            lblproc = findViewById(R.id.textView34);lblproc?.text="-"
            lblpend = findViewById(R.id.textView32);lblpend?.text="-"
            lblcomp = findViewById(R.id.textView33);lblcomp?.text="-"

            pbar = findViewById(R.id.progressBar);pbar?.isVisible=false

            fbsc=fbServicio("osm",du?.actMonth,du?.actDay)
            fbsa=fbServicio("osm","servicio","orden")

            setHandlers()

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed( { actualizaDatos() }, 200)

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //region Events

    fun doFilter(view: View) {
        showMainMenu()
    }

    fun setHandlers() {
        try {
            recview?.addOnItemTouchListener(
                RecyclerItemClickListener(this, recview!!,
                    object : RecyclerItemClickListener.OnItemClickListener {

                        override fun onItemClick(view: View, position: Int) {
                            saveselidx=position
                        }

                        override fun onItemLongClick(view: View?, position: Int) { }
                    })
            )

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Main

    private fun listItems() {
        var tproc=0
        var tpend=0
        var tcomp=0

        try {

            lblproc?.text="-";lblpend?.text="-";lblcomp?.text="-"
            fitems.clear()

            for (itm in items) {
                if (filter==0) {
                    fitems.add(itm)
                } else {
                    if (itm.idestado==filter) fitems.add(itm)
                }

                when (itm.idestado) {
                    3 -> { tpend++ }
                    4 -> { tproc++ }
                    5 -> { tcomp++ }
                }

            }

            lblproc?.text=""+tproc;lblpend?.text=""+tpend;lblcomp?.text=""+tcomp

            adapter = LA_OrdenSupAdapter(fitems)
            recview?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
        idle=true;
    }

    fun actualizaDatos() {
        try {
            idle=false
            items.clear()
            fbsc?.listItems( { cbCompletos() } )
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun cbCompletos() {
        try {
            if (fbsc?.errflag!!) throw Exception(fbsc?.value)

            for (itm in fbsc?.items!!) {

                item = clsClasses.clsOrdenlist()

                item.idorden=itm.id
                item.numero=itm.numero
                item.tarea=itm.tarea
                item.cliente=itm.cliente
                item.fecha="Inicio: "+du?.shora(itm.inicio)!!
                item.fechafin="Fin: "+du?.shora(itm.fin)!!
                item.estado=itm.estado
                item.idestado=5
                item.user=itm.user

                items.add(item)
            }

            fbsa?.listItems( { cbActuales() } )
        } catch (e: Exception) {
            finerr(javaClass.enclosingMethod.name,e.message.toString())
        }
    }

    fun cbActuales() {
        try {
            if (fbsa?.errflag!!) throw Exception(fbsa?.value)

            for (itm in fbsa?.items!!) {

                item = clsClasses.clsOrdenlist()

                item.idorden=itm.id
                item.numero=itm.numero
                item.tarea=itm.tarea
                item.cliente=itm.cliente
                item.fecha="Inicio: "+du?.shora(itm.inicio)!!
                item.fechafin="Fin: "+du?.shora(itm.fin)!!
                item.estado=itm.estado
                if (item.estado=="En proceso") item.idestado=4 else item.idestado=3
                item.user=itm.user

                items.add(item)
            }

            items.sortedWith(compareByDescending<clsClasses.clsOrdenlist> { it.idestado }
                .thenByDescending { it.idorden })

            listItems()
            finok()
        } catch (e: Exception) {
            finerr(javaClass.enclosingMethod.name,e.message.toString())
        }
    }

    //endregion

    //region Dialogs

    fun dialogswitch() {
        try {
            when (gl?.dialogid) {
                0 -> {}
                1 -> {}
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun showMainMenu() {
        try {
            val listdlg = extListDlg();

            listdlg.buildDialog(this@MenuSup, "Filtro")
            listdlg.setLines(4)
            listdlg.setWidth(-1)
            listdlg.setTopRightPosition()

            listdlg.addData(4,"En proceso")
            listdlg.addData(3,"Asignada")
            listdlg.addData(5,"Completo")
            listdlg.addData(0,"Todos")

            listdlg.clickListener= Runnable { processMainMenu(listdlg.selcodint,listdlg.selvalue) }

            listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
            listdlg.show()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun processMainMenu(menucod:Int,menutext: String) {
        try {
           filter=menucod
           lblfilter?.text=menutext
           listItems()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Aux

    fun finok() {
        idle=true
        pbar?.visibility=View.INVISIBLE
    }

    fun finerr(msg1: String,msg2: String) {
        idle=true
        val ss="Sincronización termino con error: \n"+msg1+"\n"+msg2
        runOnUiThread { msgbox(ss) }
        pbar?.visibility=View.INVISIBLE
    }

    //endregion

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }


        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}