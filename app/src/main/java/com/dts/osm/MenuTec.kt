package com.dts.osm

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.clsClienteObj
import com.dts.classes.clsEnvioimagenObj
import com.dts.classes.clsEstadoordenObj
import com.dts.classes.clsOrdenencObj
import com.dts.classes.clsOrdenfotoObj
import com.dts.classes.clsTiposerviciosObj
import com.dts.classes.clsUpdsaveObj
import com.dts.ladapt.LA_OrdenAdapter


class MenuTec : PBase() {

    var menuview: RecyclerView? = null
    var lbluser: TextView? = null
    var lblreg: TextView? = null
    var lblpend: TextView? = null
    var imgpend: ImageView? = null

    //var fbl: fbLocItem? =null

    var OrdenencObj: clsOrdenencObj? = null
    var EstadoordenObj: clsEstadoordenObj? = null
    var TiposerviciosObj: clsTiposerviciosObj? = null
    var ClienteObj: clsClienteObj? = null

    var adapter: LA_OrdenAdapter? = null


    var items = ArrayList<clsClasses.clsOrdenlist>()

    var saveselidx=-1
    var afecha=0L
    var idle=false

    //var gps: GPSLocation? = null
    var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_menu_tec)

            super.initbase(savedInstanceState)

            menuview = findViewById<View>(R.id.recview) as RecyclerView
            menuview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

            lbluser = findViewById(R.id.textView15);lbluser?.text=gl?.nuser!!+"  "
            lblreg = findViewById(R.id.textView);lblreg?.text=""
            lblpend = findViewById(R.id.textView31);lblpend?.text=""
            imgpend = findViewById(R.id.imageView24);imgpend?.isVisible=false

            OrdenencObj = clsOrdenencObj(this, Con!!, db!!)
            EstadoordenObj = clsEstadoordenObj(this, Con!!, db!!)
            TiposerviciosObj = clsTiposerviciosObj(this, Con!!, db!!)
            ClienteObj = clsClienteObj(this, Con!!, db!!)

            //gps=GPSLocation()

            //fbl= fbLocItem(gl?.gpsroot)

            setHandlers()

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                //GPS()
                idle=true
                listItems()
            }, 20)

        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }


    //region Events

    fun doCom(view: View) {
        try {
            gl?.com_pend=true
            startActivity(Intent(this,Com::class.java))
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun doInventario(view: View) {
        try {
            startActivity(Intent(this,InvLista::class.java))
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    private fun setHandlers() {
        try {
            menuview?.addOnItemTouchListener(
                RecyclerItemClickListener(this, menuview!!,
                    object : RecyclerItemClickListener.OnItemClickListener {

                        override fun onItemClick(view: View, position: Int) {
                            saveselidx=position
                            gl?.idorden=items?.get(position)?.idorden!!
                            //GPS()
                            startIntent()
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
        var item: clsClasses.clsOrdenlist
        var regs=0;var pend=0

        if (!idle) return

        try {
            items.clear()

            EstadoordenObj?.fill()
            TiposerviciosObj?.fill()
            ClienteObj?.fill()

            OrdenencObj?.fill("WHERE (idUsuario="+gl?.iduser!!+") AND (idestado in (3,4,8))  " +
                    "ORDER BY Fecha,idOrden")
            regs=OrdenencObj?.count!!;pend=0

            for (ord in OrdenencObj?.items!!) {
                item= clsClasses.clsOrdenlist()

                item.idorden = ord.idorden
                item.tarea = nombreTipo(ord.idtipo)
                item.cliente = nombreCliente(ord.idcliente)
                item.fecha = du?.sfecha(ord.fecha).toString()+" "+du?.shora(ord.hora_ini).toString()
                item.estado = nombreEstado(ord.idestado)
                item.idestado = ord.idestado

                if (ord.idestado>0) {
                    if (ord.idestado!=5) pend++
                }

                items.add(item)
            }

            adapter = LA_OrdenAdapter(items)
            menuview?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

        lblreg?.text="Registros: "+regs
        lblpend?.text="Pendientes: "+pend
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

    /*
    fun GPS() {
        try {
            location=gps?.getlocation(this)
            gl?.gpslong=location?.longitude!!
            gl?.gpslat=location?.latitude!!

            //lbluser?.text=""+location?.latitude+" : "+location?.longitude

            val litem = clsClasses.clsLocItem(
                gl?.iduser!!, du?.actDateTime!!,
                location!!.longitude, location!!.latitude, 0
            )
            fbl!!.setItem(gl?.gpsroot!!, litem)

        } catch (e: Exception) {
            //msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            toast("No se logro obtener coordenadas.")
        }
    }
     */

    fun startIntent() {
        startActivity(Intent(this,Tarea::class.java))
    }

    fun nombreEstado(codigo:Int):String {
        try {
            for (itm in EstadoordenObj?.items!!) {
                if (itm.id==codigo) return itm.nombre
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
        return "-"
    }

    fun nombreTipo(codigo:Int):String {
        try {
            for (itm in TiposerviciosObj?.items!!) {
                if (itm.codigo_tipo_departamento==codigo) return itm.nombre
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
        return "-"
    }

    fun nombreCliente(codigo:Int):String {
        try {
            for (itm in ClienteObj?.items!!) {
                if (itm.codigo_cliente==codigo) return itm.nombre
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
        return "-"
    }

    fun registrosOffline() {
        try {

            var UpdsaveObj= clsUpdsaveObj(this,Con!!,db!!)
            var OrdenfotoObj= clsOrdenfotoObj(this,Con!!,db!!)
            var EnvioimagenObj= clsEnvioimagenObj(this,Con!!,db!!)

            UpdsaveObj.fill()
            OrdenfotoObj.fill("WHERE (statcom=0)")
            EnvioimagenObj.fill()

            var cupd=UpdsaveObj?.count
            var cfot=OrdenfotoObj?.count
            var cimg=EnvioimagenObj?.count

            imgpend?.isVisible=(cupd!! + cfot!! + cimg!!)>0
            //lblpend?.text="e: "+cupd+" / f: "+cfot+" / i: "+cimg

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

    }

    //endregion

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }

            OrdenencObj?.reconnect(Con!!,db!!)
            EstadoordenObj?.reconnect(Con!!,db!!)
            TiposerviciosObj?.reconnect(Con!!,db!!)
            ClienteObj?.reconnect(Con!!,db!!)

            try {
                adapter?.setSelectedItem(saveselidx)
            } catch (e: Exception) { }

            listItems()
            registrosOffline()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}