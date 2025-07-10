package com.dts.classes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Handler
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.webservice.wsCommit


class clsOrdenUpdateWS {

    var idorden = 0
    var errflag = false
    var error = ""

    private var wscom : wsCommit? = null

    //private var http: HttpClient? = null
    //private var gson = Gson()

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

        wscom = wsCommit(URL)
        //http = HttpClient()

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

            OrdenencObj?.fill("WHERE (idOrden ="+idorden+")")!!
            OrdenenccapObj?.fill("WHERE (idOrden ="+idorden+")")!!

            OrdenencObj?.fill("WHERE (idOrden ="+idorden+")")!!
            OrdenenccapObj?.fill("WHERE (idOrden ="+idorden+")")!!


            var eitem = OrdenencObj?.first()!!
            var citem = OrdenenccapObj?.first()!!

            /*
            eitem.idorden=dt.getInt(0);
            eitem.numero=dt.getString(1);
            eitem.fecha=dt.getLong(2);
            eitem.idusuario=dt.getInt(3);
            eitem.idestado=dt.getInt(4);
            eitem.idtipo=dt.getInt(5);
            eitem.idclicontact=dt.getInt(6);
            eitem.iddir=dt.getInt(7);
            eitem.idcliente=dt.getInt(8);
            eitem.descripcion=dt.getString(9);
            eitem.fecha_cierre=dt.getLong(10);
            eitem.hora_ini=dt.getLong(11);
            eitem.hora_fin=dt.getLong(12);
            eitem.prioridad=dt.getInt(13);

             */


            //SELECT        TOP (200) CODIGO_ORDEN_SERVICIO, NUMERO, CODIGO_ESTADO_ORDEN_SERVICIO, ACTIVA, CERRADA, COORDENADA_X, COORDENADA_Y, HORA_INICIO_HH, HORA_FIN_HH, DESCRIPCION, OBSERVACION,
            //PRIORIDAD



            sql="UPDATE D_ORDEN_SERVICIO_ENC SET OBSERVACION='#"+idorden+"' WHERE (CODIGO_ORDEN_SERVICIO="+idorden+")"
            items.add(sql)

            sql="UPDATE D_ORDEN_SERVICIO_ENC SET PRIORIDAD=99 WHERE (CODIGO_ORDEN_SERVICIO="+idorden+")"
            items.add(sql)


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

            wscom!!.execute(sql, { receiveResult() })

            return true
        } catch (e: Exception) {
            errflag = true
            error = object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message
            return false
        }
    }

    private fun receiveResult() {
        try {
            if (wscom?.errflag!!) throw Exception(wscom?.error!!)

            //sql="UPDATE Ordenenccap SET Recibido=1 WHERE (idOrden="+idorden+")"
            //db?.execSQL(sql)

            errflag=false
        } catch (e: java.lang.Exception) {
            error = object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message
        }

        runCallBack()
    }


    /*
    private fun receiveResultOld() {
        try {
            var rslt=http?.data!!

            if (rslt=="#") {
                //sql="UPDATE Ordenenccap SET Recibido=1 WHERE (idOrden="+idorden+")"
                //db?.execSQL(sql)

                errflag=false
            } else {
                error = rslt
            }
        } catch (e: java.lang.Exception) {
            error = object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message
        }

        runCallBack()
    }
    */

    private fun runCallBack() {
        if (callBack == null) {
            return
        } else {
            val cbhandler = Handler()
            cbhandler.postDelayed({ callBack!!.run() }, 50)
        }
    }

    //endregion

}