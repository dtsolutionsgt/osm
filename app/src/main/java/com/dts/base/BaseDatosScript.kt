package com.dts.base

import android.app.AlertDialog
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.dts.osm.R


class BaseDatosScript(private val vcontext: Context) {

    fun scriptDatabase(database: SQLiteDatabase) {
        try {
            scriptTablas(database)
        } catch (e: SQLiteException) {
            msgbox(e.message)
        }
    }

    private fun scriptTablas(db: SQLiteDatabase) {
        var sql: String

        try {

            //region Catalogos


            sql="CREATE TABLE [Clasificacion] ("+
                    "CODIGO_CLASIFICACION INTEGER NOT NULL,"+
                    "DESCRIPCION TEXT NOT NULL,"+
                    "ES_MATERIAL INTEGER NOT NULL,"+
                    "PRIMARY KEY ([CODIGO_CLASIFICACION])"+
                    ");";
            db?.execSQL(sql);

            sql="CREATE TABLE [Cliente] ("+
                    "Codigo_Cliente INTEGER NOT NULL,"+
                    "Nombre TEXT NOT NULL,"+
                    "Telefono TEXT NOT NULL,"+
                    "Direccion TEXT NOT NULL,"+
                    "Nivel INTEGER NOT NULL,"+
                    "Nit TEXT NOT NULL,"+
                    "PRIMARY KEY ([Codigo_Cliente])"+
                    ");";
            db?.execSQL(sql);

            sql="CREATE TABLE [Clientecontacto] ("+
                    "Codigo_Cliente_Contacto INTEGER NOT NULL,"+
                    "Codigo_Cliente INTEGER NOT NULL,"+
                    "Nombre TEXT NOT NULL,"+
                    "Telefono TEXT NOT NULL,"+
                    "Correo TEXT NOT NULL,"+
                    "Direccion TEXT NOT NULL,"+
                    "PRIMARY KEY ([Codigo_Cliente_Contacto])"+
                    ");";
            db?.execSQL(sql);
            sql="CREATE INDEX Clientecontacto_idx1 ON Clientecontacto(Codigo_Cliente)";db?.execSQL(sql)

            sql="CREATE TABLE [Clientedir] ("+
                    "Codigo_Direccion INTEGER NOT NULL,"+
                    "Codigo_Cliente INTEGER NOT NULL,"+
                    "Direccion TEXT NOT NULL,"+
                    "Telefono TEXT NOT NULL,"+
                    "Referencia TEXT NOT NULL,"+
                    "PRIMARY KEY ([Codigo_Direccion])"+
                    ");";
            db?.execSQL(sql);
            sql="CREATE INDEX Clientedir_idx1 ON Clientedir(Codigo_Cliente)";db?.execSQL(sql)


            sql = "CREATE TABLE [Envioimagen] (" +
                    "id TEXT NOT NULL," +
                    "tipo INTEGER NOT NULL," +
                    "PRIMARY KEY ([id])" +
                    ");";
            db?.execSQL(sql);

            sql="CREATE TABLE [Estado] ("+
                    "codigo_ticket_estado INTEGER NOT NULL,"+
                    "nombre TEXT NOT NULL,"+
                    "PRIMARY KEY ([codigo_ticket_estado])"+
                    ");";
            db?.execSQL(sql);

            sql="CREATE TABLE [Existencia] ("+
                    "codigo INTEGER NOT NULL,"+
                    "nombre TEXT NOT NULL,"+
                    "cant REAL NOT NULL,"+
                    "PRIMARY KEY ([codigo])"+
                    ");";
            db?.execSQL(sql);



            sql="CREATE TABLE [Prodprecio] ("+
                    "CODIGO_PRECIO INTEGER NOT NULL,"+
                    "CODIGO_PRODUCTO INTEGER NOT NULL,"+
                    "NIVEL INTEGER NOT NULL,"+
                    "PRECIO REAL NOT NULL,"+
                    "UNIDADMEDIDA TEXT NOT NULL,"+
                    "PRIMARY KEY ([CODIGO_PRECIO])"+
                    ");";
            db?.execSQL(sql);

            sql="CREATE TABLE [Producto] ("+
                    "CODIGO_PRODUCTO INTEGER NOT NULL,"+
                    "DESCLARGA TEXT NOT NULL,"+
                    "CODIGO_TIPO TEXT NOT NULL,"+
                    "PRIMARY KEY ([CODIGO_PRODUCTO])"+
                    ");";
            db?.execSQL(sql);

            sql="CREATE TABLE [Razon_falla] ("+
                    "CODIGO_RAZON_FALLA INTEGER NOT NULL,"+
                    "CODIGO_TIPO_ORDEN_SERVICI INTEGER NOT NULL,"+
                    "CODIGO_CLASIFICACION INTEGER NOT NULL,"+
                    "DESCRIPCION TEXT NOT NULL,"+
                    "PRIMARY KEY ([CODIGO_RAZON_FALLA])"+
                    ");";
            db?.execSQL(sql);


            sql="CREATE TABLE [Razon_no_atencion] ("+
                    "CODIGO_RAZON_NOATENCION INTEGER NOT NULL,"+
                    "EMPRESA INTEGER NOT NULL,"+
                    "DESCRIPCION TEXT NOT NULL,"+
                    "PRIMARY KEY ([CODIGO_RAZON_NOATENCION])"+
                    ");";
            db?.execSQL(sql);

            sql = "CREATE TABLE [Savepos] (" +
                    "id INTEGER NOT NULL," +
                    "valor TEXT NOT NULL," +
                    "PRIMARY KEY ([id])" +
                    ");";
            db.execSQL(sql);

            sql="CREATE TABLE [Tiposervicio] ("+
                    "CODIGO_TIPO_ORDEN_SERVICIO INTEGER NOT NULL,"+
                    "NOMBRE TEXT NOT NULL,"+
                    "PRIMARY KEY ([CODIGO_TIPO_ORDEN_SERVICIO])"+
                    ");";
            db?.execSQL(sql);


            sql="CREATE TABLE [Usuario] ("+
                    "id INTEGER NOT NULL,"+
                    "nombre TEXT NOT NULL,"+
                    "pin INTEGER NOT NULL,"+
                    "rol TEXT NOT NULL,"+
                    "PRIMARY KEY ([id])"+
                    ");";
            db?.execSQL(sql);

            sql="CREATE TABLE [Updsave] ("+
                    "id INTEGER NOT NULL,"+
                    "cmd TEXT NOT NULL,"+
                    "PRIMARY KEY ([id])"+
                    ");";
            db?.execSQL(sql);

            //endregion

            //region Orden

            sql="CREATE TABLE [Ordendet] ("+
                    "id INTEGER NOT NULL,"+
                    "idOrden INTEGER NOT NULL,"+
                    "idProducto INTEGER NOT NULL,"+
                    "Descripcion TEXT NOT NULL,"+
                    "idrazonfalla INTEGER NOT NULL,"+
                    "Precio REAL NOT NULL,"+
                    "Cant REAL NOT NULL,"+
                    "Total REAL NOT NULL,"+
                    "Activo INTEGER NOT NULL,"+
                    "Realizado INTEGER NOT NULL,"+
                    "idnoaten INTEGER NOT NULL,"+
                    "horaini INTEGER NOT NULL,"+
                    "horafin INTEGER NOT NULL,"+
                    "serial TEXT NOT NULL,"+
                    "PRIMARY KEY ([id])"+
                    ");";
            db?.execSQL(sql);



            sql="CREATE TABLE [Ordenenc] ("+
                    "idOrden INTEGER NOT NULL,"+
                    "Numero TEXT NOT NULL,"+
                    "Fecha INTEGER NOT NULL,"+
                    "idUsuario INTEGER NOT NULL,"+
                    "idEstado INTEGER NOT NULL,"+
                    "idTipo INTEGER NOT NULL,"+
                    "idCliContact INTEGER NOT NULL,"+
                    "idDir INTEGER NOT NULL,"+
                    "idCliente INTEGER NOT NULL,"+
                    "descripcion TEXT NOT NULL,"+
                    "fecha_cierre INTEGER NOT NULL,"+
                    "hora_ini INTEGER NOT NULL,"+
                    "hora_fin INTEGER NOT NULL,"+
                    "prioridad INTEGER NOT NULL,"+
                    "PRIMARY KEY ([idOrden])"+
                    ");";
            db?.execSQL(sql);

            sql="CREATE INDEX Ordenenc_idx1 ON Ordenenc(Fecha)";db?.execSQL(sql)
            sql="CREATE INDEX Ordenenc_idx2 ON Ordenenc(idEstado)";db?.execSQL(sql)

            sql = "CREATE TABLE [Ordenenccap] (" +
                    "idOrden INTEGER NOT NULL," +
                    "Anulada INTEGER NOT NULL," +
                    "Activa INTEGER NOT NULL," +
                    "Cerrada INTEGER NOT NULL," +
                    "FirmaUsuario TEXT NOT NULL," +
                    "FirmaCliente TEXT NOT NULL," +
                    "Latit REAL NOT NULL," +
                    "Longit REAL NOT NULL," +
                    "FechaIni INTEGER NOT NULL," +
                    "FechaFin INTEGER NOT NULL," +
                    "Nota TEXT NOT NULL," +
                    "Recibido INTEGER NOT NULL," +
                    "PRIMARY KEY ([idOrden])" +
                    ");";
            db?.execSQL(sql);

            sql = "CREATE TABLE [Ordenfoto] (" +
                    "id INTEGER NOT NULL," +
                    "idOrden INTEGER NOT NULL," +
                    "nombre TEXT NOT NULL," +
                    "nota TEXT NOT NULL," +
                    "statcom INTEGER NOT NULL," +
                    "PRIMARY KEY ([id])" +
                    ");";
            db?.execSQL(sql);

            sql = "CREATE INDEX Ordenfoto_idx1 ON Ordenfoto(idOrden)";db?.execSQL(sql)
            sql = "CREATE INDEX Ordenfoto_idx2 ON Ordenfoto(statcom)";db?.execSQL(sql)


            sql="CREATE TABLE [Ordenserial] ("+
                    "idorden INTEGER NOT NULL,"+
                    "idordendet INTEGER NOT NULL,"+
                    "serial TEXT NOT NULL,"+
                    "PRIMARY KEY ([idorden],[idordendet],[serial])"+
                    ");";
            db?.execSQL(sql);


            sql="CREATE TABLE [T_ordendet] ("+
                    "CODIGO_ORDEN_SERVICIO_DET INTEGER NOT NULL,"+
                    "CODIGO_ORDEN_SERVICIO INTEGER NOT NULL,"+
                    "CODIGO_PRODUCTO INTEGER NOT NULL,"+
                    "DESCRIPCION TEXT NOT NULL,"+
                    "PRECIO REAL NOT NULL,"+
                    "REALIZADO INTEGER NOT NULL,"+
                    "CANTIDAD REAL NOT NULL,"+
                    "TOTAL REAL NOT NULL,"+
                    "ACTIVO INTEGER NOT NULL,"+
                    "PRIMARY KEY ([CODIGO_ORDEN_SERVICIO_DET])"+
                    ");";
            db?.execSQL(sql);


            sql="CREATE TABLE [T_ordenenc] ("+
                    "CODIGO_ORDEN_SERVICIO INTEGER NOT NULL,"+
                    "NUMERO TEXT NOT NULL,"+
                    "CODIGO_CLIENTE INTEGER NOT NULL,"+
                    "CODIGO_SUCURSAL INTEGER NOT NULL,"+
                    "CODIGO_EMPRESA INTEGER NOT NULL,"+
                    "CODIGO_TIPO_ORDEN_SERVICIO INTEGER NOT NULL,"+
                    "CODIGO_ESTADO_ORDEN_SERVICIO INTEGER NOT NULL,"+
                    "CODIGO_CLIENTE_CONTACTO INTEGER NOT NULL,"+
                    "CODIGO_DIRECCION INTEGER NOT NULL,"+
                    "CODIGO_MONEDA INTEGER NOT NULL,"+
                    "TOTAL REAL NOT NULL,"+
                    "ANULADA INTEGER NOT NULL,"+
                    "ACTIVA INTEGER NOT NULL,"+
                    "CERRADA INTEGER NOT NULL,"+
                    "DESCRIPCION TEXT NOT NULL,"+
                    "OBSERVACION TEXT NOT NULL,"+
                    "PRIMARY KEY ([CODIGO_ORDEN_SERVICIO])"+
                    ");";
            db?.execSQL(sql);

            //endregion

            //region App

            sql = "CREATE TABLE [Params] (" +
                    "ID integer NOT NULL," +
                    "dbver INTEGER  NOT NULL," +
                    "param1 TEXT  NOT NULL," +
                    "param2 TEXT  NOT NULL," +
                    "param3 INTEGER  NOT NULL," +  // EntityID
                    "param4 INTEGER  NOT NULL," +  //
                    "lic1 TEXT  NOT NULL," +  //
                    "lic2 INTEGER  NOT NULL," +  // i
                    "PRIMARY KEY ([ID])" + ");"
            db.execSQL(sql)

            sql = "CREATE TABLE [ParamLic] (" +
                    "ID integer NOT NULL," +
                    "param1 TEXT  NOT NULL," +
                    "param2 INTEGER  NOT NULL," +
                    "PRIMARY KEY ([ID])" + ");"
            db.execSQL(sql)

            sql = "CREATE TABLE [Paramsext] (" +
                    "ID INTEGER NOT NULL," +
                    "Nombre TEXT NOT NULL," +
                    "Valor TEXT NOT NULL," +
                    "Tipo TEXT NOT NULL," +
                    "PRIMARY KEY ([ID])" + ");"
            db.execSQL(sql)

            //endregion

        } catch (e: SQLiteException) {
            msgbox(e.message)

        }

    }

