package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsExistencia


class clsExistenciaObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Existencia"
    var sql: String? = null
    var items = ArrayList<clsExistencia>()

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

    fun add(item: clsExistencia?) {
        addItem(item!!)
    }

    fun update(item: clsExistencia?) {
        updateItem(item!!)
    }

    fun delete(item: clsExistencia?) {
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

    fun first(): clsExistencia?  {
        return items[0]
    }


    //region Private

    private fun addItem(item: clsExistencia) {
        ins!!.init("Existencia")
        ins!!.add("codigo", item.codigo)
        ins!!.add("nombre", item.nombre)
        ins!!.add("cant", item.cant)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsExistencia) {
        upd!!.init("Existencia")
        upd!!.add("nombre", item.nombre)
        upd!!.add("cant", item.cant)
        upd!!.Where("(codigo=" + item.codigo + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsExistencia) {
        sql = "DELETE FROM Existencia WHERE (codigo=" + item.codigo + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Existencia WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsExistencia
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsExistencia()
            item.codigo = dt.getInt(0)
            item.nombre = dt.getString(1)
            item.cant = dt.getDouble(2)
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

    fun addItemSql(item: clsExistencia): String? {
        ins!!.init("Existencia")
        ins!!.add("codigo", item.codigo)
        ins!!.add("nombre", item.nombre)
        ins!!.add("cant", item.cant)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsExistencia): String? {
        upd!!.init("Existencia")
        upd!!.add("nombre", item.nombre)
        upd!!.add("cant", item.cant)
        upd!!.Where("(codigo=" + item.codigo + ")")
        return upd!!.sql()
    }

    //endregion

}