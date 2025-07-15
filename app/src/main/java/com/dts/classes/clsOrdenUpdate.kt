package com.dts.classes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Handler
import android.os.Looper
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsOrdenenc
import com.dts.restapi.HttpCommit
import com.dts.webservice.wsCommit


class clsOrdenUpdate {

    var idorden = 0
    var errflag = false
    var error = ""

    var hcom : HttpCommit? = null

    private var cont: Context? = null
    private var Con: BaseDatos? = null
    private var db: SQLiteDatabase? = null
    private var ins: BaseDatos.Insert? = null
    private var upd: BaseDatos.Update? = null
    private val clsCls = clsClasses()

    private var callBack: Runnable?
    private var items = ArrayList<String>()
    private var sql = ""
    private var URL = ""


    //apiurlbase

    constructor(context: Context,url: String, dbconnection: BaseDatos, dbase: SQLiteDatabase) {
        cont = context
        Con = dbconnection
        ins = Con?.Ins
        upd = Con?.Upd
        db = dbase

        URL=url
        callBack = null

        hcom = HttpCommit(URL+"api/Orden/Commit")

    }

    fun updateOrden(id: Int, rnCallback: Runnable) {
        try {

            idorden = id
            callBack = rnCallback
            errflag = true

            if (buildSQL()) {
                if (sendUpdate()) return
            }

            runCallBack()
        } catch (e: Exception) {
            error = object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message
            throw Exception(error)
        }
    }

    //region Private

    private fun buildSQL() : Boolean {
        try {

            items.clear()

            var OrdenencObj = clsOrdenencObj(cont!!, Con!!, db!!)
            var OrdenenccapObj = clsOrdenenccapObj(cont!!, Con!!, db!!)
            var OrdendetObj = clsOrdendetObj(cont!!, Con!!, db!!)

            OrdenencObj?.fill("WHERE (idOrden ="+idorden+")")!!
            OrdenenccapObj?.fill("WHERE (idOrden ="+idorden+")")!!
            OrdendetObj?.fill("WHERE (idOrden ="+idorden+")")!!

            var eitem = OrdenencObj?.first()!!
            var citem = OrdenenccapObj?.first()!!

            upd!!.init("D_ORDEN_SERVICIO_ENC")

            upd!!.add("CODIGO_ESTADO_ORDEN_SERVICIO", eitem.idestado)
            upd!!.add("OBSERVACION", citem.nota+" ")
            if (citem.fechaini>0) upd!!.add("HORA_INICIO_HH", univfecha(citem.fechaini))
            if (citem.fechafin>0) upd!!.add("HORA_FIN_HH", univfecha(citem.fechafin))
            upd!!.add("COORDENADA_X", citem.latit)
            upd!!.add("COORDENADA_Y", citem.longit)

            upd!!.Where("(CODIGO_ORDEN_SERVICIO=" + eitem.idorden + ")")

            items.add(upd!!.sql())

            for (itm in OrdendetObj?.items!!) {

                upd!!.init("D_ORDEN_SERVICIO_DET")

                upd!!.add("CANTIDAD", itm.cant)
                upd!!.add("ACTIVO", itm.activo)
                upd!!.add("REALIZADO", itm.realizado)
                if (itm.idnoaten>0) upd!!.add("CODIGO_NO_ATENCION", itm.idnoaten)
                if (itm.horaini>0) upd!!.add("HORA_INI", univfecha(itm.horaini))
                if (itm.horafin>0) upd!!.add("HORA_FINAL", univfecha(itm.horafin))
                upd!!.add("SERIAL", itm.serial+"")

                upd!!.Where("(CODIGO_ORDEN_SERVICIO_DET=" + itm.id + ")")

                items.add(upd!!.sql())

            }

            //sql="UPDATE D_ORDEN_SERVICIO_ENC SET OBSERVACION='#"+idorden+"' WHERE (CODIGO_ORDEN_SERVICIO="+idorden+")"
            //items.add(sql)
            //sql="UPDATE D_ORDEN_SERVICIO_ENC SET PRIORIDAD=99 WHERE (CODIGO_ORDEN_SERVICIO="+idorden+")"
            //items.add(sql)

            return true
        } catch (e: Exception) {
            error = object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message
            return false
        }
    }

    private fun sendUpdate() : Boolean {
        try {
            sql=""
            for (itm in items) {
                sql+=itm+";"
            }

            hcom?.commit(sql, { receiveResult() })

            return true
        } catch (e: Exception) {
            errflag = true
            error = object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message
            return false
        }
    }

    private fun receiveResult() {
        try {
            if (hcom?.errflag!!) throw Exception(hcom?.error!!)

            //sql="UPDATE Ordenenccap SET Recibido=1 WHERE (idOrden="+idorden+")"
            //db?.execSQL(sql)

            errflag=false
        } catch (e: java.lang.Exception) {
            error = object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message
        }

        runCallBack()
    }

    private fun runCallBack() {
        if (callBack == null) {
            return
        } else {
            try {
                callBack!!.run()
            } catch (e: Exception) {
                var es=e.message
            }
        }
    }

    //endregion

    //region Aux


    fun univfecha(f: Long): String {
        var f = f
        val vy: Long
        val vm: Long
        val vd: Long
        val m: Long
        val h: Long
        var s: String

        //yyyyMMdd hh:mm:ss
        vy = (f / 100000000).toLong()
        f = f % 100000000
        vm = (f / 1000000).toLong()
        f = f % 1000000
        vd = (f / 10000).toLong()
        f = f % 10000
        h = (f.toInt() / 100).toLong()
        m = f % 100
        s = "20"
        s = if (vy > 9) s + vy else s + "0" + vy
        s = if (vm > 9) s + vm else s + "0" + vm
        s = if (vd > 9) s + vd else s + "0" + vd
        s = "$s "
        s = if (h > 9) s + h else s + "0" + h
        s = "$s:"
        s = if (m > 9) s + m else s + "0" + m
        s = "$s:00"
        return s
    }


    //endregion

}