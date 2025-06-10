package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsClasificacion


class clsClasificacionObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Clasificacion"
    var sql: String? = null
    var items = ArrayList<clsClasificacion>()

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

    fun add(item: clsClasificacion?) {
        addItem(item!!)
    }

    fun update(item: clsClasificacion?) {
        updateItem(item!!)
    }

    fun delete(item: clsClasificacion?) {
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

    fun first(): clsClasificacion?  {
        return items[0]
    }


    //region Private

    private fun addItem(item: clsClasificacion) {
        ins!!.init("Clasificacion")
        ins!!.add("CODIGO_CLASIFICACION", item.codigo_clasificacion)
        ins!!.add("DESCRIPCION", item.descripcion)
        ins!!.add("ES_MATERIAL", item.es_material)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsClasificacion) {
        upd!!.init("Clasificacion")
        upd!!.add("DESCRIPCION", item.descripcion)
        upd!!.add("ES_MATERIAL", item.es_material)
        upd!!.Where("(CODIGO_CLASIFICACION=" + item.codigo_clasificacion + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsClasificacion) {
        sql =
            "DELETE FROM Clasificacion WHERE (CODIGO_CLASIFICACION=" + item.codigo_clasificacion + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Clasificacion WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsClasificacion
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsClasificacion()
            item.codigo_clasificacion = dt.getInt(0)
            item.descripcion = dt.getString(1)
            item.es_material = dt.getInt(2)
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

    fun addItemSql(item: clsClasificacion): String? {
        ins!!.init("Clasificacion")
        ins!!.add("CODIGO_CLASIFICACION", item.codigo_clasificacion)
        ins!!.add("DESCRIPCION", item.descripcion)
        ins!!.add("ES_MATERIAL", item.es_material)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsClasificacion): String? {
        upd!!.init("Clasificacion")
        upd!!.add("DESCRIPCION", item.descripcion)
        upd!!.add("ES_MATERIAL", item.es_material)
        upd!!.Where("(CODIGO_CLASIFICACION=" + item.codigo_clasificacion + ")")
        return upd!!.sql()
    }

    //endregion


}