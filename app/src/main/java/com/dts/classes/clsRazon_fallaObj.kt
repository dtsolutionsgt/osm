package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsRazon_falla


class clsRazon_fallaObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Razon_falla"
    var sql: String? = null
    var items = ArrayList<clsRazon_falla>()

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

    fun add(item: clsRazon_falla?) {
        addItem(item!!)
    }

    fun update(item: clsRazon_falla?) {
        updateItem(item!!)
    }

    fun delete(item: clsRazon_falla?) {
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

    fun first(): clsRazon_falla?  {
        return items[0]
    }


    //region Private

    private fun addItem(item: clsRazon_falla) {
        ins!!.init("Razon_falla")
        ins!!.add("CODIGO_RAZON_FALLA", item.codigo_razon_falla)
        ins!!.add("CODIGO_TIPO_ORDEN_SERVICI", item.codigo_tipo_orden_servici)
        ins!!.add("CODIGO_CLASIFICACION", item.codigo_clasificacion)
        ins!!.add("DESCRIPCION", item.descripcion)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsRazon_falla) {
        upd!!.init("Razon_falla")
        upd!!.add("CODIGO_TIPO_ORDEN_SERVICI", item.codigo_tipo_orden_servici)
        upd!!.add("CODIGO_CLASIFICACION", item.codigo_clasificacion)
        upd!!.add("DESCRIPCION", item.descripcion)
        upd!!.Where("(CODIGO_RAZON_FALLA=" + item.codigo_razon_falla + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsRazon_falla) {
        sql = "DELETE FROM Razon_falla WHERE (CODIGO_RAZON_FALLA=" + item.codigo_razon_falla + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Razon_falla WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsRazon_falla
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsRazon_falla()
            item.codigo_razon_falla = dt.getInt(0)
            item.codigo_tipo_orden_servici = dt.getInt(1)
            item.codigo_clasificacion = dt.getInt(2)
            item.descripcion = dt.getString(3)
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

    fun addItemSql(item: clsRazon_falla): String? {
        ins!!.init("Razon_falla")
        ins!!.add("CODIGO_RAZON_FALLA", item.codigo_razon_falla)
        ins!!.add("CODIGO_TIPO_ORDEN_SERVICI", item.codigo_tipo_orden_servici)
        ins!!.add("CODIGO_CLASIFICACION", item.codigo_clasificacion)
        ins!!.add("DESCRIPCION", item.descripcion)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsRazon_falla): String? {
        upd!!.init("Razon_falla")
        upd!!.add("CODIGO_TIPO_ORDEN_SERVICI", item.codigo_tipo_orden_servici)
        upd!!.add("CODIGO_CLASIFICACION", item.codigo_clasificacion)
        upd!!.add("DESCRIPCION", item.descripcion)
        upd!!.Where("(CODIGO_RAZON_FALLA=" + item.codigo_razon_falla + ")")
        return upd!!.sql()
    }

    //endregion


}