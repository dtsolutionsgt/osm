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

            sql="CREATE TABLE [Cliente] ("+
                    "Codigo_Cliente INTEGER NOT NULL,"+
                    "Nombre TEXT NOT NULL,"+
                    "Telefono TEXT NOT NULL,"+
                    "Direccion TEXT NOT NULL,"+
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
                    "PRECIO RERAL NOT NULL,"+
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

            sql = "CREATE TABLE [Savepos] (" +
                    "id INTEGER NOT NULL," +
                    "valor TEXT NOT NULL," +
                    "PRIMARY KEY ([id])" +
                    ");";
            db.execSQL(sql);

            sql="CREATE TABLE [Tiposervicios] ("+
                    "codigo_tipo_departamento INTEGER NOT NULL,"+
                    "codigo_ticket_departamento INTEGER NOT NULL,"+
                    "nombre TEXT NOT NULL,"+
                    "PRIMARY KEY ([codigo_tipo_departamento])"+
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



            /*

            sql = "CREATE TABLE [Envioimagen] (" +
                    "id TEXT NOT NULL," +
                    "tipo INTEGER NOT NULL," +
                    "PRIMARY KEY ([id])" +
                    ");";
            db?.execSQL(sql);

            sql = "CREATE TABLE [Estadoorden] (" +
                    "id INTEGER NOT NULL," +
                    "nombre TEXT NOT NULL," +
                    "PRIMARY KEY ([id])" +
                    ");";
            db?.execSQL(sql);

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


            sql = "CREATE TABLE [Ordencliente] (" +
                    "id INTEGER NOT NULL," +
                    "nombre TEXT NOT NULL," +
                    "dir TEXT NOT NULL," +
                    "tel TEXT NOT NULL," +
                    "PRIMARY KEY ([id])" +
                    ");";
            db?.execSQL(sql);


            sql = "CREATE TABLE [Ordencont] (" +
                    "id INTEGER NOT NULL," +
                    "idcliente INTEGER NOT NULL," +
                    "nombre TEXT NOT NULL," +
                    "dir TEXT NOT NULL," +
                    "tel TEXT NOT NULL," +
                    "PRIMARY KEY ([id])" +
                    ");";
            db?.execSQL(sql);


            sql = "CREATE TABLE [Ordendet] (" +
                    "id INTEGER NOT NULL," +
                    "idOrden INTEGER NOT NULL," +
                    "idProducto INTEGER NOT NULL," +
                    "Descripcion TEXT NOT NULL," +
                    "Realizado INTEGER NOT NULL," +
                    "Cant REAL NOT NULL," +
                    "Activo INTEGER NOT NULL," +
                    "PRIMARY KEY ([id])" +
                    ");";
            db?.execSQL(sql);


            sql = "CREATE TABLE [Ordendir] (" +
                    "id INTEGER NOT NULL," +
                    "idcliente INTEGER NOT NULL," +
                    "referencia TEXT NOT NULL," +
                    "dir TEXT NOT NULL," +
                    "zona TEXT NOT NULL," +
                    "tel TEXT NOT NULL," +
                    "PRIMARY KEY ([id])" +
                    ");";
            db?.execSQL(sql);


            sql = "CREATE TABLE [Ordenenc] (" +
                    "idOrden INTEGER NOT NULL," +
                    "Numero TEXT NOT NULL," +
                    "Fecha INTEGER NOT NULL," +
                    "idUsuario INTEGER NOT NULL," +
                    "idEstado INTEGER NOT NULL," +
                    "idTipo INTEGER NOT NULL," +
                    "idCliContact INTEGER NOT NULL," +
                    "idDir INTEGER NOT NULL," +
                    "idCliente INTEGER NOT NULL," +
                    "descripcion TEXT NOT NULL," +
                    "fecha_cierre INTEGER NOT NULL," +
                    "hora_ini INTEGER NOT NULL," +
                    "hora_fin INTEGER NOT NULL," +
                    "PRIMARY KEY ([idOrden])" +
                    ");";
            db?.execSQL(sql);

            sql = "CREATE INDEX Ordenenc_idx1 ON Ordenenc(Fecha)";db?.execSQL(sql)
            sql = "CREATE INDEX Ordenenc_idx2 ON Ordenenc(idEstado)";db?.execSQL(sql)


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

            sql="CREATE TABLE [Syntaxlog] ("+
                    "id INTEGER NOT NULL,"+
                    "fecha INTEGER NOT NULL,"+
                    "cmd TEXT NOT NULL,"+
                    "PRIMARY KEY ([id])"+
                    ");";
            db?.execSQL(sql);

            sql="CREATE INDEX Syntaxlog_idx1 ON Syntaxlog(fecha)";db?.execSQL(sql)


            sql="CREATE TABLE [Tipoorden] ("+
                    "id INTEGER NOT NULL,"+
                    "nombre TEXT NOT NULL,"+
                    "PRIMARY KEY ([id])"+
                    ");";
            db?.execSQL(sql);


            sql="CREATE TABLE [Tiposervicio] ("+
                    "id INTEGER NOT NULL,"+
                    "idticket INTEGER NOT NULL,"+
                    "nombre TEXT NOT NULL,"+
                    "PRIMARY KEY ([id])"+
                    ");";
            db?.execSQL(sql);

            sql="CREATE TABLE [Updcmd] ("+
                    "id INTEGER NOT NULL,"+
                    "cmd TEXT NOT NULL,"+
                    "PRIMARY KEY ([id])"+
                    ");";
            db?.execSQL(sql);

            sql="CREATE TABLE [Updsave] ("+
                    "id INTEGER NOT NULL,"+
                    "cmd TEXT NOT NULL,"+
                    "PRIMARY KEY ([id])"+
                    ");";
            db?.execSQL(sql);


            sql="CREATE TABLE [Usuario] ("+
                    "id INTEGER NOT NULL,"+
                    "nombre TEXT NOT NULL,"+
                    "clave TEXT NOT NULL,"+
                    "activo INTEGER NOT NULL,"+
                    "rol INTEGER NOT NULL,"+
                    "PRIMARY KEY ([id])"+
                    ");";
            db.execSQL(sql);

            sql="CREATE TABLE [Param] ("+
                    "codigo INTEGER NOT NULL,"+
                    "empresa INTEGER NOT NULL,"+
                    "id INTEGER NOT NULL,"+
                    "userid INTEGER NOT NULL,"+
                    "nombre TEXT NOT NULL,"+
                    "valor TEXT NOT NULL,"+
                    "PRIMARY KEY ([codigo])"+
                    ");";
            db?.execSQL(sql);

             */

            //-------------------------------------------

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

        } catch (e: SQLiteException) {
            msgbox(e.message)

        }
    }

    fun scriptData(db: SQLiteDatabase) {
        try {
            db.execSQL("INSERT INTO Params VALUES (1,1,'','',0,0,'',0);")
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