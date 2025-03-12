package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsParam


class clsParamObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Param"
    var sql: String? = null
    var items = ArrayList<clsParam>()

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

    fun add(item: clsParam?) {
        addItem(item!!)
    }

    fun update(item: clsParam?) {
        updateItem(item!!)
    }

    fun delete(item: clsParam?) {
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

    fun first(): clsParam?  {
        return items[0]
    }

    //region Private

    private fun addItem(item: clsParam) {
        ins!!.init("Param")
        ins!!.add("codigo", item.codigo)
        ins!!.add("empresa", item.empresa)
        ins!!.add("id", item.id)
        ins!!.add("userid", item.userid)
        ins!!.add("nombre", item.nombre)
        ins!!.add("valor", item.valor)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsParam) {
        upd!!.init("Param")
        upd!!.add("empresa", item.empresa)
        upd!!.add("id", item.id)
        upd!!.add("userid", item.userid)
        upd!!.add("nombre", item.nombre)
        upd!!.add("valor", item.valor)
        upd!!.Where("(codigo=" + item.codigo + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsParam) {
        sql = "DELETE FROM Param WHERE (codigo=" + item.codigo + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Param WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsParam
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsParam()
            item.codigo = dt.getInt(0)
            item.empresa = dt.getInt(1)
            item.id = dt.getInt(2)
            item.userid = dt.getInt(3)
            item.nombre = dt.getString(4)
            item.valor = dt.getString(5)
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

    fun addItemSql(item: clsParam): String? {
        ins!!.init("Param")
        ins!!.add("codigo", item.codigo)
        ins!!.add("empresa", item.empresa)
        ins!!.add("id", item.id)
        ins!!.add("userid", item.userid)
        ins!!.add("nombre", item.nombre)
        ins!!.add("valor", item.valor)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsParam): String? {
        upd!!.init("Param")
        upd!!.add("empresa", item.empresa)
        upd!!.add("id", item.id)
        upd!!.add("userid", item.userid)
        upd!!.add("nombre", item.nombre)
        upd!!.add("valor", item.valor)
        upd!!.Where("(codigo=" + item.codigo + ")")
        return upd!!.sql()
    }

    //endregion


}

