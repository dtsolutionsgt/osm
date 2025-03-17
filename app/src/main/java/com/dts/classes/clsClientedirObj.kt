package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsClientedir


class clsClientedirObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Clientedir"
    var sql: String? = null
    var items = ArrayList<clsClientedir>()

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

    fun add(item: clsClientedir?) {
        addItem(item!!)
    }

    fun update(item: clsClientedir?) {
        updateItem(item!!)
    }

    fun delete(item: clsClientedir?) {
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

    fun first(): clsClientedir?  {
        return items[0]
    }


    //region Private

    private fun addItem(item: clsClientedir) {
        ins!!.init("Clientedir")
        ins!!.add("Codigo_Direccion", item.codigo_direccion)
        ins!!.add("Codigo_Cliente", item.codigo_cliente)
        ins!!.add("Direccion", item.direccion)
        ins!!.add("Telefono", item.telefono)
        ins!!.add("Referencia", item.referencia)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsClientedir) {
        upd!!.init("Clientedir")
        upd!!.add("Codigo_Cliente", item.codigo_cliente)
        upd!!.add("Direccion", item.direccion)
        upd!!.add("Telefono", item.telefono)
        upd!!.add("Referencia", item.referencia)
        upd!!.Where("(Codigo_Direccion=" + item.codigo_direccion + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsClientedir) {
        sql = "DELETE FROM Clientedir WHERE (Codigo_Direccion=" + item.codigo_direccion + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Clientedir WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsClientedir
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsClientedir()
            item.codigo_direccion = dt.getInt(0)
            item.codigo_cliente = dt.getInt(1)
            item.direccion = dt.getString(2)
            item.telefono = dt.getString(3)
            item.referencia = dt.getString(4)
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

    fun addItemSql(item: clsClientedir): String? {
        ins!!.init("Clientedir")
        ins!!.add("Codigo_Direccion", item.codigo_direccion)
        ins!!.add("Codigo_Cliente", item.codigo_cliente)
        ins!!.add("Direccion", item.direccion)
        ins!!.add("Telefono", item.telefono)
        ins!!.add("Referencia", item.referencia)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsClientedir): String? {
        upd!!.init("Clientedir")
        upd!!.add("Codigo_Cliente", item.codigo_cliente)
        upd!!.add("Direccion", item.direccion)
        upd!!.add("Telefono", item.telefono)
        upd!!.add("Referencia", item.referencia)
        upd!!.Where("(Codigo_Direccion=" + item.codigo_direccion + ")")
        return upd!!.sql()
    }

    //endregion

}