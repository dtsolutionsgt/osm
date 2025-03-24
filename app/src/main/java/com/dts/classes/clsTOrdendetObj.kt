package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsT_ordendet


class clsTOrdendetObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM T_ordendet"
    var sql: String? = null
    var items = ArrayList<clsT_ordendet>()

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

    fun add(item: clsT_ordendet?) {
        addItem(item!!)
    }

    fun update(item: clsT_ordendet?) {
        updateItem(item!!)
    }

    fun delete(item: clsT_ordendet?) {
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

    fun first(): clsT_ordendet?  {
        return items[0]
    }


    //region Private

    private fun addItem(item: clsT_ordendet) {
        ins!!.init("T_ordendet")
        ins!!.add("CODIGO_ORDEN_SERVICIO_DET", item.codigo_orden_servicio_det)
        ins!!.add("CODIGO_ORDEN_SERVICIO", item.codigo_orden_servicio)
        ins!!.add("CODIGO_PRODUCTO", item.codigo_producto)
        ins!!.add("DESCRIPCION", item.descripcion)
        ins!!.add("PRECIO", item.precio)
        ins!!.add("REALIZADO", item.realizado)
        ins!!.add("CANTIDAD", item.cantidad)
        ins!!.add("TOTAL", item.total)
        ins!!.add("ACTIVO", item.activo)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsT_ordendet) {
        upd!!.init("T_ordendet")
        upd!!.add("CODIGO_ORDEN_SERVICIO", item.codigo_orden_servicio)
        upd!!.add("CODIGO_PRODUCTO", item.codigo_producto)
        upd!!.add("DESCRIPCION", item.descripcion)
        upd!!.add("PRECIO", item.precio)
        upd!!.add("REALIZADO", item.realizado)
        upd!!.add("CANTIDAD", item.cantidad)
        upd!!.add("TOTAL", item.total)
        upd!!.add("ACTIVO", item.activo)
        upd!!.Where("(CODIGO_ORDEN_SERVICIO_DET=" + item.codigo_orden_servicio_det + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsT_ordendet) {
        sql =
            "DELETE FROM T_ordendet WHERE (CODIGO_ORDEN_SERVICIO_DET=" + item.codigo_orden_servicio_det + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM T_ordendet WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsT_ordendet
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsT_ordendet()
            item.codigo_orden_servicio_det = dt.getInt(0)
            item.codigo_orden_servicio = dt.getInt(1)
            item.codigo_producto = dt.getInt(2)
            item.descripcion = dt.getString(3)
            item.precio = dt.getDouble(4)
            item.realizado = dt.getInt(5)
            item.cantidad = dt.getDouble(6)
            item.total = dt.getDouble(7)
            item.activo = dt.getInt(8)
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

    fun addItemSql(item: clsT_ordendet): String? {
        ins!!.init("T_ordendet")
        ins!!.add("CODIGO_ORDEN_SERVICIO_DET", item.codigo_orden_servicio_det)
        ins!!.add("CODIGO_ORDEN_SERVICIO", item.codigo_orden_servicio)
        ins!!.add("CODIGO_PRODUCTO", item.codigo_producto)
        ins!!.add("DESCRIPCION", item.descripcion)
        ins!!.add("PRECIO", item.precio)
        ins!!.add("REALIZADO", item.realizado)
        ins!!.add("CANTIDAD", item.cantidad)
        ins!!.add("TOTAL", item.total)
        ins!!.add("ACTIVO", item.activo)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsT_ordendet): String? {
        upd!!.init("T_ordendet")
        upd!!.add("CODIGO_ORDEN_SERVICIO", item.codigo_orden_servicio)
        upd!!.add("CODIGO_PRODUCTO", item.codigo_producto)
        upd!!.add("DESCRIPCION", item.descripcion)
        upd!!.add("PRECIO", item.precio)
        upd!!.add("REALIZADO", item.realizado)
        upd!!.add("CANTIDAD", item.cantidad)
        upd!!.add("TOTAL", item.total)
        upd!!.add("ACTIVO", item.activo)
        upd!!.Where("(CODIGO_ORDEN_SERVICIO_DET=" + item.codigo_orden_servicio_det + ")")
        return upd!!.sql()
    }

    //endregion


}