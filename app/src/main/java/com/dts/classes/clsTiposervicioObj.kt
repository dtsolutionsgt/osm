package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsTiposervicio


class clsTiposervicioObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Tiposervicio"
    var sql: String? = null
    var items = ArrayList<clsTiposervicio>()

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

    fun add(item: clsTiposervicio?) {
        addItem(item!!)
    }

    fun update(item: clsTiposervicio?) {
        updateItem(item!!)
    }

    fun delete(item: clsTiposervicio?) {
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

    fun first(): clsTiposervicio?  {
        return items[0]
    }

    //region Private

    private fun addItem(item: clsTiposervicio) {
        ins!!.init("Tiposervicio")
        ins!!.add("CODIGO_TIPO_ORDEN_SERVICIO", item.codigo_tipo_orden_servicio)
        ins!!.add("NOMBRE", item.nombre)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsTiposervicio) {
        upd!!.init("Tiposervicio")
        upd!!.add("NOMBRE", item.nombre)
        upd!!.Where("(CODIGO_TIPO_ORDEN_SERVICIO=" + item.codigo_tipo_orden_servicio + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsTiposervicio) {
        sql =
            "DELETE FROM Tiposervicio WHERE (CODIGO_TIPO_ORDEN_SERVICIO=" + item.codigo_tipo_orden_servicio + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Tiposervicio WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsTiposervicio
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsTiposervicio()
            item.codigo_tipo_orden_servicio = dt.getInt(0)
            item.nombre = dt.getString(1)
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

    fun addItemSql(item: clsTiposervicio): String? {
        ins!!.init("Tiposervicio")
        ins!!.add("CODIGO_TIPO_ORDEN_SERVICIO", item.codigo_tipo_orden_servicio)
        ins!!.add("NOMBRE", item.nombre)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsTiposervicio): String? {
        upd!!.init("Tiposervicio")
        upd!!.add("NOMBRE", item.nombre)
        upd!!.Where("(CODIGO_TIPO_ORDEN_SERVICIO=" + item.codigo_tipo_orden_servicio + ")")
        return upd!!.sql()
    }

    //endregion

}