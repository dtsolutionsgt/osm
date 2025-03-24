package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsOrdendet

class clsOrdendetObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Ordendet"
    var sql: String? = null
    var items = ArrayList<clsOrdendet>()

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

    fun add(item: clsOrdendet?) {
        addItem(item!!)
    }

    fun update(item: clsOrdendet?) {
        updateItem(item!!)
    }

    fun delete(item: clsOrdendet?) {
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

    fun first(): clsOrdendet?  {
        return items[0]
    }


    //region Private

    private fun addItem(item: clsOrdendet) {
        ins!!.init("Ordendet")
        ins!!.add("id", item.id)
        ins!!.add("idOrden", item.idorden)
        ins!!.add("idProducto", item.idproducto)
        ins!!.add("Descripcion", item.descripcion)
        ins!!.add("Realizado", item.realizado)
        ins!!.add("Cant", item.cant)
        ins!!.add("Activo", item.activo)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsOrdendet) {
        upd!!.init("Ordendet")
        upd!!.add("idOrden", item.idorden)
        upd!!.add("idProducto", item.idproducto)
        upd!!.add("Descripcion", item.descripcion)
        upd!!.add("Realizado", item.realizado)
        upd!!.add("Cant", item.cant)
        upd!!.add("Activo", item.activo)
        upd!!.Where("(id=" + item.id + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsOrdendet) {
        sql = "DELETE FROM Ordendet WHERE (id=" + item.id + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Ordendet WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsOrdendet
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsOrdendet()
            item.id = dt.getInt(0)
            item.idorden = dt.getInt(1)
            item.idproducto = dt.getInt(2)
            item.descripcion = dt.getString(3)
            item.realizado = dt.getInt(4)
            item.cant = dt.getDouble(5)
            item.activo = dt.getInt(6)
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

    fun addItemSql(item: clsOrdendet): String? {
        ins!!.init("Ordendet")
        ins!!.add("id", item.id)
        ins!!.add("idOrden", item.idorden)
        ins!!.add("idProducto", item.idproducto)
        ins!!.add("Descripcion", item.descripcion)
        ins!!.add("Realizado", item.realizado)
        ins!!.add("Cant", item.cant)
        ins!!.add("Activo", item.activo)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsOrdendet): String? {
        upd!!.init("Ordendet")
        upd!!.add("idOrden", item.idorden)
        upd!!.add("idProducto", item.idproducto)
        upd!!.add("Descripcion", item.descripcion)
        upd!!.add("Realizado", item.realizado)
        upd!!.add("Cant", item.cant)
        upd!!.add("Activo", item.activo)
        upd!!.Where("(id=" + item.id + ")")
        return upd!!.sql()
    }

    //endregion

}