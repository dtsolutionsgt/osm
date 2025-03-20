package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsCliente


class clsClienteObj {


    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Cliente"
    var sql: String? = null
    var items = ArrayList<clsCliente>()

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

    fun add(item: clsCliente?) {
        addItem(item!!)
    }

    fun update(item: clsCliente?) {
        updateItem(item!!)
    }

    fun delete(item: clsCliente?) {
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

    fun first(): clsCliente?  {
        return items[0]
    }


    //region Private

    private fun addItem(item: clsCliente) {
        ins!!.init("Cliente")
        ins!!.add("Codigo_Cliente", item.codigo_cliente)
        ins!!.add("Nombre", item.nombre)
        ins!!.add("Telefono", item.telefono)
        ins!!.add("Direccion", item.direccion)
        ins!!.add("Nivel", item.nivel)
        ins!!.add("Nit", item.nit)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsCliente) {
        upd!!.init("Cliente")
        upd!!.add("Nombre", item.nombre)
        upd!!.add("Telefono", item.telefono)
        upd!!.add("Direccion", item.direccion)
        upd!!.add("Nivel", item.nivel)
        upd!!.add("Nit", item.nit)
        upd!!.Where("(Codigo_Cliente=" + item.codigo_cliente + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsCliente) {
        sql = "DELETE FROM Cliente WHERE (Codigo_Cliente=" + item.codigo_cliente + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Cliente WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsCliente
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsCliente()
            item.codigo_cliente = dt.getInt(0)
            item.nombre = dt.getString(1)
            item.telefono = dt.getString(2)
            item.direccion = dt.getString(3)
            item.nivel = dt.getInt(4)
            item.nit = dt.getString(5)
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

    fun addItemSql(item: clsCliente): String? {
        ins!!.init("Cliente")
        ins!!.add("Codigo_Cliente", item.codigo_cliente)
        ins!!.add("Nombre", item.nombre)
        ins!!.add("Telefono", item.telefono)
        ins!!.add("Direccion", item.direccion)
        ins!!.add("Nivel", item.nivel)
        ins!!.add("Nit", item.nit)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsCliente): String? {
        upd!!.init("Cliente")
        upd!!.add("Nombre", item.nombre)
        upd!!.add("Telefono", item.telefono)
        upd!!.add("Direccion", item.direccion)
        upd!!.add("Nivel", item.nivel)
        upd!!.add("Nit", item.nit)
        upd!!.Where("(Codigo_Cliente=" + item.codigo_cliente + ")")
        return upd!!.sql()
    }

    //endregion


}