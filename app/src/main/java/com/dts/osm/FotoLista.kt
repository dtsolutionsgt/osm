package com.dts.osm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.clsEnvioimagenObj
import com.dts.classes.clsOrdenfotoObj
import com.dts.classes.clsUpdcmdObj
import com.dts.ladapt.LA_FotoAdapter
import com.dts.restapi.HttpClient
import com.google.gson.Gson
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class FotoLista : PBase() {

    var recview: RecyclerView? = null
    var lbl1: TextView? = null
    var pbar: ProgressBar? = null

    var http: HttpClient? = null
    var gson = Gson()

    var OrdenfotoObj: clsOrdenfotoObj? = null
    var EnvioimagenObj: clsEnvioimagenObj? = null
    var UpdcmdObj: clsUpdcmdObj? = null

    var adapter: LA_FotoAdapter? = null

    var selitem= clsClasses.clsOrdenfoto()
    var idorden=0
    var idordendet=0
    var ssql=""
    var idle=true


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_foto_lista)

            super.initbase(savedInstanceState)

            onBackPressedDispatcher.addCallback(this,backPress)

            recview = findViewById<View>(R.id.recview) as RecyclerView
            recview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            lbl1 = findViewById(R.id.textView28)
            pbar = findViewById(R.id.progressBar3)

            idorden=gl?.idorden!!
            idordendet=gl?.idordendet!!
            gl?.changed=false

            http = HttpClient()

            OrdenfotoObj = clsOrdenfotoObj(this, Con!!, db!!)
            EnvioimagenObj = clsEnvioimagenObj(this, Con!!, db!!)
            UpdcmdObj = clsUpdcmdObj(this, Con!!, db!!)

            setHandlers()
            listItems()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //region Events

    fun doPhoto(view: View) {
        if (!idle) return

        try {
            callback=1
            startActivity(Intent(this,Foto::class.java))
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun doExit(view: View) {
        if (idle) envioLista()
    }

    private fun setHandlers() {
        try {
            recview?.addOnItemTouchListener(
                RecyclerItemClickListener(this, recview!!,
                    object : RecyclerItemClickListener.OnItemClickListener {

                        override fun onItemClick(view: View, position: Int) {
                            try {
                                if (idle) {
                                    selitem = OrdenfotoObj?.items?.get(position)!!
                                    gl?.idordfoto = OrdenfotoObj?.items?.get(position)?.id!!
                                    gl?.gstr = gl?.picdir!!
                                    val rotationAngle = selitem?.rotationAngle ?: 0

                                    val intent = Intent(this@FotoLista, FotoDetalle::class.java)
                                    intent.putExtra("rotationAngle", rotationAngle)
                                    callback = 2
                                    startActivity(Intent(this@FotoLista, FotoDetalle::class.java))
                                }
                            } catch (e: Exception) {
                                msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
                            }
                        }

                        override fun onItemLongClick(view: View?, position: Int) {
                            try {
                                if (idle) {
                                    selitem = OrdenfotoObj?.items?.get(position)!!
                                    msgask(0, "Borrar foto?")
                                }
                            } catch (e: Exception) {
                                msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
                            }
                        }
                    })
            )

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Main

    fun listItems() {
        try {
            OrdenfotoObj?.fill("WHERE (idOrden="+idorden+") AND (idOrdendet="+idordendet+") AND " +
                    "(nombre NOT LIKE '%FIRMA%') ORDER BY id DESC")
            adapter = LA_FotoAdapter(OrdenfotoObj?.items!!,gl?.picdir!!, OrdenfotoObj!!)
            recview?.adapter = adapter

            lbl1?.text="Registros: "+OrdenfotoObj?.items?.size
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun addItem() {
        try {

            var newid=OrdenfotoObj?.newID("SELECT Max(id) FROM Ordenfoto")

            var item = clsClasses.clsOrdenfoto()
            item.id = newid!!
            item.idorden = idorden
            item.idordendet = idordendet
            item.nombre = gl?.idfoto!!
            item.nota = ""
            item.statcom = 0

            OrdenfotoObj?.add(item)

            var eiitem = clsClasses.clsEnvioimagen(gl?.idfoto!!, 1)
            try {
                EnvioimagenObj?.add(eiitem)
            } catch (e: Exception) {
                EnvioimagenObj?.update(eiitem)
            }

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

        listItems()
    }

    fun delItem() {
        try {
            OrdenfotoObj?.delete(selitem)

            var fbm= File(gl?.picdir!!,selitem?.nombre)
            fbm.delete()

            gl?.changed=true

            var eiitem = clsClasses.clsEnvioimagen(selitem?.nombre!!, 0)
            try {
                EnvioimagenObj?.add(eiitem)
            } catch (e: Exception) {
                EnvioimagenObj?.update(eiitem)
            }

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

        listItems()
    }

    //endregion

    //region Envio datos

    fun envioLista() {
        try {

            ssql="DELETE FROM D_ORDEN_SERVICIO_FOTO WHERE (CODIGO_ORDEN_SERVICIO="+idorden+");"

            OrdenfotoObj?.fill("WHERE (idOrden="+idorden+")")
            for (itm in OrdenfotoObj?.items!!) {
                sql=addItemSql(itm)
                ssql+=sql+";"
            }

            sendUpdate(ssql)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun sendUpdate(usql:String) {
        try {
            val jupd=clsClasses.clsUpdate(usql)
            val pbody = gson.toJson(jupd)
            val body: RequestBody = pbody.toRequestBody(gl?.mediaType)

            http?.url=gl?.urlbase+"api/Orden/Commit"

            val request: Request = Request.Builder()
                .url(http?.url!!)
                .post(body)
                .addHeader("accept", "*/*")
                .build()

            http!!.processRequest(request, { cbUpdate() })
        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message);
        }
    }

    fun cbUpdate() {
        try {
            var rslt=http?.data!!
            if (rslt=="#") {
                sql="UPDATE Ordenfoto SET statcom=1 WHERE (idOrden="+idorden+")"
                db?.execSQL(sql);
            } else {
                var newid=UpdcmdObj?.newID("SELECT MAX(id) FROM Updcmd")
                ssql=ssql.replace("'","´")
                var uitem= clsClasses.clsUpdcmd(newid!!, ssql)
                UpdcmdObj?.add(uitem)
            }
        } catch (e: java.lang.Exception) {
            toast(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message);
        }

        actualizaImagenes()
        finish()
    }

    fun addItemSql(item: clsClasses.clsOrdenfoto): String? {
        ins!!.init("D_ORDEN_SERVICIO_FOTO")
        ins!!.add("CODIGO_ORDEN_SERVICIO", item.idorden)
        if (item.idordendet>0) ins!!.add("CODIGO_ORDEN_SERVICIO_DET", item.idordendet)
        ins!!.add("DESCRIPCION", item.nombre)
        ins!!.add("NOTA", item.nota)
        return ins!!.sql()
    }

    fun actualizaImagenes() {
        try {

            EnvioimagenObj?.fill()

            if (EnvioimagenObj?.count!!>0) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    startActivity(Intent(this, EnvioImagenes::class.java))
                }, 200)
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Dialogs

    fun dialogswitch() {
        try {
            when (gl?.dialogid) {
                0 -> {delItem()}
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

            OrdenfotoObj?.reconnect(Con!!,db!!)
            EnvioimagenObj?.reconnect(Con!!,db!!)
            UpdcmdObj!!.reconnect(Con!!, db!!)

            if (callback==1) {
                callback=0
                if (gl?.idfoto?.isNotBlank()!!) addItem()
                return
            }

            if (callback==2) {
                callback=0
                listItems()
                return
            }

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    val backPress = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            envioLista()
        }
    }

    //endregion

}