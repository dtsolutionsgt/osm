package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsSavepos


class clsSaveposObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Savepos"
    var sql: String? = null
    var items = ArrayList<clsClasses.clsSavepos>()

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

    fun add(item: clsSavepos?) {
        addItem(item!!)
    }

    fun update(item: clsSavepos?) {
        updateItem(item!!)
    }

    fun delete(item: clsSavepos?) {
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

    fun first(): clsSavepos?  {
        return items[0]
    }

    //region Private

    private fun addItem(item: clsSavepos) {
        ins!!.init("Savepos")
        ins!!.add("id", item.id)
        ins!!.add("valor", item.valor)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsSavepos) {
        upd!!.init("Savepos")
        upd!!.add("valor", item.valor)
        upd!!.Where("(id=" + item.id + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsSavepos) {
        sql = "DELETE FROM Savepos WHERE (id=" + item.id + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Savepos WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsSavepos
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsSavepos(0,"")
            item.id = dt.getInt(0)
            item.valor = dt.getString(1)
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

    fun addItemSql(item: clsSavepos): String? {
        ins!!.init("Savepos")
        ins!!.add("id", item.id)
        ins!!.add("valor", item.valor)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsSavepos): String? {
        upd!!.init("Savepos")
        upd!!.add("valor", item.valor)
        upd!!.Where("(id=" + item.id + ")")
        return upd!!.sql()
    }

    //endregion


}