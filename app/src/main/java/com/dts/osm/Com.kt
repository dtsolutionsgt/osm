package com.dts.osm

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import com.dts.base.clsClasses
import com.dts.classes.clsClienteObj
import com.dts.classes.clsClientecontactoObj
import com.dts.classes.clsClientedirObj
import com.dts.classes.clsEstadoObj
import com.dts.classes.clsProdprecioObj
import com.dts.classes.clsProductoObj
import com.dts.classes.clsTiposerviciosObj
import com.dts.classes.clsUsuarioObj
import com.dts.restapi.ClassesAPI
import com.dts.restapi.HttpClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Request

class Com : PBase() {

    var lblstat: TextView? = null
    var relcom: RelativeLayout? = null
    var pbar: ProgressBar? = null
    var imgpend: ImageView? = null

    var http: HttpClient? = null
    var gson = Gson()

    var UsuarioObj: clsUsuarioObj? = null
    var ProductoObj: clsProductoObj? = null
    var ProdprecioObj: clsProdprecioObj? = null
    var EstadoObj: clsEstadoObj? = null
    var TiposervicioObj: clsTiposerviciosObj? = null
    var ClienteObj: clsClienteObj? = null
    var ClientecontactoObj: clsClientecontactoObj? = null
    var ClientedirObj: clsClientedirObj? = null

    var updrem = ArrayList<String>()
    var updloc = ArrayList<String>()
    var fotos = ArrayList<String>()

    var updpos=0
    var updsize=0
    var lpos=0
    var lsize=0
    var updcerrar=false
    var upderrs=0
    var upderr=""
    var sqlrem=""
    var sqlloc=""

