package com.dts.osm

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
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
import com.dts.classes.clsOrdenenccapObj
import com.dts.classes.clsTiposervicioObj
import com.dts.ladapt.LA_OrdenAdapter


class MenuTec : PBase() {

    var menuview: RecyclerView? = null
    var lbluser: TextView? = null
    var lblreg: TextView? = null
    var lblpend: TextView? = null
    var imgpend: ImageView? = null
    var lblbtnpend: TextView? = null
    var lblbtact: TextView? = null
    var lblbtncomp: TextView? = null

    var OrdenencObj: clsOrdenencObj? = null
    var EstadoordenObj: clsEstadoordenObj? = null
    var TiposervicioObj: clsTiposervicioObj? = null
    var ClienteObj: clsClienteObj? = null
    var OrdenenccapObj: clsOrdenenccapObj? = null

    var adapter: LA_OrdenAdapter? = null

    var items = ArrayList<clsClasses.clsOrdenlist>()

    var saveselidx=-1
    var afecha=0L
    var idle=false
    var listmode=0
    var tproc=0;var tpend=0;var tcomp=0


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
            lblbtnpend = findViewById(R.id.textView43)
            lblbtact = findViewById(R.id.textView42)
            lblbtncomp = findViewById(R.id.textView38)

            OrdenencObj = clsOrdenencObj(this, Con!!, db!!)
            EstadoordenObj = clsEstadoordenObj(this, Con!!, db!!)
            TiposervicioObj = clsTiposervicioObj(this, Con!!, db!!)
            ClienteObj = clsClienteObj(this, Con!!, db!!)
            OrdenenccapObj = clsOrdenenccapObj(this, Con!!, db!!)

            setHandlers()

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                idle=true
                marcaBoton(1)
                if (items?.size==0) marcaBoton(0)
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

    fun doNueva(view: View) {
        try {
            startActivity(Intent(this,TipoServicio::class.java))
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

    fun doPendClick(view: View) {
        try {
            marcaBoton(0)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun doActClick(view: View) {
        try {
            marcaBoton(1)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun doCompClick(view: View) {
        try {
            marcaBoton(2)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun setHandlers() {
        try {
            menuview?.addOnItemTouchListener(
                RecyclerItemClickListener(this, menuview!!,
                    object : RecyclerItemClickListener.OnItemClickListener {

                        override fun onItemClick(view: View, position: Int) {
                            saveselidx=position
                            gl?.idorden=items?.get(position)?.idorden!!

                            abrirOrden()
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

    fun listItems() {
        var item: clsClasses.clsOrdenlist
        var pend=0

        if (!idle) return

        try {
            items.clear()
            tproc=0;tpend=0;tcomp=0

            EstadoordenObj?.fill()
            TiposervicioObj?.fill()
            ClienteObj?.fill()

            OrdenencObj?.fill("WHERE (idestado in (2,3,4,8))  ORDER BY Numero")
            //OrdenencObj?.fill("WHERE (idUsuario="+gl?.iduser!!+") AND (idestado in (2,3,4,8)) ORDER BY Numero")

            pend=0

            for (ord in OrdenencObj?.items!!) {
                item= clsClasses.clsOrdenlist()

                item.idorden = ord.idorden
                item.numero = ord.numero
                item.tarea = nombreTipo(ord.idtipo)
                item.cliente = nombreCliente(ord.idcliente)
                item.fecha = du?.sfecha(ord.fecha).toString()+" "+du?.shora(ord.hora_ini).toString()
                item.estado = nombreEstado(ord.idestado)
                item.idestado = ord.idestado

                if (ord.idestado>0) {
                    //if (ord.idestado!=5) pend++
                }

                if (item.idestado==listmode) {
                    items.add(item)
                }

                when (item.idestado) {
                    3 -> { tpend++ }
                    4 -> { tproc++ }
                    5 -> { tcomp++ }
                }
            }

            adapter = LA_OrdenAdapter(items)
            menuview?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

        lblreg?.text="Faltan: "+pend
        lblpend?.text="Completos: "+tcomp
    }

    fun abrirOrden() {
        try {
            when (listmode) {
                3 -> { msgask(0,1,"¿Iniciar servicio?") }
                4 -> { startActivity(Intent(this,Orden::class.java)) }
                5 -> { startActivity(Intent(this,Orden::class.java)) }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun iniciarOrden() {
        try {
            OrdenencObj?.fill("WHERE (idorden="+gl?.idorden+")")
            OrdenenccapObj?.fill("WHERE (idorden="+gl?.idorden+")")

            var encitem=OrdenencObj?.first()
            encitem?.idestado=4
            OrdenencObj?.update(encitem)

            var capitem=OrdenenccapObj?.first()

            capitem?.fechaini=du?.actDateTime!!

            OrdenenccapObj?.update(capitem)

            listItems()

            startActivity(Intent(this,Orden::class.java))
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Dialogs

    fun dialogswitch() {
        try {
            when (gl?.dialogid) {
                0 -> { iniciarOrden() }
                1 -> { startActivity(Intent(this,Orden::class.java)) }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

    //region Aux

    fun nombreEstado(codigo:Int):String {
        try {
            for (itm in EstadoordenObj?.items!!) {
                if (itm.id==codigo) {
                    if (codigo<4) return "Pendiente" else return itm.nombre
                }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
        return "-"
    }

    fun nombreTipo(codigo:Int):String {
        try {
            for (itm in TiposervicioObj?.items!!) {
                if (itm.codigo_tipo_orden_servicio==codigo) return itm.nombre
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

            var OrdenenccapObj= clsOrdenenccapObj(this,Con!!,db!!)
            var EnvioimagenObj= clsEnvioimagenObj(this,Con!!,db!!)

            OrdenenccapObj.fill("WHERE (activa=1)")
            EnvioimagenObj.fill()

            var cord=OrdenenccapObj?.count
            var cimg=EnvioimagenObj?.count

            imgpend?.isVisible=(cord!! + cimg!!)>0

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

    }

    fun marcaBoton(bpos:Int) {
        try {

            when (bpos) {
                0 -> {
                    lblbtnpend?.setBackgroundResource(R.drawable.frame_key_select)
                    lblbtact?.setBackgroundResource(R.drawable.frame_btn)
                    lblbtncomp?.setBackgroundResource(R.drawable.frame_btn)
                    listmode=3
                }
                1 -> {
                    lblbtnpend?.setBackgroundResource(R.drawable.frame_btn)
                    lblbtact?.setBackgroundResource(R.drawable.frame_key_select)
                    lblbtncomp?.setBackgroundResource(R.drawable.frame_btn)
                    listmode=4
                }
                2 -> {
                    lblbtnpend?.setBackgroundResource(R.drawable.frame_btn)
                    lblbtact?.setBackgroundResource(R.drawable.frame_btn)
                    lblbtncomp?.setBackgroundResource(R.drawable.frame_key_select)
                    listmode=5
                }
            }

            listItems()
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
            TiposervicioObj?.reconnect(Con!!,db!!)
            ClienteObj?.reconnect(Con!!,db!!)
            OrdenenccapObj?.reconnect(Con!!,db!!)

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