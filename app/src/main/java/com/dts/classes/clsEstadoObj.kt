package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.BaseDatos.Update
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsEstado


public class clsEstadoObj {

    var count = 0

    private var cont: Context? = null
    private var Con: BaseDatos? = null
    private var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: Update? = null
    private val clsCls = clsClasses()

    private val sel = "SELECT * FROM Estado"
    private var sql: String? = null
    var items = ArrayList<clsEstado>()

    constructor(context: Context, dbconnection: BaseDatos, dbase: SQLiteDatabase) {
        cont = context
        Con = dbconnection
        ins = Con!!.Ins
        upd = Con!!.Upd
        db = dbase
        count = 0
    }

    fun reconnect(dbconnection: BaseDatos?, dbase: SQLiteDatabase?) {
        Con = dbconnection
        ins = Con!!.Ins
        upd = Con!!.Upd
        db = dbase
    }

    fun add(item: clsEstado) {
        addItem(item)
    }

    fun update(item: clsEstado) {
        updateItem(item)
    }

    fun delete(item: clsEstado) {
        deleteItem(item)
    }

    fun delete(id: Int) {
        deleteItem(id)
    }

    fun fill() {
        fillItems(sel)
    }

    fun fill(specstr: String) {
        fillItems("$sel $specstr")
    }

    fun fillSelect(sq: String) {
        fillItems(sq)
    }

    fun first(): clsEstado? {
        return items[0]
    }


    //region Private

    private fun addItem(item: clsEstado) {
        ins!!.init("Estado")
        ins!!.add("codigo_ticket_estado", item?.codigo_ticket_estado!!)
        ins!!.add("nombre", item.nombre)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsEstado) {
        upd!!.init("Estado")
        upd!!.add("nombre", item.nombre)
        upd!!.Where("(codigo_ticket_estado=" + item.codigo_ticket_estado + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsEstado) {
        sql = "DELETE FROM Estado WHERE (codigo_ticket_estado=" + item.codigo_ticket_estado + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Estado WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsEstado
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsClasses.clsEstado()
            item.codigo_ticket_estado = dt.getInt(0)
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

    fun addItemSql(item: clsEstado): String? {
        ins!!.init("Estado")
        ins!!.add("codigo_ticket_estado", item.codigo_ticket_estado)
        ins!!.add("nombre", item.nombre)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsEstado): String? {
        upd!!.init("Estado")
        upd!!.add("nombre", item.nombre)
        upd!!.Where("(codigo_ticket_estado=" + item.codigo_ticket_estado + ")")
        return upd!!.sql()
    }

    //endregion

}