    fun scriptData(db: SQLiteDatabase) {
        try {
            db.execSQL("INSERT INTO Params VALUES (1,1,'','',0,0,'',0);")

            // 0-idempresa, 1-idusuario, 2-Nombre Empresa, 3-modo,4-rol,
            // 5-pais, 6-idsucursal, 7-moneda simb, 8 - moneda id

            db.execSQL("INSERT INTO Savepos VALUES (0,'44');")
            db.execSQL("INSERT INTO Savepos VALUES (2,'DTSolutions');")
            db.execSQL("INSERT INTO Savepos VALUES (3,'TEC');")
            db.execSQL("INSERT INTO Savepos VALUES (4,'TEC');")
            db.execSQL("INSERT INTO Savepos VALUES (5,'GT');")
            db.execSQL("INSERT INTO Savepos VALUES (6,'141');")
            db.execSQL("INSERT INTO Savepos VALUES (7,'Q');")
            db.execSQL("INSERT INTO Savepos VALUES (8,'6');")

        } catch (e: SQLiteException) {
            msgbox(e.message)
        }
    }

    private fun msgbox(msg: String?) {
        val dialog = AlertDialog.Builder(vcontext)
        dialog.setTitle(R.string.app_name)
        dialog.setMessage(msg)
        dialog.setNeutralButton("OK") { dialog, which -> }
        dialog.show()
    }
}