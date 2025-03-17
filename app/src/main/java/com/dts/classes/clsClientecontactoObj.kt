package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsClientecontacto


class clsClientecontactoObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Clientecontacto"
    var sql: String? = null
    var items = ArrayList<clsClientecontacto>()

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

    fun add(item: clsClientecontacto?) {
        addItem(item!!)
    }

    fun update(item: clsClientecontacto?) {
        updateItem(item!!)
    }

    fun delete(item: clsClientecontacto?) {
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

    fun first(): clsClientecontacto?  {
        return items[0]
    }

    //region Private

    private fun addItem(item: clsClientecontacto) {
        ins!!.init("Clientecontacto")
        ins!!.add("Codigo_Cliente_Contacto", item.codigo_cliente_contacto)
        ins!!.add("Codigo_Cliente", item.codigo_cliente)
        ins!!.add("Nombre", item.nombre)
        ins!!.add("Telefono", item.telefono)
        ins!!.add("Correo", item.correo)
        ins!!.add("Direccion", item.direccion)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsClientecontacto) {
        upd!!.init("Clientecontacto")
        upd!!.add("Codigo_Cliente", item.codigo_cliente)
        upd!!.add("Nombre", item.nombre)
        upd!!.add("Telefono", item.telefono)
        upd!!.add("Correo", item.correo)
        upd!!.add("Direccion", item.direccion)
        upd!!.Where("(Codigo_Cliente_Contacto=" + item.codigo_cliente_contacto + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsClientecontacto) {
        sql = "DELETE FROM Clientecontacto WHERE (Codigo_Cliente_Contacto=" + item.codigo_cliente_contacto + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Clientecontacto WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsClientecontacto
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsClientecontacto()
            item.codigo_cliente_contacto = dt.getInt(0)
            item.codigo_cliente =""+ dt.getInt(1)
            item.nombre = dt.getString(2)
            item.telefono = dt.getString(3)
            item.correo = dt.getString(4)
            item.direccion = dt.getString(5)
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

    fun addItemSql(item: clsClientecontacto): String? {
        ins!!.init("Clientecontacto")
        ins!!.add("Codigo_Cliente_Contacto", item.codigo_cliente_contacto)
        ins!!.add("Codigo_Cliente", item.codigo_cliente)
        ins!!.add("Nombre", item.nombre)
        ins!!.add("Telefono", item.telefono)
        ins!!.add("Correo", item.correo)
        ins!!.add("Direccion", item.direccion)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsClientecontacto): String? {
        upd!!.init("Clientecontacto")
        upd!!.add("Codigo_Cliente", item.codigo_cliente)
        upd!!.add("Nombre", item.nombre)
        upd!!.add("Telefono", item.telefono)
        upd!!.add("Correo", item.correo)
        upd!!.add("Direccion", item.direccion)
        upd!!.Where("(Codigo_Cliente_Contacto=" + item.codigo_cliente_contacto + ")")
        return upd!!.sql()
    }

    //endregion


}