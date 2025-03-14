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
import com.dts.classes.clsProdprecioObj
import com.dts.classes.clsProductoObj
import com.dts.classes.clsUsuarioObj
import com.dts.restapi.ClassesAPI
import com.dts.restapi.HttpClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Request


class Comunicacion : PBase() {

    var lblstat: TextView? = null
    var relcom: RelativeLayout? = null
    var pbar: ProgressBar? = null
    var imgpend: ImageView? = null

    var http: HttpClient? = null
    var gson = Gson()

    var UsuarioObj: clsUsuarioObj? = null
    var ProductoObj: clsProductoObj? = null
    var ProdprecioObj: clsProdprecioObj? = null

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
            setContentView(R.layout.activity_comunicacion)

            super.initbase(savedInstanceState)

            onBackPressedDispatcher.addCallback(this,backPress)

            lblstat = findViewById(R.id.textView10);lblstat?.text=""
            relcom = findViewById(R.id.relcom);
            pbar = findViewById(R.id.progressBar2);pbar?.visibility=View.INVISIBLE
            imgpend = findViewById(R.id.imageView29);imgpend?.isVisible=false

            http = HttpClient()

            UsuarioObj = clsUsuarioObj(this, Con!!, db!!)
            ProductoObj = clsProductoObj(this, Con!!, db!!)
            ProdprecioObj = clsProdprecioObj(this, Con!!, db!!)

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
            finerr(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message);
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
                finerr(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
                return
            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({recProductos()}, 200)

        } catch (e: java.lang.Exception) {
            var es=e.message
            finerr(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message);
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
            finerr(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message);
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
                finerr(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
                return
            }

            //val handler = Handler(Looper.getMainLooper())
            //handler.postDelayed({recEstados()}, 200)

            finok()
        } catch (e: java.lang.Exception) {
            var es=e.message
            finerr(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message);
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

    fun finerr(msg: String) {
        idle=true
        lblstat?.text="Sincronización termino con error:\n"+msg!!
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