package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsRazon_no_atencion


class clsRazon_no_atencionObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Razon_no_atencion"
    var sql: String? = null
    var items = ArrayList<clsRazon_no_atencion>()

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

    fun add(item: clsRazon_no_atencion?) {
        addItem(item!!)
    }

    fun update(item: clsRazon_no_atencion?) {
        updateItem(item!!)
    }

    fun delete(item: clsRazon_no_atencion?) {
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

    fun first(): clsRazon_no_atencion?  {
        return items[0]
    }


    //region Private

    private fun addItem(item: clsRazon_no_atencion) {
        ins!!.init("Razon_no_atencion")
        ins!!.add("CODIGO_RAZON_NOATENCION", item.codigo_razon_noatencion)
        ins!!.add("EMPRESA", item.empresa)
        ins!!.add("DESCRIPCION", item.descripcion)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsRazon_no_atencion) {
        upd!!.init("Razon_no_atencion")
        upd!!.add("EMPRESA", item.empresa)
        upd!!.add("DESCRIPCION", item.descripcion)
        upd!!.Where("(CODIGO_RAZON_NOATENCION=" + item.codigo_razon_noatencion + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsRazon_no_atencion) {
        sql =
            "DELETE FROM Razon_no_atencion WHERE (CODIGO_RAZON_NOATENCION=" + item.codigo_razon_noatencion + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Razon_no_atencion WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsRazon_no_atencion
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsRazon_no_atencion()
            item.codigo_razon_noatencion = dt.getInt(0)
            item.empresa = dt.getInt(1)
            item.descripcion = dt.getString(2)
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

    fun addItemSql(item: clsRazon_no_atencion): String? {
        ins!!.init("Razon_no_atencion")
        ins!!.add("CODIGO_RAZON_NOATENCION", item.codigo_razon_noatencion)
        ins!!.add("EMPRESA", item.empresa)
        ins!!.add("DESCRIPCION", item.descripcion)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsRazon_no_atencion): String? {
        upd!!.init("Razon_no_atencion")
        upd!!.add("EMPRESA", item.empresa)
        upd!!.add("DESCRIPCION", item.descripcion)
        upd!!.Where("(CODIGO_RAZON_NOATENCION=" + item.codigo_razon_noatencion + ")")
        return upd!!.sql()
    }

    //endregion

}