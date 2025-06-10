package com.dts.osm

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.extListDlg
import com.dts.ladapt.LA_ProductoAdapter
import com.dts.ladapt.LA_SupOrdenListAdapter
import com.dts.restapi.ClassesAPI
import com.dts.restapi.HttpClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Request

class SupBuscarOrden : PBase() {

    var recview: RecyclerView? = null
    var lblsuc: TextView? = null

    var adapter: LA_SupOrdenListAdapter? = null

    var http: HttpClient? = null
    var gson = Gson()

    var items = ArrayList<clsClasses.clsOrdenEncSup>()
    var suc   = ArrayList<clsSucursal>()

    var item = clsClasses.clsOrdenEncSup()

    var idsucursal=0
    var idestado=0

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_sup_buscar_orden)

            super.initbase(savedInstanceState)

            recview = findViewById<View>(R.id.recview) as RecyclerView
            recview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

            lblsuc = findViewById(R.id.textView41);lblsuc?.text=""

            http = HttpClient()

            setHandlers()

            //listItems()

            gl?.idemp=24
            idsucursal=102
            idestado=0

            recSucursales()

        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }

    //region Events

    fun doSearch(view: View) {
        recOrdenes()
    }

    fun doSucursal(view: View) {
        listaSucursales()
    }

    fun doExit(view: View) {
        finish()
    }

    private fun setHandlers() {
        try {

            recview?.addOnItemTouchListener(
                RecyclerItemClickListener(this, recview!!,
                    object : RecyclerItemClickListener.OnItemClickListener {

                        override fun onItemClick(view: View, position: Int) {
                            //productoSeleccionado = items[position]

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
        try {
            adapter = LA_SupOrdenListAdapter(items)
            recview?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun recOrdenes() {
        try {

            http?.url=gl?.urlbase+"api/Orden/GetListaOrdenesSuperFilt?pempresa="+gl?.idemp!!+"&psucursal="+idsucursal+"&pestado="+idestado

            val request: Request = Request.Builder()
                .url(http?.url!!).get()
                .addHeader("accept", "*/*")
                .build()

            http!!.processRequest(request, { cbOrdenes() })
        } catch (e: java.lang.Exception) {
            var se=e.message!!
            se=se+" "
            msgbox(object : Any() {}.javaClass.enclosingMethod.name +" . "+e.message!!);
        }
    }

    fun cbOrdenes() {
        var jss: ClassesAPI.clsAPIOrdenEncSup? = null
        var item: clsClasses.clsOrdenEncSup
        var prior = 0

        try {
            if (http!!.retcode!=1) {
                msgbox("Error: "+http!!.data);return
            }

            val parsedList =http?.splitJsonArray()
            val RType = object : TypeToken<ClassesAPI.clsAPIOrdenEncSup>() {}.type

            items.clear()

            try {

                for (pls in parsedList!!) {

                    jss=gson.fromJson(pls, RType)

                    item= clsClasses.clsOrdenEncSup()

                    item.CODIGO_ORDEN_SERVICIO = jss?.CODIGO_ORDEN_SERVICIO!!
                    item.NUMERO = jss?.NUMERO!!
                    item.NOMBRE = jss?.NOMBRE!!
                    item.USUARIO = jss?.USUARIO!!
                    item.CODIGO_SUCURSAL = jss?.CODIGO_SUCURSAL!!
                    item.NTIPO = jss?.NTIPO!!
                    item.NESTADO = jss?.NESTADO!!
                    item.ESTADO = jss?.ESTADO!!
                    item.FECHAAGR = jss?.FECHAAGR!!
                    item.FECHASERV = jss?.FECHASERV!!
                    prior=jss?.PRIORIDAD!!;if (prior==0 || prior>99) prior=100
                    item.PRIORIDAD = prior

                    item.NFECHAAGR = du?.sfecha(item.FECHAAGR)!!
                    item.NFECHASERV =du?.sfecha(item.FECHASERV)!!

                    items?.add(item)
                }

                items.sortWith(compareBy<clsClasses.clsOrdenEncSup> { it.PRIORIDAD }.thenBy { it.FECHAAGR })

            } catch (e: java.lang.Exception) {
                msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message!!)
                return
            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({listItems()}, 200)

        } catch (e: java.lang.Exception) {
            var es=e.message
            msgbox(object : Any() {}.javaClass.enclosingMethod.name +" . " + e.message!!);
        }
    }

    //endregion

    //region Sucursales

    fun recSucursales() {
        try {

            http?.url=gl?.urlbase+"api/P_SUCURSAL/GetSucursalesByEmpresa?Empresa="+gl?.idemp!!

            val request: Request = Request.Builder()
                .url(http?.url!!).get()
                .addHeader("accept", "*/*")
                .build()

            http!!.processRequest(request, { cbSucursales() })
        } catch (e: java.lang.Exception) {
            var se=e.message!!
            se=se+" "
            msgbox(object : Any() {}.javaClass.enclosingMethod.name +" . "+e.message!!);
        }
    }

    fun cbSucursales() {
        var jss: ClassesAPI.clsAPISucursal? = null
        var item: clsSucursal

        try {
            Looper.prepare()

            if (http!!.retcode!=1) {
                msgbox("Error: "+http!!.data);return
            }

            val parsedList =http?.splitJsonArray()
            val RType = object : TypeToken<ClassesAPI.clsAPISucursal>() {}.type

            suc.clear()

            try {

                for (pls in parsedList!!) {

                    jss=gson.fromJson(pls, RType)

                    item= clsSucursal()

                    item.id = jss?.CODIGO_SUCURSAL!!
                    item.nombre = jss?.DESCRIPCION.toString()

                    suc?.add(item)
                }

                suc.sortBy { it.nombre }

            } catch (e: java.lang.Exception) {
                msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message!!)
                return
            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({seleccionaSucursal()}, 200)

        } catch (e: java.lang.Exception) {
            var es=e.message
            msgbox(object : Any() {}.javaClass.enclosingMethod.name +" . " + e.message!!);
        }
    }

    fun listaSucursales() {
        try {
            val listdlg = extListDlg();

            listdlg.buildDialog(this@SupBuscarOrden, "Sucursal")
            //listdlg.setLines(4)
            listdlg.setWidth(-1)
            listdlg.setTopRightPosition()

            for (itm in suc!!) {
                listdlg.addData(itm?.id!!,itm?.nombre!!)
            }

            listdlg.clickListener= Runnable { guardaSucursal(listdlg.selcodint,listdlg.selvalue) }

            listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
            listdlg.show()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun seleccionaSucursal() {
        try {
            lblsuc?.text="";
            idsucursal=app?.loadpos(9)!!

            if (idsucursal==-1) return

            for (itm in suc!!) {
                if (itm?.id==idsucursal) {
                    lblsuc?.text=itm?.nombre;return
                }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun guardaSucursal(id : Int , text : String) {
        try {
            lblsuc?.text=text
            app?.savepos(9,id)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Dialogs

    fun dialogswitch() {
        try {
            when (gl?.dialogid) {
                0 -> {  }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

    //region Aux


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


    data class clsSucursal(
        var id: Int = 0,
        var nombre: String = "",
    )

}