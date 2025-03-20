package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsProdprecio


class clsProdprecioObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Prodprecio"
    var sql: String? = null
    var items = ArrayList<clsProdprecio>()

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

    fun add(item: clsProdprecio?) {
        addItem(item!!)
    }

    fun update(item: clsProdprecio?) {
        updateItem(item!!)
    }

    fun delete(item: clsProdprecio?) {
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

    fun first(): clsProdprecio?  {
        return items[0]
    }


    //region Private

    private fun addItem(item: clsProdprecio) {
        ins!!.init("Prodprecio")
        ins!!.add("CODIGO_PRECIO", item.codigo_precio)
        ins!!.add("CODIGO_PRODUCTO", item.codigo_producto)
        ins!!.add("NIVEL", item.nivel)
        ins!!.add("PRECIO", item.precio)
        ins!!.add("UNIDADMEDIDA", item.unidadmedida)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsProdprecio) {
        upd!!.init("Prodprecio")
        upd!!.add("CODIGO_PRODUCTO", item.codigo_producto)
        upd!!.add("NIVEL", item.nivel)
        upd!!.add("PRECIO", item.precio)
        upd!!.add("UNIDADMEDIDA", item.unidadmedida)
        upd!!.Where("(CODIGO_PRECIO=" + item.codigo_precio + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsProdprecio) {
        sql = "DELETE FROM Prodprecio WHERE (CODIGO_PRECIO=" + item.codigo_precio + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Prodprecio WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsProdprecio
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsProdprecio()
            item.codigo_precio = dt.getInt(0)
            item.codigo_producto = dt.getInt(1)
            item.nivel = dt.getInt(2)
            item.precio = dt.getDouble(3)
            item.unidadmedida = dt.getString(4)
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

    fun addItemSql(item: clsProdprecio): String? {
        ins!!.init("Prodprecio")
        ins!!.add("CODIGO_PRECIO", item.codigo_precio)
        ins!!.add("CODIGO_PRODUCTO", item.codigo_producto)
        ins!!.add("NIVEL", item.nivel)
        ins!!.add("PRECIO", item.precio)
        ins!!.add("UNIDADMEDIDA", item.unidadmedida)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsProdprecio): String? {
        upd!!.init("Prodprecio")
        upd!!.add("CODIGO_PRODUCTO", item.codigo_producto)
        upd!!.add("NIVEL", item.nivel)
        upd!!.add("PRECIO", item.precio)
        upd!!.add("UNIDADMEDIDA", item.unidadmedida)
        upd!!.Where("(CODIGO_PRECIO=" + item.codigo_precio + ")")
        return upd!!.sql()
    }

    //endregion

}