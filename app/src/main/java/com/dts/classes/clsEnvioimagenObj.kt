package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsEnvioimagen

class clsEnvioimagenObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Envioimagen"
    var sql: String? = null
    var items = ArrayList<clsEnvioimagen>()

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

    fun add(item: clsEnvioimagen?) {
        addItem(item!!)
    }

    fun update(item: clsEnvioimagen?) {
        updateItem(item!!)
    }

    fun delete(item: clsEnvioimagen?) {
        deleteItem(item!!)
    }

    fun delete(item:String) {
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

    fun first(): clsEnvioimagen?  {
        return items[0]
    }


    //region Private

    private fun addItem(item: clsEnvioimagen) {
        ins!!.init("Envioimagen")
        ins!!.add("id", item.id)
        ins!!.add("tipo", item.tipo)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsEnvioimagen) {
        upd!!.init("Envioimagen")
        upd!!.add("tipo", item.tipo)
        upd!!.Where("(id='" + item.id + "')")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsEnvioimagen) {
        sql = "DELETE FROM Envioimagen WHERE (id='" + item.id + "')"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: String) {
        sql = "DELETE FROM Envioimagen WHERE id='$id'"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsEnvioimagen
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsEnvioimagen()
            item.id = dt.getString(0)
            item.tipo = dt.getInt(1)
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

    fun addItemSql(item: clsEnvioimagen): String? {
        ins!!.init("Envioimagen")
        ins!!.add("id", item.id)
        ins!!.add("tipo", item.tipo)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsEnvioimagen): String? {
        upd!!.init("Envioimagen")
        upd!!.add("tipo", item.tipo)
        upd!!.Where("(id='" + item.id + "')")
        return upd!!.sql()
    }

    //endregion
}