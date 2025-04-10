package com.dts.osm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.dts.base.clsClasses
import com.dts.classes.clsEnvioimagenObj
import com.dts.classes.clsOrdendetObj
import com.dts.classes.clsOrdenenccapObj
import com.dts.classes.clsOrdenserialObj
import com.dts.restapi.HttpClient
import com.google.gson.Gson
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class Pendientes : PBase() {

    var lblstat: TextView? = null

    var http: HttpClient? = null
    var gson = Gson()

    var OrdenenccapObj: clsOrdenenccapObj? = null
    var OrdendetObj: clsOrdendetObj? = null
    var OrdenserialObj: clsOrdenserialObj? = null


    var enviados = ArrayList<Int>()

    var cap = clsClasses.clsOrdenenccap()

    var ctotal : Int=0
    var cpos : Int=0
    var cenv: Int=0
    var idorden=0
    var idle=true

    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_pendientes)

            super.initbase(savedInstanceState)

            lblstat = findViewById(R.id.textView30);lblstat?.text="Orden : "

            http = HttpClient()

            OrdenenccapObj = clsOrdenenccapObj(this, Con!!, db!!)
            OrdendetObj = clsOrdendetObj(this, Con!!, db!!)
            OrdenserialObj = clsOrdenserialObj(this, Con!!, db!!)


            if (app?.sinInternet() == true) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed( {
                    toast("Sin conexión a internet")
                    finish() }, 200)
            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed( { iniciaEnvio() }, 200)

        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }


    //region Events


    //endregion

    //region Main

    fun iniciaEnvio() {

        finish()

        try {
            OrdenenccapObj?.fill("WHERE (recibido=1)")

            ctotal=OrdenenccapObj?.count!!
            if (ctotal==0) {
                finishCom();return
            }

            idle=false
            enviaOrden()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }


    }

    fun enviaOrden() {

        if (cpos>=ctotal) {
            finishCom();return
        }

        try {

            runOnUiThread {lblstat?.text = "  Orden: "+(cpos+1)+" / "+(ctotal+1)}

            sql="UPDATE D_ORDEN_SERVICIO_ENC SET TOTAL="+cpos+" WHERE CODIGO_ORDEN_SERVICIO=5"

            cap=OrdenenccapObj?.items?.get(cpos)!!
            idorden=cap?.idorden!!

            var fs=du?.univfecha(du?.actDateTime!!)
            var sqle=app?.buildEncUpdate(cap,5,fs!!)!!+";"
            var sqld=updateDetaille()
            var sqls=updateSerial()

            sql=sqle
            if (sqld!="#") sql=sql+sqld;
            if (sqls!="#") sql=sql+sqls;

            val jupd= clsClasses.clsUpdate(sql!!)
            val pbody = gson.toJson(jupd)
            val body: RequestBody = pbody.toRequestBody(gl?.mediaType)

            http?.url=gl?.urlbase+"api/Orden/Commit"

            val request: Request = Request.Builder()
                .url(http?.url!!)
                .post(body)
                .addHeader("accept", "*/*")
                .build()

            http!!.processRequest(request, { cbEnvioOrden() })
        } catch (e: java.lang.Exception) {
            toastlong(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message);
            finishCom()
        }
    }

    fun cbEnvioOrden() {
        try {
            var retcode=http?.retcode!!
            var retmsg=http?.data.toString()
            if (retcode==1) {
                enviados.add(idorden)
                cenv++
            }

            try {
                cpos++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed( { enviaOrden() }, 200)
            } catch (e: Exception) { }
        } catch (e: java.lang.Exception) {
            toastlong(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message);
            finishCom()
        }
    }

    fun updateDetaille():String {
        var ccmd=""
        var tcmd=""

        OrdendetObj?.fill("WHERE (idOrden="+idorden+")")
        if (OrdendetObj?.count!!>0) {
            for (itm in OrdendetObj?.items!!) {
                ccmd=app?.buildDetUpdate(itm)!!
                tcmd+=ccmd+";"
            }
            return tcmd
        } else {
            return "#"
        }
    }

    fun updateSerial():String {
        var tcmd="DELETE FROM D_ORDEN_SERVICIO_SERIAL WHERE (CODIGO_ORDEN_SERVICIO="+idorden+");"
        var ccmd=""

        OrdenserialObj?.fill("WHERE (idOrden="+idorden+")")
        if (OrdenserialObj?.count!!>0) {
            for (itm in OrdenserialObj?.items!!) {
                ccmd=app?.buildSerialUpdate(itm)!!
                tcmd+=ccmd+";"
            }
            return tcmd
        } else {
            return "#"
        }
    }

    fun finishCom() {

        try {
            idle=true
            toastlong("Enviado ordenes : "+(cenv)+" / "+(ctotal+1))

            if (enviados?.size!!>0) {
                for (itm in OrdenenccapObj?.items!!) {
                    idorden=itm.idorden
                    if (enviados.contains(idorden)) {
                        itm.activa = 2
                        itm.recibido=1

                        OrdenenccapObj?.update(itm)
                    }
                }
            }

            var EnvioimagenObj = clsEnvioimagenObj(this, Con!!, db!!)
            EnvioimagenObj?.fill()

            if (EnvioimagenObj?.count!!>0) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed( { startActivity(Intent(this, EnvioImagenes::class.java)) }, 200)
            }
        } catch (e: Exception) {
            toastlong(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

        finish()
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


    //endregion

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }

            OrdenenccapObj!!.reconnect(Con!!, db!!)
            OrdendetObj!!.reconnect(Con!!, db!!)
            OrdenserialObj!!.reconnect(Con!!, db!!)


        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}