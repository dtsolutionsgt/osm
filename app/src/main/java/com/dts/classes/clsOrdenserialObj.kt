package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsOrdenserial


class clsOrdenserialObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Ordenserial"
    var sql: String? = null
    var items = ArrayList<clsOrdenserial>()

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

    fun add(item: clsOrdenserial?) {
        addItem(item!!)
    }

    fun update(item: clsOrdenserial?) {
        updateItem(item!!)
    }

    fun delete(item: clsOrdenserial?) {
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

    fun first(): clsOrdenserial?  {
        return items[0]
    }


    //region Private

    private fun addItem(item: clsOrdenserial) {
        ins!!.init("Ordenserial")
        ins!!.add("idorden", item.idorden)
        ins!!.add("idordendet", item.idordendet)
        ins!!.add("serial", item.serial)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsOrdenserial) {
        upd!!.init("Ordenserial")
        upd!!.Where("(idorden=" + item.idorden + ") AND (idordendet=" + item.idordendet + ") AND (serial='" + item.serial + "')")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsOrdenserial) {
        sql =
            "DELETE FROM Ordenserial WHERE (idorden=" + item.idorden + ") AND (idordendet=" + item.idordendet + ") AND (serial='" + item.serial + "')"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Ordenserial WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsOrdenserial
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsOrdenserial()
            item.idorden = dt.getInt(0)
            item.idordendet = dt.getInt(1)
            item.serial = dt.getString(2)
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

    fun addItemSql(item: clsOrdenserial): String? {
        ins!!.init("Ordenserial")
        ins!!.add("idorden", item.idorden)
        ins!!.add("idordendet", item.idordendet)
        ins!!.add("serial", item.serial)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsOrdenserial): String? {
        upd!!.init("Ordenserial")
        upd!!.Where("(idorden=" + item.idorden + ") AND (idordendet=" + item.idordendet + ") AND (serial='" + item.serial + "')")
        return upd!!.sql()
    }

    //endregion

}