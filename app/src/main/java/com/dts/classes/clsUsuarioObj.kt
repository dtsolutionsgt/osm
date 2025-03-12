package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsUsuario


class clsUsuarioObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Usuario"
    var sql: String? = null
    var items = ArrayList<clsUsuario>()

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

    fun add(item: clsUsuario?) {
        addItem(item!!)
    }

    fun update(item: clsUsuario?) {
        updateItem(item!!)
    }

    fun delete(item: clsUsuario?) {
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

    fun first(): clsUsuario?  {
        return items[0]
    }

//region Private

    //region Private
    private fun addItem(item: clsUsuario) {
        ins!!.init("Usuario")
        ins!!.add("id", item.id)
        ins!!.add("nombre", item.nombre)
        ins!!.add("clave", item.clave)
        ins!!.add("activo", item.activo)
        ins!!.add("rol", item.rol)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsUsuario) {
        upd!!.init("Usuario")
        upd!!.add("nombre", item.nombre)
        upd!!.add("clave", item.clave)
        upd!!.add("activo", item.activo)
        upd!!.add("rol", item.rol)
        upd!!.Where("(id=" + item.id + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsUsuario) {
        sql = "DELETE FROM Usuario WHERE (id=" + item.id + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Usuario WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsUsuario
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsUsuario()
            item.id = dt.getInt(0)
            item.nombre = dt.getString(1)
            item.clave = dt.getString(2)
            item.activo = dt.getInt(3)
            item.rol = dt.getInt(4)
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

    fun addItemSql(item: clsUsuario): String? {
        ins!!.init("Usuario")
        ins!!.add("id", item.id)
        ins!!.add("nombre", item.nombre)
        ins!!.add("clave", item.clave)
        ins!!.add("activo", item.activo)
        ins!!.add("rol", item.rol)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsUsuario): String? {
        upd!!.init("Usuario")
        upd!!.add("nombre", item.nombre)
        upd!!.add("clave", item.clave)
        upd!!.add("activo", item.activo)
        upd!!.add("rol", item.rol)
        upd!!.Where("(id=" + item.id + ")")
        return upd!!.sql()
    }

    //endregion


}