package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsProducto


class clsProductoObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Producto"
    var sql: String? = null
    var items = ArrayList<clsProducto>()

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

    fun add(item: clsProducto?) {
        addItem(item!!)
    }

    fun update(item: clsProducto?) {
        updateItem(item!!)
    }

    fun delete(item: clsProducto?) {
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

    fun first(): clsProducto?  {
        return items[0]
    }

    //region Private


    private fun addItem(item: clsProducto) {
        ins!!.init("Producto")
        ins!!.add("CODIGO_PRODUCTO", item.codigo_producto)
        ins!!.add("DESCLARGA", item.desclarga)
        ins!!.add("CODIGO_TIPO", item.codigo_tipo)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsProducto) {
        upd!!.init("Producto")
        upd!!.add("DESCLARGA", item.desclarga)
        upd!!.add("CODIGO_TIPO", item.codigo_tipo)
        upd!!.Where("(CODIGO_PRODUCTO=" + item.codigo_producto + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsProducto) {
        sql = "DELETE FROM Producto WHERE (CODIGO_PRODUCTO=" + item.codigo_producto + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Producto WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsProducto
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsProducto()
            item.codigo_producto = dt.getInt(0)
            item.desclarga = dt.getString(1)
            item.codigo_tipo = dt.getString(2)
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

    fun addItemSql(item: clsProducto): String? {
        ins!!.init("Producto")
        ins!!.add("CODIGO_PRODUCTO", item.codigo_producto)
        ins!!.add("DESCLARGA", item.desclarga)
        ins!!.add("CODIGO_TIPO", item.codigo_tipo)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsProducto): String? {
        upd!!.init("Producto")
        upd!!.add("DESCLARGA", item.desclarga)
        upd!!.add("CODIGO_TIPO", item.codigo_tipo)
        upd!!.Where("(CODIGO_PRODUCTO=" + item.codigo_producto + ")")
        return upd!!.sql()
    }

    //endregion


}