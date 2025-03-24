package com.dts.classes

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dts.base.BaseDatos
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsOrdenenc
import com.dts.base.clsClasses.clsT_ordenenc


class clsTOrdenencObj {

    var count=0

    var cont: Context? = null
    var Con: BaseDatos? = null
    var db: SQLiteDatabase? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    val clsCls = clsClasses()

    val sel ="SELECT * FROM T_Ordenenc"
    var sql: String? = null
    var items = ArrayList<clsT_ordenenc>()

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

    fun add(item: clsT_ordenenc?) {
        addItem(item!!)
    }

    fun update(item: clsT_ordenenc?) {
        updateItem(item!!)
    }

    fun delete(item: clsT_ordenenc?) {
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

    fun first(): clsT_ordenenc?  {
        return items[0]
    }

    //region Private

    private fun addItem(item: clsT_ordenenc) {
        ins!!.init("T_ordenenc")
        ins!!.add("CODIGO_ORDEN_SERVICIO", item.codigo_orden_servicio)
        ins!!.add("NUMERO", item.numero)
        ins!!.add("CODIGO_CLIENTE", item.codigo_cliente)
        ins!!.add("CODIGO_SUCURSAL", item.codigo_sucursal)
        ins!!.add("CODIGO_EMPRESA", item.codigo_empresa)
        ins!!.add("CODIGO_TIPO_ORDEN_SERVICIO", item.codigo_tipo_orden_servicio)
        ins!!.add("CODIGO_ESTADO_ORDEN_SERVICIO", item.codigo_estado_orden_servicio)
        ins!!.add("CODIGO_CLIENTE_CONTACTO", item.codigo_cliente_contacto)
        ins!!.add("CODIGO_DIRECCION", item.codigo_direccion)
        ins!!.add("CODIGO_MONEDA", item.codigo_moneda)
        ins!!.add("TOTAL", item.total)
        ins!!.add("ANULADA", item.anulada)
        ins!!.add("ACTIVA", item.activa)
        ins!!.add("CERRADA", item.cerrada)
        ins!!.add("DESCRIPCION", item.descripcion)
        ins!!.add("OBSERVACION", item.observacion)
        db!!.execSQL(ins!!.sql())
    }

    private fun updateItem(item: clsT_ordenenc) {
        upd!!.init("T_ordenenc")
        upd!!.add("NUMERO", item.numero)
        upd!!.add("CODIGO_CLIENTE", item.codigo_cliente)
        upd!!.add("CODIGO_SUCURSAL", item.codigo_sucursal)
        upd!!.add("CODIGO_EMPRESA", item.codigo_empresa)
        upd!!.add("CODIGO_TIPO_ORDEN_SERVICIO", item.codigo_tipo_orden_servicio)
        upd!!.add("CODIGO_ESTADO_ORDEN_SERVICIO", item.codigo_estado_orden_servicio)
        upd!!.add("CODIGO_CLIENTE_CONTACTO", item.codigo_cliente_contacto)
        upd!!.add("CODIGO_DIRECCION", item.codigo_direccion)
        upd!!.add("CODIGO_MONEDA", item.codigo_moneda)
        upd!!.add("TOTAL", item.total)
        upd!!.add("ANULADA", item.anulada)
        upd!!.add("ACTIVA", item.activa)
        upd!!.add("CERRADA", item.cerrada)
        upd!!.add("DESCRIPCION", item.descripcion)
        upd!!.add("OBSERVACION", item.observacion)
        upd!!.Where("(CODIGO_ORDEN_SERVICIO=" + item.codigo_orden_servicio + ")")
        db!!.execSQL(upd!!.sql())
    }

    private fun deleteItem(item: clsT_ordenenc) {
        sql =
            "DELETE FROM T_ordenenc WHERE (CODIGO_ORDEN_SERVICIO=" + item.codigo_orden_servicio + ")"
        db!!.execSQL(sql)
    }

    private fun deleteItem(id: Int) {
        sql = "DELETE FROM T_ordenenc WHERE id=$id"
        db!!.execSQL(sql)
    }

    private fun fillItems(sq: String) {
        val dt: Cursor
        var item: clsT_ordenenc
        items.clear()
        dt = Con!!.OpenDT(sq)
        count = dt.count
        if (dt.count > 0) dt.moveToFirst()
        while (!dt.isAfterLast) {
            item = clsT_ordenenc()
            item.codigo_orden_servicio = dt.getInt(0)
            item.numero = dt.getString(1)
            item.codigo_cliente = dt.getInt(2)
            item.codigo_sucursal = dt.getInt(3)
            item.codigo_empresa = dt.getInt(4)
            item.codigo_tipo_orden_servicio = dt.getInt(5)
            item.codigo_estado_orden_servicio = dt.getInt(6)
            item.codigo_cliente_contacto = dt.getInt(7)
            item.codigo_direccion = dt.getInt(8)
            item.codigo_moneda = dt.getInt(9)
            item.total = dt.getDouble(10)
            item.anulada = dt.getInt(11)
            item.activa = dt.getInt(12)
            item.cerrada = dt.getInt(13)
            item.descripcion = dt.getString(14)
            item.observacion = dt.getString(15)
            items?.add(item!!)
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

    fun addItemSql(item: clsT_ordenenc): String? {
        ins!!.init("T_ordenenc")
        ins!!.add("CODIGO_ORDEN_SERVICIO", item.codigo_orden_servicio)
        ins!!.add("NUMERO", item.numero)
        ins!!.add("CODIGO_CLIENTE", item.codigo_cliente)
        ins!!.add("CODIGO_SUCURSAL", item.codigo_sucursal)
        ins!!.add("CODIGO_EMPRESA", item.codigo_empresa)
        ins!!.add("CODIGO_TIPO_ORDEN_SERVICIO", item.codigo_tipo_orden_servicio)
        ins!!.add("CODIGO_ESTADO_ORDEN_SERVICIO", item.codigo_estado_orden_servicio)
        ins!!.add("CODIGO_CLIENTE_CONTACTO", item.codigo_cliente_contacto)
        ins!!.add("CODIGO_DIRECCION", item.codigo_direccion)
        ins!!.add("CODIGO_MONEDA", item.codigo_moneda)
        ins!!.add("TOTAL", item.total)
        ins!!.add("ANULADA", item.anulada)
        ins!!.add("ACTIVA", item.activa)
        ins!!.add("CERRADA", item.cerrada)
        ins!!.add("DESCRIPCION", item.descripcion)
        ins!!.add("OBSERVACION", item.observacion)
        return ins!!.sql()
    }

    fun updateItemSql(item: clsT_ordenenc): String? {
        upd!!.init("T_ordenenc")
        upd!!.add("NUMERO", item.numero)
        upd!!.add("CODIGO_CLIENTE", item.codigo_cliente)
        upd!!.add("CODIGO_SUCURSAL", item.codigo_sucursal)
        upd!!.add("CODIGO_EMPRESA", item.codigo_empresa)
        upd!!.add("CODIGO_TIPO_ORDEN_SERVICIO", item.codigo_tipo_orden_servicio)
        upd!!.add("CODIGO_ESTADO_ORDEN_SERVICIO", item.codigo_estado_orden_servicio)
        upd!!.add("CODIGO_CLIENTE_CONTACTO", item.codigo_cliente_contacto)
        upd!!.add("CODIGO_DIRECCION", item.codigo_direccion)
        upd!!.add("CODIGO_MONEDA", item.codigo_moneda)
        upd!!.add("TOTAL", item.total)
        upd!!.add("ANULADA", item.anulada)
        upd!!.add("ACTIVA", item.activa)
        upd!!.add("CERRADA", item.cerrada)
        upd!!.add("DESCRIPCION", item.descripcion)
        upd!!.add("OBSERVACION", item.observacion)
        upd!!.Where("(CODIGO_ORDEN_SERVICIO=" + item.codigo_orden_servicio + ")")
        return upd!!.sql()
    }

    //endregion


}