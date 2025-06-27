package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsOrdenfoto


class clsOrdenfotoObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Ordenfoto"
    var sql: String? = null
    var items = ArrayList<clsOrdenfoto>()

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

    fun add(item: clsOrdenfoto?) {
        addItem(item!!)
    }

    fun update(item: clsOrdenfoto?) {
        updateItem(item!!)
    }

    fun delete(item: clsOrdenfoto?) {
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

    fun first(): clsOrdenfoto?  {
        return items[0]
    }


    //region Private

    private fun addItem(item: clsOrdenfoto) {
        ins!!.init("Ordenfoto")
        ins!!.add("id", item.id)
        ins!!.add("idOrden", item.idorden)
        ins!!.add("idOrdenDet", item.idordendet)
        ins!!.add("nombre", item.nombre)
        ins!!.add("nota", item.nota)
        ins!!.add("statcom", item.statcom)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsOrdenfoto) {
        upd!!.init("Ordenfoto")
        upd!!.add("idOrden", item.idorden)
        upd!!.add("idOrdenDet", item.idordendet)
        upd!!.add("nombre", item.nombre)
        upd!!.add("nota", item.nota)
        upd!!.add("statcom", item.statcom)
        upd!!.Where("(id=" + item.id + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsOrdenfoto) {
        sql = "DELETE FROM Ordenfoto WHERE (id=" + item.id + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Ordenfoto WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsOrdenfoto
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsOrdenfoto()
            item.id = dt.getInt(0)
            item.idorden = dt.getInt(1)
            item.idordendet = dt.getInt(2)
            item.nombre = dt.getString(3)
            item.nota = dt.getString(4)
            item.statcom = dt.getInt(5)
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

    fun addItemSql(item: clsOrdenfoto): String? {
        ins!!.init("Ordenfoto")
        ins!!.add("id", item.id)
        ins!!.add("idOrden", item.idorden)
        ins!!.add("idOrdenDet", item.idordendet)
        ins!!.add("nombre", item.nombre)
        ins!!.add("nota", item.nota)
        ins!!.add("statcom", item.statcom)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsOrdenfoto): String? {
        upd!!.init("Ordenfoto")
        upd!!.add("idOrden", item.idorden)
        upd!!.add("idOrdenDet", item.idordendet)
        upd!!.add("nombre", item.nombre)
        upd!!.add("nota", item.nota)
        upd!!.add("statcom", item.statcom)
        upd!!.Where("(id=" + item.id + ")")
        return upd!!.sql()
    }

    //endregion
}