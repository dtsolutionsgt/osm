package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsOrdenenccap

class clsOrdenenccapObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Ordenenccap"
    var sql: String? = null
    var items = ArrayList<clsOrdenenccap>()

    constructor(context: Context, dbconnection: BaseDatos, dbase: SQLiteDatabase) {
        cont = context
        Con = dbconnection
        ins = Con?.Ins
        upd = Con?.Upd
        db = dbase
        count = 0
    }

    fun reconnect(dbconnection: BaseDatos, dbase: SQLiteDatabase) {
        Con = dbconnection
        ins = Con!!.Ins
        upd = Con!!.Upd
        db = dbase
    }

    fun add(item: clsOrdenenccap?) {
        addItem(item!!)
    }

    fun update(item: clsOrdenenccap?) {
        updateItem(item!!)
    }

    fun delete(item: clsOrdenenccap?) {
        deleteItem(item!!)
    }

    fun delete(item:Int) {
        deleteItem(item);
    }

    fun fill() {
        fillItems(sel)
    }

    fun fill( specstr: String) {
        fillItems(sel+ " " +specstr)
    }

    fun fillSelect(sq: String) {
        fillItems(sq)
    }

    fun first(): clsOrdenenccap?  {
        return items[0]
    }


    //region Private

    private fun addItem(item: clsOrdenenccap) {
        ins!!.init("Ordenenccap")
        ins!!.add("idOrden", item.idorden)
        ins!!.add("Anulada", item.anulada)
        ins!!.add("Activa", item.activa)
        ins!!.add("Cerrada", item.cerrada)
        ins!!.add("FirmaUsuario", item.firmausuario)
        ins!!.add("FirmaCliente", item.firmacliente)
        ins!!.add("Latit", item.latit)
        ins!!.add("Longit", item.longit)
        ins!!.add("FechaIni", item.fechaini.toDouble())
        ins!!.add("FechaFin", item.fechafin.toDouble())
        ins!!.add("Nota", item.nota)
        ins!!.add("Recibido", item.recibido)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsOrdenenccap) {
        upd!!.init("Ordenenccap")
        upd!!.add("Anulada", item.anulada)
        upd!!.add("Activa", item.activa)
        upd!!.add("Cerrada", item.cerrada)
        upd!!.add("FirmaUsuario", item.firmausuario)
        upd!!.add("FirmaCliente", item.firmacliente)
        upd!!.add("Latit", item.latit)
        upd!!.add("Longit", item.longit)
        upd!!.add("FechaIni", item.fechaini.toDouble())
        upd!!.add("FechaFin", item.fechafin.toDouble())
        upd!!.add("Nota", item.nota)
        upd!!.add("Recibido", item.recibido)
        upd!!.Where("(idOrden=" + item.idorden + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsOrdenenccap) {
        sql = "DELETE FROM Ordenenccap WHERE (idOrden=" + item.idorden + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Ordenenccap WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsOrdenenccap
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsOrdenenccap()
            item.idorden = dt.getInt(0)
            item.anulada = dt.getInt(1)
            item.activa = dt.getInt(2)
            item.cerrada = dt.getInt(3)
            item.firmausuario = dt.getString(4)
            item.firmacliente = dt.getString(5)
            item.latit = dt.getDouble(6)
            item.longit = dt.getDouble(7)
            item.fechaini = dt.getLong(8)
            item.fechafin = dt.getLong(9)
            item.nota = dt.getString(10)
            item.recibido = dt.getInt(11)
            items.add(item)
            dt.moveToNext()
        }
        if (dt != null) dt.close()
    }

    fun newID(idsql: String?): Int {
        var dt: Cursor? = null
        var nid: Int
        try {
            dt = Con!!.OpenDT(idsql)
            dt.moveToFirst()
            nid = dt.getInt(0) + 1
        } catch (e: Exception) {
            nid = 1
        }
        dt?.close()
        return nid
    }

    fun addItemSql(item: clsOrdenenccap): String? {
        ins!!.init("Ordenenccap")
        ins!!.add("idOrden", item.idorden)
        ins!!.add("Anulada", item.anulada)
        ins!!.add("Activa", item.activa)
        ins!!.add("Cerrada", item.cerrada)
        ins!!.add("FirmaUsuario", item.firmausuario)
        ins!!.add("FirmaCliente", item.firmacliente)
        ins!!.add("Latit", item.latit)
        ins!!.add("Longit", item.longit)
        ins!!.add("FechaIni", item.fechaini.toDouble())
        ins!!.add("FechaFin", item.fechafin.toDouble())
        ins!!.add("Nota", item.nota)
        ins!!.add("Recibido", item.recibido)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsOrdenenccap): String? {
        upd!!.init("Ordenenccap")
        upd!!.add("Anulada", item.anulada)
        upd!!.add("Activa", item.activa)
        upd!!.add("Cerrada", item.cerrada)
        upd!!.add("FirmaUsuario", item.firmausuario)
        upd!!.add("FirmaCliente", item.firmacliente)
        upd!!.add("Latit", item.latit)
        upd!!.add("Longit", item.longit)
        upd!!.add("FechaIni", item.fechaini.toDouble())
        upd!!.add("FechaFin", item.fechafin.toDouble())
        upd!!.add("Nota", item.nota)
        upd!!.add("Recibido", item.recibido)
        upd!!.Where("(idOrden=" + item.idorden + ")")
        return upd!!.sql()
    }

    //endregion


}