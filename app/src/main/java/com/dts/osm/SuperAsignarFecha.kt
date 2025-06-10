package com.dts.osm

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.wsOpenDT
import com.dts.ladapt.LA_SuperFechaAdapter

class SuperAsignarFecha : PBase() {

    var recview: RecyclerView? = null

    var adapter: LA_SuperFechaAdapter? = null

    var wso: wsOpenDT? = null

    var items = ArrayList<clsClasses.clsOrdenSupHora>()

    var item = clsClasses.clsOrdenSupHora()


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_super_asignar_fecha)

            super.initbase(savedInstanceState)

            recview = findViewById<View>(R.id.recview) as RecyclerView
            recview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

            wso = wsOpenDT(gl?.wsurl)

            setHandlers()

            listaHoras()

        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }

    //region Events

    fun doExit(view: View) {
        finish()
    }

    private fun setHandlers() {
        try {

            recview?.addOnItemTouchListener(
                RecyclerItemClickListener(this, recview!!,
                    object : RecyclerItemClickListener.OnItemClickListener {

                        override fun onItemClick(view: View, position: Int) {
                            item = items[position]

                            val context: Context = view?.getContext()!!
                            val intent = Intent(context, SuperAsignarFecha::class.java)
                            context.startActivity(intent)

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
            adapter = LA_SuperFechaAdapter(items)
            recview?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    private fun listaHoras() {
        try {
            items.clear()

            sql = "SELECT dbo.D_ORDEN_SERVICIO_ENC.CODIGO_ORDEN_SERVICIO, dbo.D_ORDEN_SERVICIO_ENC.NUMERO, dbo.Users.Nombre, dbo.P_CLIENTE.NOMBRE AS CLIENTE,  " +
                    "dbo.P_TIPO_ORDEN_SERVICIO.NOMBRE AS TIPO, dbo.AndrDate(dbo.D_ORDEN_SERVICIO_ENC.FECHA_SERVICIO) AS FECHA, " +
                    "dbo.AndrHour(dbo.D_ORDEN_SERVICIO_ENC.HORA_SERVICIO_INI) AS HORAINI, dbo.AndrHour(dbo.D_ORDEN_SERVICIO_ENC.HORA_SERVICIO_FIN) AS HORAFIN " +
                    "FROM dbo.D_ORDEN_SERVICIO_ENC " +
                    "INNER JOIN dbo.D_ORDEN_SERVICIO_USUARIO ON dbo.D_ORDEN_SERVICIO_ENC.CODIGO_ORDEN_SERVICIO = dbo.D_ORDEN_SERVICIO_USUARIO.CODIGO_ORDEN_SERVICIO " +
                    "INNER JOIN dbo.Users ON dbo.D_ORDEN_SERVICIO_USUARIO.CODIGO_USUARIO = dbo.Users.UserId " +
                    "INNER JOIN dbo.P_CLIENTE ON dbo.D_ORDEN_SERVICIO_ENC.CODIGO_CLIENTE = dbo.P_CLIENTE.CODIGO_CLIENTE " +
                    "INNER JOIN dbo.P_TIPO_ORDEN_SERVICIO ON dbo.D_ORDEN_SERVICIO_ENC.CODIGO_TIPO_ORDEN_SERVICIO = dbo.P_TIPO_ORDEN_SERVICIO.CODIGO_TIPO_ORDEN_SERVICIO " +
                    "WHERE (dbo.D_ORDEN_SERVICIO_ENC.CODIGO_EMPRESA = "+gl?.idemp+") " +
                    "AND (dbo.D_ORDEN_SERVICIO_ENC.FECHA_SERVICIO > '20250101') " +
                    "ORDER BY dbo.D_ORDEN_SERVICIO_ENC.FECHA_SERVICIO, dbo.D_ORDEN_SERVICIO_ENC.HORA_SERVICIO_INI, dbo.Users.Nombre"

            wso!!.execute(sql) { cbListaHoras() }

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    private fun cbListaHoras() {
        val dt: Cursor?
        var ff = 0L
        var fini = 0L
        var ffin = 0L

        try {
            if (wso!!.errflag) {
                throw java.lang.Exception(wso!!.error)
            }

            dt = wso!!.openDTCursor

            var rn=dt?.count!!
            if (rn>0) {

                dt?.moveToFirst()

                for (i in 0 until rn) {
                    item=clsClasses.clsOrdenSupHora()

                    item.CODIGO_ORDEN_SERVICIO=dt?.getInt(0)!!
                    item.NUMERO = dt?.getString(1)!!
                    item.NOMBRE = dt?.getString(2)!!
                    item.CLIENTE = dt?.getString(3)!!
                    item.TIPO = dt?.getString(4)!!

                    ff = dt?.getLong(5)!!
                    fini = dt?.getLong(6)!!
                    ffin = dt?.getLong(7)!!

                    item.FECHAINI = ff+fini
                    item.FECHAFIN = ff+ffin
                    item.HORAINI = fini
                    item.HORAFIN = ffin
                    item.SFECHAINI  = du?.sfechash(item.FECHAINI)+" "+du?.dayweeksp(item.FECHAINI)+" - "+du?.shora(item.FECHAINI)
                    item.SFECHAFIN  = du?.shora(item.FECHAFIN)!!

                    items.add(item)

                    if (i<rn-1) {
                        dt?.moveToNext()
                    }
                }
            }

            listItems()
        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
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

}