    var idle=true
    var enccnt=0
    var flim=0L
    var rol=0


    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_com)

            super.initbase(savedInstanceState)

            onBackPressedDispatcher.addCallback(this,backPress)

            lblstat = findViewById(R.id.textView10);lblstat?.text=""
            relcom = findViewById(R.id.relcom);
            pbar = findViewById(R.id.progressBar2);pbar?.visibility= View.INVISIBLE
            imgpend = findViewById(R.id.imageView29);imgpend?.isVisible=false

            http = HttpClient()

            UsuarioObj = clsUsuarioObj(this, Con!!, db!!)
            ProductoObj = clsProductoObj(this, Con!!, db!!)
            ProdprecioObj = clsProdprecioObj(this, Con!!, db!!)
            EstadoObj = clsEstadoObj(this, Con!!, db!!)
            TiposervicioObj = clsTiposerviciosObj(this, Con!!, db!!)
            ClienteObj = clsClienteObj(this, Con!!, db!!)
            ClientecontactoObj = clsClientecontactoObj(this, Con!!, db!!)
            ClientedirObj = clsClientedirObj(this, Con!!, db!!)


        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }

    //region Events

    fun doCom(view: View) {
        if (idle) comunica()
    }

    fun doExit(view: View) {
        if (idle) finishCom() else toast("Espere, por favor . . . ")
    }
    //endregion

    //region Main

    fun comunica() {
        try {
            idle=false
            pbar?.visibility=View.VISIBLE
            relcom?.isVisible=false

            upderrs=0;upderr=""
            updrem.clear();updloc.clear()

            recUsuarios()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Catalogos

    fun recUsuarios() {
        try {
            runOnUiThread {lblstat?.text = "Actualizando usuarios . . ."}

            http?.url=gl?.urlbase+"api/Users/GetUsuarioApp?Id_app=OSM"

            val request: Request = Request.Builder()
                .url(http?.url!!).get()
                .addHeader("accept", "*/*")
                .build()

            http!!.processRequest(request, { cbUsuarios() })
        } catch (e: java.lang.Exception) {
            finerr(object : Any() {}.javaClass.enclosingMethod.name ,e.message!!);
        }
    }

    fun cbUsuarios() {
        var jss: ClassesAPI.clsAPIUsuarioApp? = null
        var item: clsClasses.clsUsuario

        try {
            Looper.prepare()

            if (http!!.retcode!=1) {
                stoperr("Error: "+http!!.data);return
            }

            val parsedList =http?.splitJsonArray()
            val RType = object : TypeToken<ClassesAPI.clsAPIUsuarioApp>() {}.type

            try {
                db!!.beginTransaction()
                db!!.execSQL("DELETE FROM Usuario");

                for (pls in parsedList!!) {

                    jss=gson.fromJson(pls, RType)
                    item= clsClasses.clsUsuario()

                    item.id = jss?.Codigo_Usuario!!
                    item.nombre = jss?.Nombre.toString()
                    item.pin = jss?.Pin!!
                    item.rol = jss?.Rol!!

                    UsuarioObj?.add(item)

                }

                db!!.setTransactionSuccessful()
                db!!.endTransaction()
            } catch (e: java.lang.Exception) {
                db!!.endTransaction()
                finerr(object : Any() {}.javaClass.enclosingMethod.name ,e.message!!)
                return
            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({recProductos()}, 200)

        } catch (e: java.lang.Exception) {
            var es=e.message
            finerr(object : Any() {}.javaClass.enclosingMethod.name ,e.message!!);
        }
    }

    fun recProductos() {
        try {
            runOnUiThread {lblstat?.text = "Actualizando productos . . ."}

            http?.url=gl?.urlbase+"api/Users/Getproductos?pEmpresa="+gl?.idemp!!

            val request: Request = Request.Builder()
                .url(http?.url!!).get()
                .addHeader("accept", "*/*")
                .build()

            http!!.processRequest(request, { cbProductos() })
        } catch (e: java.lang.Exception) {
            finerr(object : Any() {}.javaClass.enclosingMethod.name ,e.message!!);
        }
    }

    fun cbProductos() {
        var jss: ClassesAPI.clsAPIProducto? = null
        var item: clsClasses.clsProducto

        try {
            try {
                if (http!!.retcode!=1) {
                    stoperr("Error: "+http!!.data);return
                }

                val parsedList =http?.splitJsonArray()
                val RType = object : TypeToken<ClassesAPI.clsAPIProducto>() {}.type

                db!!.beginTransaction()

                db!!.execSQL("DELETE FROM Producto");

                for (pls in parsedList!!) {

                    jss=gson.fromJson(pls, RType)
                    item= clsClasses.clsProducto()

                    item.codigo_producto = jss?.CODIGO_PRODUCTO!!
                    item.desclarga = jss?.DESCLARGA!!
                    item.codigo_tipo = jss?.CODIGO_TIPO!!

                    try {
                        ProductoObj?.add(item)
                    } catch (e: Exception) {
                        throw Exception("cbProductos . "+e.message+"\n"+ProductoObj?.addItemSql(item))
                    }

                }

                db!!.setTransactionSuccessful()
                db!!.endTransaction()
            } catch (e: java.lang.Exception) {
                db!!.endTransaction()
                finerr(object : Any() {}.javaClass.enclosingMethod.name,e.message!!)
                return
            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({recPrecios()}, 200)

        } catch (e: java.lang.Exception) {
            var es=e.message
            finerr(object : Any() {}.javaClass.enclosingMethod.name ,e.message!!);
        }

    }

    fun recPrecios() {
        try {
            runOnUiThread {lblstat?.text = "Actualizando precios . . ."}

            http?.url=gl?.urlbase+"api/Users/GetProductosPrecio?pEmpresa="+gl?.idemp!!

            val request: Request = Request.Builder()
                .url(http?.url!!).get()
                .addHeader("accept", "*/*")
                .build()

            http!!.processRequest(request, { cbPrecios() })
        } catch (e: java.lang.Exception) {
            finerr(object : Any() {}.javaClass.enclosingMethod.name , e.message!!);
        }
    }

    fun cbPrecios() {
        var jss: ClassesAPI.clsAPIProdprecio? = null
        var item: clsClasses.clsProdprecio

        try {
            try {
                if (http!!.retcode!=1) {
                    stoperr("Error: "+http!!.data);return
                }

                val parsedList =http?.splitJsonArray()
                val RType = object : TypeToken<ClassesAPI.clsAPIProdprecio>() {}.type

                db!!.beginTransaction()

                db!!.execSQL("DELETE FROM Prodprecio");

                for (pls in parsedList!!) {

                    jss=gson.fromJson(pls, RType)
                    item= clsClasses.clsProdprecio()

                    item.codigo_precio = jss?.CODIGO_PRECIO!!
                    item.codigo_producto = jss?.CODIGO_PRODUCTO!!
                    item.nivel = jss?.NIVEL!!
                    item.precio = jss?.PRECIO!!
                    item.unidadmedida = jss?.UNIDADMEDIDA!!

                    try {
                        ProdprecioObj?.add(item)
                    } catch (e: Exception) {
                        throw Exception("  "+e.message+"\n"+ProdprecioObj?.addItemSql(item))
                    }
                }

                db!!.setTransactionSuccessful()
                db!!.endTransaction()
            } catch (e: java.lang.Exception) {
                db!!.endTransaction()
                throw Exception( e.message)
                return
            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({recEstados()}, 200)

        } catch (e: java.lang.Exception) {
            var es=e.message
            finerr(object : Any() {}.javaClass.enclosingMethod.name , e.message!!);
        }

    }

    fun recEstados() {
        try {
            runOnUiThread {lblstat?.text = "Actualizando estados . . ."}

            http?.url=gl?.urlbase+"api/Users/Get_P_Ticket_Estado"

            val request: Request = Request.Builder()
                .url(http?.url!!).get()
                .addHeader("accept", "*/*")
                .build()

            http!!.processRequest(request, { cbEstados() })
        } catch (e: java.lang.Exception) {
            finerr(object : Any() {}.javaClass.enclosingMethod.name , e.message!!);
        }
    }

    fun cbEstados() {
        var jss: ClassesAPI.clsAPIEstado? = null
        var item: clsClasses.clsEstado

        try {
            try {
                if (http!!.retcode!=1) {
                    stoperr("Error: "+http!!.data);return
                }

                val parsedList =http?.splitJsonArray()
                val RType = object : TypeToken<ClassesAPI.clsAPIEstado>() {}.type

                db!!.beginTransaction()

                db!!.execSQL("DELETE FROM Estado");

                for (pls in parsedList!!) {

                    jss=gson.fromJson(pls, RType)
                    item= clsClasses.clsEstado()

                    item.codigo_ticket_estado = jss?.CODIGO_TICKET_ESTADO!!
                    item.nombre = jss?.Nombre!!

                    try {
                        EstadoObj?.add(item)
                    } catch (e: Exception) {
                        throw Exception("  "+e.message+"\n"+EstadoObj?.addItemSql(item))
                    }
                }

                db!!.setTransactionSuccessful()
                db!!.endTransaction()
            } catch (e: java.lang.Exception) {
                db!!.endTransaction()
                throw Exception( e.message)
                return
            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({recTipoServicio()}, 200)

        } catch (e: java.lang.Exception) {
            var es=e.message
            finerr(object : Any() {}.javaClass.enclosingMethod.name , e.message!!);
        }

    }

    fun recTipoServicio() {
        try {
            runOnUiThread {lblstat?.text = "Actualizando tipos . . ."}

            http?.url=gl?.urlbase+"api/Users/Get_P_TipoServicioDepartamento"

            val request: Request = Request.Builder()
                .url(http?.url!!).get()
                .addHeader("accept", "*/*")
                .build()

            http!!.processRequest(request, { cbTipoServicio() })
        } catch (e: java.lang.Exception) {
            finerr(object : Any() {}.javaClass.enclosingMethod.name , e.message!!);
        }
    }

    fun cbTipoServicio() {
        var jss: ClassesAPI.clsAPITipoServicio? = null
        var item: clsClasses.clsTiposervicios

        try {
            try {
                if (http!!.retcode!=1) {
                    stoperr("Error: "+http!!.data);return
                }

                val parsedList =http?.splitJsonArray()
                val RType = object : TypeToken<ClassesAPI.clsAPITipoServicio>() {}.type

                db!!.beginTransaction()

                db!!.execSQL("DELETE FROM Tiposervicios");

                for (pls in parsedList!!) {

                    jss=gson.fromJson(pls, RType)
                    item= clsClasses.clsTiposervicios()

                    item.codigo_tipo_departamento  = jss?.CODIGO_TIPO_SERVICIO_DEP!!
                    item.codigo_ticket_departamento  = jss?.CODIGO_TICKET_DEPARTAMENTO!!
                    item.nombre = jss?.Nombre!!

                    try {
                        TiposervicioObj?.add(item)
                    } catch (e: Exception) {
                        throw Exception("  "+e.message+"\n"+TiposervicioObj?.addItemSql(item))
                    }
                }

                db!!.setTransactionSuccessful()
                db!!.endTransaction()
            } catch (e: java.lang.Exception) {
                db!!.endTransaction()
                throw Exception( e.message)
                return
            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({recClientes()}, 200)

        } catch (e: java.lang.Exception) {
            var es=e.message
            finerr(object : Any() {}.javaClass.enclosingMethod.name , e.message!!);
        }

    }

    fun recClientes() {
        try {
            runOnUiThread {lblstat?.text = "Actualizando clientes . . ."}

            http?.url=gl?.urlbase+"api/Users/GetPCliente?Empresa="+gl?.idemp!!

            val request: Request = Request.Builder()
                .url(http?.url!!).get()
                .addHeader("accept", "*/*")
                .build()

            http!!.processRequest(request, { cbClientes() })
        } catch (e: java.lang.Exception) {
            finerr(object : Any() {}.javaClass.enclosingMethod.name , e.message!!);
        }
    }

    fun cbClientes() {
        var jss: ClassesAPI.clsAPICliente? = null
        var item: clsClasses.clsCliente

        try {
            try {
                if (http!!.retcode!=1) {
                    stoperr("Error: "+http!!.data);return
                }

                val parsedList =http?.splitJsonArray()
                val RType = object : TypeToken<ClassesAPI.clsAPICliente>() {}.type

                db!!.beginTransaction()

                db!!.execSQL("DELETE FROM Cliente");

                for (pls in parsedList!!) {

                    jss=gson.fromJson(pls, RType)
                    item= clsClasses.clsCliente()

                    item.codigo_cliente = jss?.Codigo_Cliente!!
                    item.nombre = jss?.Nombre!!
                    item.telefono = jss?.Telefono!!
                    item.direccion = jss?.Direccion!!

                    try {
                        ClienteObj?.add(item)
                    } catch (e: Exception) {
                        throw Exception("  "+e.message+"\n"+ClienteObj?.addItemSql(item))
                    }
                }

                db!!.setTransactionSuccessful()
                db!!.endTransaction()
            } catch (e: java.lang.Exception) {
                db!!.endTransaction()
                throw Exception( e.message)
                return
            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({recClienteDir()}, 200)

        } catch (e: java.lang.Exception) {
            var es=e.message
            finerr(object : Any() {}.javaClass.enclosingMethod.name , e.message!!);
        }

    }

    fun recClienteDir() {
        try {
            runOnUiThread {lblstat?.text = "Actualizando direcciones . . ."}

            http?.url=gl?.urlbase+"api/Users/GetClienteDir?pEmpresa="+gl?.idemp!!

            val request: Request = Request.Builder()
                .url(http?.url!!).get()
                .addHeader("accept", "*/*")
                .build()

            http!!.processRequest(request, { cbClienteDir() })
        } catch (e: java.lang.Exception) {
            finerr(object : Any() {}.javaClass.enclosingMethod.name , e.message!!);
        }
    }

    fun cbClienteDir() {
        var jss: ClassesAPI.clsAPIClienteDir? = null
        var item: clsClasses.clsClientedir

        try {
            try {
                if (http!!.retcode!=1) {
                    stoperr("Error: "+http!!.data);return
                }

                val parsedList =http?.splitJsonArray()
                val RType = object : TypeToken<ClassesAPI.clsAPIClienteDir>() {}.type

                db!!.beginTransaction()

                db!!.execSQL("DELETE FROM Clientedir");

                for (pls in parsedList!!) {

                    jss=gson.fromJson(pls, RType)
                    item= clsClasses.clsClientedir()

                    item.codigo_direccion = jss?.CODIGO_DIRECCION!!
                    item.codigo_cliente = jss?.CODIGO_CLIENTE!!
                    item.direccion = jss?.DIRECCION!!
                    item.telefono = jss?.TELEFONO!!
                    item.referencia = jss?.REFERENCIA!!

                    try {
                        ClientedirObj?.add(item)
                    } catch (e: Exception) {
                        throw Exception("  "+e.message+"\n"+ClientedirObj?.addItemSql(item))
                    }
                }

                db!!.setTransactionSuccessful()
                db!!.endTransaction()
            } catch (e: java.lang.Exception) {
                db!!.endTransaction()
                throw Exception( e.message)
                return
            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({recClienteContacto()}, 200)

        } catch (e: java.lang.Exception) {
            var es=e.message
            finerr(object : Any() {}.javaClass.enclosingMethod.name , e.message!!);
        }

    }

    fun recClienteContacto() {
        try {
            runOnUiThread {lblstat?.text = "Actualizando contactos . . ."}

            http?.url=gl?.urlbase+"api/Users/GetClienteContacto?pEmpresa="+gl?.idemp!!

            val request: Request = Request.Builder()
                .url(http?.url!!).get()
                .addHeader("accept", "*/*")
                .build()

            http!!.processRequest(request, { cbClienteContacto() })
        } catch (e: java.lang.Exception) {
            finerr(object : Any() {}.javaClass.enclosingMethod.name , e.message!!);
        }
    }

    fun cbClienteContacto() {
        var jss: ClassesAPI.clsAPIClienteContacto? = null
        var item: clsClasses.clsClientecontacto

        try {
            try {
                if (http!!.retcode!=1) {
                    stoperr("Error: "+http!!.data);return
                }

                val parsedList =http?.splitJsonArray()
                val RType = object : TypeToken<ClassesAPI.clsAPIClienteContacto>() {}.type

                db!!.beginTransaction()

                db!!.execSQL("DELETE FROM Clientecontacto");

                for (pls in parsedList!!) {

                    jss=gson.fromJson(pls, RType)
                    item= clsClasses.clsClientecontacto()

                    item.codigo_cliente_contacto = jss?.CODIGO_CLIENTE_CONTACTO!!
                    item.codigo_cliente = jss?.CODIGO_CLIENTE!!
                    item.nombre = jss?.NOMBRE!!
                    item.telefono = jss?.TELEFONO!!
                    item.correo = jss?.CORREO!!
                    item.direccion = jss?.DIRECCION!!

                    try {
                        ClientecontactoObj?.add(item)
                    } catch (e: Exception) {
                        throw Exception("  "+e.message+"\n"+ClientecontactoObj?.addItemSql(item))
                    }
                }

                db!!.setTransactionSuccessful()
                db!!.endTransaction()
            } catch (e: java.lang.Exception) {
                db!!.endTransaction()
                throw Exception( e.message)
                return
            }

            //val handler = Handler(Looper.getMainLooper())
            //handler.postDelayed({recEstados()}, 200)

            finok()
        } catch (e: java.lang.Exception) {
            var es=e.message
            finerr(object : Any() {}.javaClass.enclosingMethod.name , e.message!!);
        }

    }

    //endregion

    //region Dialogs

    fun dialogswitch() {
        try {
            when (gl?.dialogid) {
                0 -> {  }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }


    //endregion

    //region Aux

    fun stoperr(msg: String) {
        idle=true
        lblstat?.text="Sincronización termino con error:\n"+msg!!
        pbar?.visibility=View.INVISIBLE
    }

    fun finok() {
        idle=true
        lblstat?.text="Sincronización completa."
        pbar?.visibility=View.INVISIBLE

        //app?.params()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (!gl?.com_pend!!) {
                //if (rol==2) toastlong("Ordenes recibidos: "+enccnt)
            }
            //envioConfirmacion(true)
            finish()
        }, 1500)

    }

    fun finerr(msg1: String,msg2: String) {
        idle=true
        val ss="Sincronización termino con error: \n"+msg1+"\n"+msg2
        runOnUiThread { lblstat?.text = ss }
        pbar?.visibility=View.INVISIBLE


        //app?.params()
        //envioConfirmacion(false)
    }

    fun finishCom() {
        /*
        try {
            var EnvioimagenObj = clsEnvioimagenObj(this, Con!!, db!!)
            EnvioimagenObj?.fill()

            if (EnvioimagenObj?.count!!>0) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    startActivity(Intent(this, EnvioImagenes::class.java))
                }, 200)
            }
        } catch (e: Exception) {
            toastlong(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
        */

        finish()
    }

    //endregion

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }

            UsuarioObj!!.reconnect(Con!!, db!!)
            ProdprecioObj!!.reconnect(Con!!, db!!)
            ProductoObj!!.reconnect(Con!!, db!!)
            EstadoObj!!.reconnect(Con!!, db!!)
            TiposervicioObj!!.reconnect(Con!!, db!!)
            ClienteObj!!.reconnect(Con!!, db!!)
            ClientecontactoObj!!.reconnect(Con!!, db!!)
            ClientedirObj!!.reconnect(Con!!, db!!)

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    val backPress = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (idle) {
                onBackPressedDispatcher?.onBackPressed()
            } else {
                toast("Espere, por favor . . . ")
            }
        }
    }

    //endregion

}