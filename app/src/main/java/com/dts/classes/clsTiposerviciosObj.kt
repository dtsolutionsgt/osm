package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsTiposervicios


class clsTiposerviciosObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Tiposervicios"
    var sql: String? = null
    var items = ArrayList<clsClasses.clsTiposervicios>()

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

    fun add(item: clsTiposervicios?) {
        addItem(item!!)
    }

    fun update(item: clsTiposervicios?) {
        updateItem(item!!)
    }

    fun delete(item: clsTiposervicios?) {
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

    fun first(): clsTiposervicios?  {
        return items[0]
    }


    //region Private

    private fun addItem(item: clsTiposervicios) {
        ins!!.init("Tiposervicios")
        ins!!.add("codigo_tipo_departamento", item.codigo_tipo_departamento)
        ins!!.add("codigo_ticket_departamento", item.codigo_ticket_departamento)
        ins!!.add("nombre", item.nombre)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsTiposervicios) {
        upd!!.init("Tiposervicios")
        upd!!.add("codigo_tipo_departamento", item.codigo_tipo_departamento)
        upd!!.add("nombre", item.nombre)
        upd!!.Where("(codigo_ticket_departamento=" + item.codigo_ticket_departamento + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsTiposervicios) {
        sql =
            "DELETE FROM Tiposervicios WHERE (codigo_ticket_departamento=" + item.codigo_ticket_departamento + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Tiposervicios WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsTiposervicios
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsTiposervicios()
            item.codigo_tipo_departamento = dt.getInt(0)
            item.codigo_ticket_departamento = dt.getInt(1)
            item.nombre = dt.getString(2)
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

    fun addItemSql(item: clsTiposervicios): String? {
        ins!!.init("Tiposervicios")
        ins!!.add("codigo_tipo_departamento", item.codigo_tipo_departamento)
        ins!!.add("codigo_ticket_departamento", item.codigo_ticket_departamento)
        ins!!.add("nombre", item.nombre)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsTiposervicios): String? {
        upd!!.init("Tiposervicios")
        upd!!.add("codigo_tipo_departamento", item.codigo_tipo_departamento)
        upd!!.add("nombre", item.nombre)
        upd!!.Where("(codigo_ticket_departamento=" + item.codigo_ticket_departamento + ")")
        return upd!!.sql()
    }

    //endregion


}