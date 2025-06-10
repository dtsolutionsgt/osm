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
import com.dts.ladapt.LA_OrdenAsigAdapter
import com.dts.ladapt.LA_ProductoAdapter

class SupAsignarOrden : PBase() {

    var recview: RecyclerView? = null

    var adapter: LA_OrdenAsigAdapter? = null

    var wso: wsOpenDT? = null

    var items = ArrayList<clsClasses.clsOrdenSupAsig>()

    var item = clsClasses.clsOrdenSupAsig()


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_sup_asignar_orden)

            super.initbase(savedInstanceState)

            recview = findViewById<View>(R.id.recview) as RecyclerView
            recview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

            wso = wsOpenDT(gl?.wsurl)

            setHandlers()

            listaOrdenes()

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
            adapter = LA_OrdenAsigAdapter(items)
            recview?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }


    private fun listaOrdenes() {
        try {
            items.clear()

            sql = "SELECT  dbo.D_ORDEN_SERVICIO_ENC.CODIGO_ORDEN_SERVICIO, dbo.D_ORDEN_SERVICIO_ENC.NUMERO, dbo.AndrDate(dbo.D_ORDEN_SERVICIO_ENC.FECHA_AGR) AS Fecha, " +
                    "dbo.P_CLIENTE.NOMBRE AS CLIENTE, dbo.P_TIPO_ORDEN_SERVICIO.NOMBRE AS TIPO, COUNT(dbo.D_ORDEN_SERVICIO_USUARIO.CODIGO_ORDEN_SERVICIO) AS Expr1 " +
                    "FROM  dbo.D_ORDEN_SERVICIO_ENC " +
                    "INNER JOIN dbo.P_CLIENTE ON dbo.D_ORDEN_SERVICIO_ENC.CODIGO_CLIENTE = dbo.P_CLIENTE.CODIGO_CLIENTE " +
                    "INNER JOIN  dbo.P_TIPO_ORDEN_SERVICIO ON dbo.D_ORDEN_SERVICIO_ENC.CODIGO_TIPO_ORDEN_SERVICIO = dbo.P_TIPO_ORDEN_SERVICIO.CODIGO_TIPO_ORDEN_SERVICIO " +
                    "LEFT OUTER JOIN dbo.D_ORDEN_SERVICIO_USUARIO ON dbo.D_ORDEN_SERVICIO_ENC.CODIGO_ORDEN_SERVICIO = dbo.D_ORDEN_SERVICIO_USUARIO.CODIGO_ORDEN_SERVICIO " +
                    "WHERE (dbo.D_ORDEN_SERVICIO_ENC.CODIGO_EMPRESA = "+gl?.idemp+") " +
                    "AND (dbo.D_ORDEN_SERVICIO_ENC.CODIGO_ESTADO_ORDEN_SERVICIO = 2) " +
                    "AND (dbo.D_ORDEN_SERVICIO_ENC.ANULADA = 0) " +
                    "GROUP BY dbo.D_ORDEN_SERVICIO_ENC.CODIGO_ORDEN_SERVICIO, dbo.D_ORDEN_SERVICIO_ENC.NUMERO, dbo.AndrDate(dbo.D_ORDEN_SERVICIO_ENC.FECHA_AGR), " +
                    "dbo.P_CLIENTE.NOMBRE,  dbo.P_TIPO_ORDEN_SERVICIO.NOMBRE " +
                    "HAVING  (COUNT(dbo.D_ORDEN_SERVICIO_USUARIO.CODIGO_ORDEN_SERVICIO) = 0) " +
                    "ORDER BY Fecha, CLIENTE"

            wso!!.execute(sql) { cbListaOrdenes() }

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    private fun cbListaOrdenes() {
        val dt: Cursor?

        try {
            if (wso!!.errflag) {
                throw java.lang.Exception(wso!!.error)
            }

            dt = wso!!.openDTCursor

            var rn=dt?.count!!
            if (rn>0) {

                dt?.moveToFirst()

                for (i in 0 until rn) {
                    item=clsClasses.clsOrdenSupAsig()

                    item.CODIGO_ORDEN_SERVICIO=dt?.getInt(0)!!
                    item.NUMERO = dt?.getString(1)!!
                    item.CLIENTE = dt?.getString(3)!!
                    item.TIPO = dt?.getString(4)!!
                    item.FECHA = dt?.getLong(2)!!
                    item.SFECHA = du?.sfecha(item.FECHA)!!

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

    //region Web Service


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