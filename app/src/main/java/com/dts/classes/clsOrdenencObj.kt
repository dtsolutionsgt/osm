package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsOrdenenc

class clsOrdenencObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM Ordenenc"
    var sql: String? = null
    var items = ArrayList<clsOrdenenc>()

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

    fun add(item: clsOrdenenc?) {
        addItem(item!!)
    }

    fun update(item: clsOrdenenc?) {
        updateItem(item!!)
    }

    fun delete(item: clsOrdenenc?) {
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

    fun first(): clsOrdenenc?  {
        return items[0]
    }

    //region Private

    private fun addItem(item: clsOrdenenc) {
        ins!!.init("Ordenenc")
        ins!!.add("idOrden", item.idorden)
        ins!!.add("Numero", item.numero)
        ins!!.add("Fecha", item.fecha.toDouble())
        ins!!.add("idUsuario", item.idusuario)
        ins!!.add("idEstado", item.idestado)
        ins!!.add("idTipo", item.idtipo)
        ins!!.add("idCliContact", item.idclicontact)
        ins!!.add("idDir", item.iddir)
        ins!!.add("idCliente", item.idcliente)
        ins!!.add("descripcion", item.descripcion)
        ins!!.add("fecha_cierre", item.fecha_cierre.toDouble())
        ins!!.add("hora_ini", item.hora_ini.toDouble())
        ins!!.add("hora_fin", item.hora_fin.toDouble())
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsOrdenenc) {
        upd!!.init("Ordenenc")
        upd!!.add("Numero", item.numero)
        upd!!.add("Fecha", item.fecha.toDouble())
        upd!!.add("idUsuario", item.idusuario)
        upd!!.add("idEstado", item.idestado)
        upd!!.add("idTipo", item.idtipo)
        upd!!.add("idCliContact", item.idclicontact)
        upd!!.add("idDir", item.iddir)
        upd!!.add("idCliente", item.idcliente)
        upd!!.add("descripcion", item.descripcion)
        upd!!.add("fecha_cierre", item.fecha_cierre.toDouble())
        upd!!.add("hora_ini", item.hora_ini.toDouble())
        upd!!.add("hora_fin", item.hora_fin.toDouble())
        upd!!.Where("(idOrden=" + item.idorden + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsOrdenenc) {
        sql = "DELETE FROM Ordenenc WHERE (idOrden=" + item.idorden + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM Ordenenc WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsOrdenenc
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsOrdenenc()
            item.idorden = dt.getInt(0)
            item.numero = dt.getString(1)
            item.fecha = dt.getLong(2)
            item.idusuario = dt.getInt(3)
            item.idestado = dt.getInt(4)
            item.idtipo = dt.getInt(5)
            item.idclicontact = dt.getInt(6)
            item.iddir = dt.getInt(7)
            item.idcliente = dt.getInt(8)
            item.descripcion = dt.getString(9)
            item.fecha_cierre = dt.getLong(10)
            item.hora_ini = dt.getLong(11)
            item.hora_fin = dt.getLong(12)
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

    fun addItemSql(item: clsOrdenenc): String? {
        ins!!.init("Ordenenc")
        ins!!.add("idOrden", item.idorden)
        ins!!.add("Numero", item.numero)
        ins!!.add("Fecha", item.fecha.toDouble())
        ins!!.add("idUsuario", item.idusuario)
        ins!!.add("idEstado", item.idestado)
        ins!!.add("idTipo", item.idtipo)
        ins!!.add("idCliContact", item.idclicontact)
        ins!!.add("idDir", item.iddir)
        ins!!.add("idCliente", item.idcliente)
        ins!!.add("descripcion", item.descripcion)
        ins!!.add("fecha_cierre", item.fecha_cierre.toDouble())
        ins!!.add("hora_ini", item.hora_ini.toDouble())
        ins!!.add("hora_fin", item.hora_fin.toDouble())
        return ins!!.sql()
    }

    fun updateItemSql(item: clsOrdenenc): String? {
        upd!!.init("Ordenenc")
        upd!!.add("Numero", item.numero)
        upd!!.add("Fecha", item.fecha.toDouble())
        upd!!.add("idUsuario", item.idusuario)
        upd!!.add("idEstado", item.idestado)
        upd!!.add("idTipo", item.idtipo)
        upd!!.add("idCliContact", item.idclicontact)
        upd!!.add("idDir", item.iddir)
        upd!!.add("idCliente", item.idcliente)
        upd!!.add("descripcion", item.descripcion)
        upd!!.add("fecha_cierre", item.fecha_cierre.toDouble())
        upd!!.add("hora_ini", item.hora_ini.toDouble())
        upd!!.add("hora_fin", item.hora_fin.toDouble())
        upd!!.Where("(idOrden=" + item.idorden + ")")
        return upd!!.sql()
    }

    //endregion


}