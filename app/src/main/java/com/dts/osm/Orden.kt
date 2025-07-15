package com.dts.osm

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.clsClienteObj
import com.dts.classes.clsClientecontactoObj
import com.dts.classes.clsClientedirObj
import com.dts.classes.clsEnvioimagenObj
import com.dts.classes.clsEstadoordenObj
import com.dts.classes.clsExistenciaObj
import com.dts.classes.clsOrdenUpdate
import com.dts.classes.clsOrdenUpdateWS
import com.dts.classes.clsOrdendetObj
import com.dts.classes.clsOrdenencObj
import com.dts.classes.clsOrdenenccapObj
import com.dts.classes.clsOrdenfotoObj
import com.dts.classes.clsOrdenserialObj
import com.dts.classes.clsTiposervicioObj
import com.dts.classes.clsUpdsaveObj
import com.dts.classes.extListDlg
import com.dts.fbase.fbServicio
import com.dts.ladapt.LA_ordendet
import com.dts.restapi.ClassesAPI
import com.dts.restapi.HttpClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class Orden : PBase() {

    var recview: RecyclerView? = null
    var lbltit: TextView? = null
    var lbl1: TextView? = null
    var lbl2: TextView? = null
    var lbl3: TextView? = null
    var lbl4: TextView? = null
    var lbl5: TextView? = null
    var lbl6: TextView? = null
    var lbl7: TextView? = null
    var lbl8: TextView? = null
    var lbl11: TextView? = null
    var lbl12: TextView? = null
    var lbl13: TextView? = null
    var txt1: EditText? = null
    var pbar: ProgressBar? = null
    var relbot: RelativeLayout? = null

    var imgnext: ImageView? = null
    var imgfoto: ImageView? = null
    var imgsign: ImageView? = null

    var http: HttpClient? = null
    var gson = Gson()

    var EstadoordenObj: clsEstadoordenObj? = null
    var TiposervicioObj: clsTiposervicioObj? = null
    var OrdenencObj: clsOrdenencObj? = null
    var ClienteObj: clsClienteObj? = null
    var ClientecontactoObj: clsClientecontactoObj? = null
    var ClientedirObj: clsClientedirObj? = null
    var OrdenenccapObj: clsOrdenenccapObj? = null
    var OrdendetObj: clsOrdendetObj? = null
    var UpdsaveObj: clsUpdsaveObj? = null
    var ExistenciasObj: clsExistenciaObj? = null
    var OrdenserialObj: clsOrdenserialObj? = null

    var adapter: LA_ordendet? = null

    var cap = clsClasses.clsOrdenenccap()
    var enc = clsClasses.clsOrdenenc()

    var fbsa : fbServicio? = null
    var fbsc : fbServicio? = null

    var fbsItem = clsClasses.clsFbServicio()
    var updord : clsOrdenUpdate? = null

    lateinit var fusedLocationClient: FusedLocationProviderClient

    var ditems = ArrayList<clsClasses.clsOrdendet>()

    var idorden=0
    var numord=""
    var idcliente=0
    var iddir=0
    var idcont=0
    var idestado=0
    var observ=""
    var sqlsave=""
    var phoneNum=""
    var saveselidx=-1
    var saveid=-1


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_orden)

            super.initbase(savedInstanceState)

            recview = findViewById<View>(R.id.recview) as RecyclerView
            recview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            lbltit = findViewById(R.id.textView15)
            lbl1 = findViewById(R.id.textView12)
            lbl2 = findViewById(R.id.textView13)
            lbl3 = findViewById(R.id.textView17)
            lbl4= findViewById(R.id.textView18)
            lbl5 = findViewById(R.id.textView22)
            lbl6 = findViewById(R.id.textView20)
            lbl7 = findViewById(R.id.textView21)
            lbl8 = findViewById(R.id.textView23)
            lbl11 = findViewById(R.id.textView25)
            lbl12 = findViewById(R.id.textView36)
            lbl13 = findViewById(R.id.textView37)
            txt1 = findViewById(R.id.editTextText)
            imgnext = findViewById(R.id.imageView20)
            imgfoto = findViewById(R.id.imageView17)
            imgsign = findViewById(R.id.imageView18)
            pbar = findViewById(R.id.progressBar5)
            relbot = findViewById(R.id.relbot)


            http = HttpClient()

            OrdenencObj = clsOrdenencObj(this, Con!!, db!!)
            EstadoordenObj = clsEstadoordenObj(this, Con!!, db!!)
            TiposervicioObj = clsTiposervicioObj(this, Con!!, db!!)
            ClienteObj = clsClienteObj(this, Con!!, db!!)
            ClientedirObj = clsClientedirObj(this, Con!!, db!!)
            ClientecontactoObj = clsClientecontactoObj(this, Con!!, db!!)
            OrdenenccapObj = clsOrdenenccapObj(this, Con!!, db!!)
            OrdendetObj = clsOrdendetObj(this, Con!!, db!!)
            UpdsaveObj = clsUpdsaveObj(this, Con!!, db!!)
            ExistenciasObj = clsExistenciaObj(this, Con!!, db!!)
            OrdenserialObj = clsOrdenserialObj(this, Con!!, db!!)

            idorden=gl?.idorden!!

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            fbsc=fbServicio("osm",du?.actMonth,du?.actDay)
            fbsa=fbServicio("osm","servicio","orden")
            fbsa?.load(idorden, { cbServico() })

            loadItem()

            setHandlers()

        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }


    //region Events

    fun doNext(view: View) {

        capturaGPS()

        if (idestado==4) {
            if (!tieneFirma()) {
                msgbox("¡Falta la firma!")
            } else {
                when (validaDetalle()) {
                    1 -> { msgask(1,"¿Completar servicio?") }
                    0 -> { msgask(5,"¿Completar servicio sin completar todas las tareas?") }
                   -1 -> { return }
                }

            }
        } else {
            msgask(0,"Atender servicio?")
        }
    }

    fun doPhoto(view: View) {
        if (idestado==8) return
        try {
            gl?.idordendet=0
            startActivity(Intent(this,FotoLista::class.java))
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun doUbic(view: View) {
        capturaGPS()
    }

    fun doSign(view: View) {
        if (idestado!=4) return

        try {
            OrdenenccapObj?.fill("WHERE (idOrden="+idorden+")")
            if (OrdenenccapObj?.first()?.firmacliente?.isNotEmpty()!!) {
                msgask(4,"La firma ya existe.¿Capturar la de nuevo?")
            } else {
                startActivity(Intent(this, Firma::class.java))
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun doDelete(view: View) {
        showDelMenu()
    }

    fun doWaze(view: View) {
        if (app?.sinInternet()!!) return

        var ii=0

        if (ii==0) {
            Waze(14.617162,-90.529535)
        } else {
            toast("Espere . . .")
            try {
                http?.url = gl?.urlbase + "api/Orden/GetOrdenClienteCoord?pCliente=" + idcliente

                val request: Request = Request.Builder()
                    .url(http?.url!!).get()
                    .addHeader("accept", "*/*")
                    .build()

                http!!.processRequest(request, { cbCoordenadas() })
            } catch (e: java.lang.Exception) {
                msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message);
            }
        }
    }

    fun doPhone(view: View) {
        try {
            if (phoneNum.isNotEmpty()) {
                toast("Llamando . . .")
                makePhoneCall(phoneNum)
            } else {
                toastlong("Número incorrecto");
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun doExit(view: View) {
        try {
            cap.nota=""+txt1?.text?.toString()!!
            OrdenenccapObj?.update(cap)
        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }

        try {
            runOnUiThread({
                pbar?.isVisible=true;relbot?.isVisible=true
            })

            actualizaImagenes()
        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }

        finish()
    }

    fun setHandlers() {
        try {

            recview?.addOnItemTouchListener(
                RecyclerItemClickListener(this, recview!!,
                    object : RecyclerItemClickListener.OnItemClickListener {

                        override fun onItemClick(view: View, position: Int) {
                            saveselidx=position
                            actualizaDetalle(saveselidx)
                        }

                        override fun onItemLongClick(view: View?, position: Int) { }
                    })
            )

            txt1?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    txt1?.viewTreeObserver?.addOnGlobalLayoutListener {
                        val lines = txt1?.lineCount!!
                        val newHeight = (lines * txt1?.lineHeight!!) + txt1?.paddingTop!! + txt1?.paddingBottom!!
                        txt1?.layoutParams?.height = newHeight
                        txt1?.requestLayout()
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Main

    fun loadItem() {
        var s="";var ss=""

        try {

            EstadoordenObj!!.fill()
            TiposervicioObj!!.fill()
            cargaCap()

            OrdenencObj?.fill("WHERE (idorden="+idorden+")")
            enc=OrdenencObj?.first()!!

            lbltit?.text="Orden #"+enc.numero!!+"  "
            lbl4?.text=du?.sfecha(enc.fecha!!)

            ss=du?.shora(enc.hora_ini!!).toString()
            s= du?.shora(enc.hora_fin!!).toString()
            if (s.isNotBlank()) ss=ss+" - "+s
            lbl8?.text=ss

            var fia=cap.fechaini;lbl11?.text="Inicio atención: "+du?.shora(fia!!).toString()
            var ffa=cap.fechafin;lbl12?.text="Fin atención: "+du?.shora(ffa!!).toString()
            if (ffa>0) {
                var mdif:Int=du?.minDiff(ffa,fia)!!
                var ht:Int=mdif / 60
                var mt=mdif-ht*60
                var smt="0"
                if (mt>9) smt=""+mt else smt="0"+mt
                lbl13?.text="Tiempo trascurrido: "+ht+":"+smt
            } else {
                lbl13?.text="Tiempo trascurrido: "
            }


            lbl6?.text=nombreTipo(enc.idtipo!!)
            lbl7?.text=enc.descripcion!!

            idestado=enc.idestado!!
            mostrarEstado()

            numord=enc.numero!!
            iddir=enc.iddir!!
            idcont=enc.idclicontact!!
            idcliente=enc.idcliente!!

            cargaCliente()
            cargaContacto()
            cargaDetalle()
            aplicaEstado()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

    }

    fun listItems() {
        try {
            adapter = LA_ordendet(ditems)
            recview?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun cargaCliente() {
        try {
            ClienteObj?.fill("WHERE (codigo_cliente="+idcliente+")")

            lbl1?.text=ClienteObj?.first()?.nombre!!
            if (!cargaDireccion()) {
                var s = ClienteObj?.first()?.direccion!! + "\n"
                s = s + ClienteObj?.first()?.telefono!!

                lbl2?.text = s
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun cargaDireccion():Boolean {
        try {
            ClientedirObj?.fill("WHERE (codigo_direccion="+iddir+")")
            if (ClientedirObj?.count!!>0) {
                var s = ClientedirObj?.first()?.referencia!! + "\n"
                s = s + ClientedirObj?.first()?.direccion!! + "\n"
                s = s + ClientedirObj?.first()?.telefono!!
                lbl2?.text = s
                return true
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
        return false
    }

    fun cargaContacto() {
        try {
            ClientecontactoObj?.fill("WHERE (codigo_cliente_contacto="+idcont+")")
            if (ClientecontactoObj?.count!!>0) {

                phoneNum=ClientecontactoObj?.first()?.telefono.toString().trim()
                phoneNum=phoneNum.replace("-","")
                phoneNum=phoneNum.replace(" ","")
                phoneNum=phoneNum.replace(".","")
                phoneNum=phoneNum.replace(",","")

                var s = ClientecontactoObj?.first()?.nombre!! + "\n"
                s = s + ClientecontactoObj?.first()?.direccion!! + "\n"
                s = s + phoneNum

                lbl3?.text = s
            } else {
                lbl3?.text = " ";phoneNum=""
            }

        } catch(e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun cargaDetalle() {
        try {
            ditems.clear()

            OrdendetObj?.fill("WHERE (idOrden="+idorden+")")
            if (OrdendetObj?.count!!>0) {
                recview?.visibility=View.VISIBLE
                for (itm in OrdendetObj?.items!!) {
                    ditems.add(itm)
                }
                listItems()
            } else {
                recview?.visibility=View.GONE
            }
        } catch(e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun cargaCap() {

        try {
            var ocap= clsClasses.clsOrdenenccap(idorden, 0, 1, 8, "", "", 0.0, 0.0, 0L, 0L, "", 0)
            OrdenenccapObj?.add(ocap)
        } catch (e: Exception) {  }

        try {
            OrdenenccapObj?.fill("WHERE idorden="+idorden)
            cap=OrdenenccapObj?.first()!!

            //gl?.gpslat=cap?.latit!!
            //gl?.gpslong=cap?.longit!!

            txt1?.setText(""+cap.nota.toString()!!)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

    }

    fun iniciarOrden() {
        try {
            db!!.beginTransaction()

            enc.idestado=4
            OrdenencObj?.update(enc)

            cargaCap()

            cap.activa=1
            cap.cerrada=0
            cap.latit=gl?.gpslat!!
            cap.longit=gl?.gpslong!!
            cap.fechaini=du?.actDateTime!!
            var fia=cap.fechaini;lbl11?.text="Inicio atención: "+du?.shora(fia!!).toString()
            cap.nota=""+txt1?.text?.toString()!!

            OrdenenccapObj?.update(cap)

            db!!.setTransactionSuccessful()
            db!!.endTransaction()

            idestado=enc.idestado
            mostrarEstado()

            var fs=du?.univfecha(du?.actDateTime!!)
            sql=app?.buildEncUpdate(cap,enc.idestado,fs!!)!!
            var csql=buildCoordUpdate(cap)

            //sendUpdate(sql!!,csql,false)

            fbsItem.estado = "En proceso"
            fbsItem.inicio = du?.actDateTime!!
            fbsa?.setItem(fbsItem!!)

        } catch (e: java.lang.Exception) {
            db!!.endTransaction()
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun completarOrden() {
        try {
            db!!.beginTransaction()

            enc.idestado=5
            OrdenencObj?.update(enc)

            cargaCap()

            cap.activa=1
            cap.cerrada=1
            cap.fechafin=du?.actDateTime!!
            cap.nota=""+txt1?.text?.toString()!!
            cap.latit=gl?.gpslat!!
            cap.longit=gl?.gpslong!!

            OrdenenccapObj?.update(cap)

            actualizaInventario()

            db!!.setTransactionSuccessful()
            db!!.endTransaction()

            idestado=enc.idestado
            mostrarEstado()

            sendOrdenCompleto()

            fbsItem.estado = "Completo"
            fbsItem.fin = du?.actDateTime!!

            fbsa?.delItem(fbsItem.id)
            fbsc?.setItem(fbsItem!!)

        } catch (e: java.lang.Exception) {
            db!!.endTransaction()
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun cerrarOrden() {
        try {
            db!!.beginTransaction()

            enc.idestado=6
            OrdenencObj?.update(enc)

            cargaCap()

            cap.activa=0
            cap.cerrada=1
            cap.fechafin=du?.actDateTime!!
            cap.nota=""+txt1?.text?.toString()!!

            OrdenenccapObj?.update(cap)

            db!!.setTransactionSuccessful()
            db!!.endTransaction()

            idestado=enc.idestado
            mostrarEstado()

            sendOrdenCompleto()
            /*
            var commitflag=false

            var fs=du?.univfecha(du?.actDateTime!!)
            var sqle=app?.buildEncUpdate(cap,enc.idestado,fs!!)!!+";"
            var sqld=updateDetaille()
            var sqls=updateSerial()

            sql=sqle
            if (sqld!="#") {
                sql=sql+sqld;commitflag=true
            }
            if (sqls!="#") {
                sql=sql+sqls;commitflag=true
            }

            if (commitflag) {
                sendCommit(sql!!)
            } else {
                sendUpdate(sqle!!,"",true)
            }
            */

            fbsItem.estado = "Cerrado"
            fbsItem.fin = du?.actDateTime!!

            fbsa?.delItem(fbsItem.id)
            fbsc?.setItem(fbsItem!!)

        } catch (e: java.lang.Exception) {
            db!!.endTransaction()
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun actualizaInventario() {
        var item : clsClasses.clsExistencia

        OrdendetObj?.fill("WHERE (idOrden="+idorden+")")
        if (OrdendetObj?.count!!>0) {

            for (itm in OrdendetObj?.items!!) {
                item = clsClasses.clsExistencia()

                item.codigo =itm?.idproducto!!
                item.nombre = itm?.descripcion!!
                item.cant = -itm?.cant!!

                try {
                    ExistenciasObj?.add(item)
                } catch (e: Exception){
                    ExistenciasObj?.fill("WHERE codigo="+item.codigo)
                    var existant:Double=ExistenciasObj?.first()?.cant!!
                    item.cant=existant+item.cant
                    ExistenciasObj?.update(item)
                }

            }

        }
    }

    fun borrarOrden() {
        var fotos = ArrayList<String>()

        try {

            db!!.beginTransaction()

            var OrdenfotoObj= clsOrdenfotoObj(this,Con!!,db!!)
            OrdenfotoObj?.fill("WHERE (idOrden="+idorden+") ")
            for (itm in OrdenfotoObj?.items!!) {
                fotos.add(itm.nombre)
            }

            db?.execSQL("DELETE FROM Ordenenc WHERE (idorden="+idorden+")");
            db?.execSQL("DELETE FROM Ordendet WHERE (idorden="+idorden+")");
            db?.execSQL("DELETE FROM Ordenenccap WHERE (idorden="+idorden+")");
            db?.execSQL("DELETE FROM Ordenfoto WHERE (idorden="+idorden+")");

            db!!.setTransactionSuccessful()
            db!!.endTransaction()

            fbsa?.delItem(idorden)

            for (itm in fotos) {
                var ffile = File(gl?.picdir.toString()+itm.toString())
                try {
                    ffile.delete()
                } catch (e: Exception) {}
            }

            toastlong("Servicio borrado.")

            finish()
        } catch (e: java.lang.Exception) {
            db!!.endTransaction()
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }

    }

    fun anularOrden() {
        var fotos = ArrayList<String>()

        try {

            db!!.beginTransaction()

            var OrdenfotoObj= clsOrdenfotoObj(this,Con!!,db!!)
            var EnvioimagenObj= clsEnvioimagenObj(this,Con!!,db!!)

            OrdenfotoObj?.fill("WHERE (idOrden="+idorden+") ")

            for (itm in OrdenfotoObj?.items!!) {
                fotos.add(itm.nombre)
            }

            db?.execSQL("DELETE FROM Ordenenc WHERE (idorden="+idorden+")");
            db?.execSQL("DELETE FROM Ordendet WHERE (idorden="+idorden+")");
            db?.execSQL("DELETE FROM Ordenenccap WHERE (idorden="+idorden+")");
            db?.execSQL("DELETE FROM Ordenfoto WHERE (idorden="+idorden+")");

            for (itm in fotos) {
                var eitem= clsClasses.clsEnvioimagen(itm.toString(),0)
                try {
                    EnvioimagenObj.add(eitem)
                } catch (e: Exception) {
                    EnvioimagenObj.update(eitem)
                }
            }

            db!!.setTransactionSuccessful()
            db!!.endTransaction()

            for (itm in fotos) {
                var ffile = File(gl?.picdir.toString()+itm.toString())
                try {
                    ffile.delete()
                } catch (e: Exception) {}
            }

            sendUpdateAnul("UPDATE D_ORDEN_SERVICIO_ENC SET CODIGO_ESTADO_ORDEN_SERVICIO=6 " +
                    "WHERE (CODIGO_ORDEN_SERVICIO="+idorden+")")

            try {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    startActivity(Intent(this, EnvioImagenes::class.java))
                }, 200)
            } catch (e: Exception) {
                msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            }

            toast("Servicio cerrado.")

            actualizaImagenes()
            finish()
        } catch (e: java.lang.Exception) {
            db!!.endTransaction()
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }

    }

    fun cbServico() {
        var exist=false

        try {
            if (fbsa?.errflag!!) throw Exception(fbsa?.value!!)
            exist=fbsa?.exist==1

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

        if (exist) {
            fbsItem=fbsa?.litem!!
        } else {
            fbsItem.id = idorden
            fbsItem.numero = numord
            fbsItem.cliente = lbl1?.text?.toString()!!
            fbsItem.estado = "Nuevo"
            fbsItem.fin = 0
            fbsItem.inicio = 0
            fbsItem.user = gl?.nuser!!
        }
    }

    fun actualizaDetalle(dpos: Int ) {
        try {
            var completo=ditems.get(dpos).realizado
            var activo=ditems.get(dpos).activo
            var anulado=ditems.get(dpos).idnoaten

            gl?.gstr =lbltit?.text?.toString()!!
            gl?.gint2=ditems.get(dpos).id
            gl?.gint3=ditems.get(dpos).idorden

            if (anulado==0) {
                if (completo==0) {
                    if (activo==1) {
                        callback=1
                        startActivity(Intent(this,OrdenDet::class.java))
                    } else {
                        msgask(7,"¿Iniciar tarea?")
                    }
                } else {
                    callback=1
                    startActivity(Intent(this,OrdenDet::class.java))
                }
            } else {
                msgask(7,"¿Iniciar tarea?")
            }

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun actualizaEstadoDetalle() {
        try {
            if (gl?.gintval!!<=0) return

            ditems.get(saveselidx).realizado=1
            ditems.get(saveselidx).cant=gl?.gint?.toDouble()!!

            OrdendetObj?.update(ditems.get(saveselidx))
            adapter?.notifyDataSetChanged()

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun iniciaEstadoDetalle() {
        try {

            if (idestado<4) {
                idestado=4
                iniciarOrden()
            }

            ditems.get(saveselidx).activo=1
            ditems.get(saveselidx).realizado=0
            ditems.get(saveselidx).horaini=du?.actDateTime!!
            ditems.get(saveselidx).horafin=0
            ditems.get(saveselidx).idnoaten=0

            OrdendetObj?.update(ditems.get(saveselidx))

            callback=1
            startActivity(Intent(this,OrdenDet::class.java))
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun resetEstadoDetalle() {
        try {
            /*
            ditems.get(saveselidx).realizado=0
            OrdendetObj?.update(ditems.get(saveselidx))
            adapter?.notifyDataSetChanged()

            db?.execSQL("DELETE FROM Ordenserial WHERE (idordendet="+gl?.gint2+")")

             */
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Envio

    fun sendOrdenCompleto() {
        try {
            updord =clsOrdenUpdate(this, gl?.urlbase!! ,Con!!, db!!)
            updord?.updateOrden(idorden, { cbOrdenCompleto() })
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun cbOrdenCompleto() {
        try {
            if (updord?.errflag!!) throw Exception(updord?.error!!)

            Thread {
                Handler(Looper.getMainLooper()).post {
                    updateEstadoEnvio()
                }
            }.start()

        } catch (e: Exception) {
            Thread { Handler(Looper.getMainLooper()).post {
                msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            } }.start()
        }

    }

    fun sendUpdate(usql:String,csql:String,close: Boolean) {
        try {
            sqlsave=usql

            val jupd=clsClasses.clsUpdate(usql)
            val pbody = gson.toJson(jupd)
            val body: RequestBody = pbody.toRequestBody(gl?.mediaType)

            http?.url=gl?.urlbase+"api/Orden/Update"

            val request: Request = Request.Builder()
                .url(http?.url!!)
                .post(body)
                .addHeader("accept", "*/*")
                .build()

            http!!.processRequest(request, { cbUpdate(csql , close) })
        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message);
        }
    }

    fun cbUpdate(csql:String,close: Boolean) {
        try {
            if (http?.retcode != -1) {
                if (http?.data!! == "#") {
                    cap.recibido = 1
                    OrdenenccapObj?.update(cap)
                }
            } else {
                var newid=UpdsaveObj?.newID("SELECT MAX(id) FROM Updsave")
                sqlsave=sqlsave.replace("'","´")
                var uitem= clsClasses.clsUpdsave(newid!!, sqlsave)
                UpdsaveObj?.add(uitem)
            }
        } catch (e: java.lang.Exception) {
            runOnUiThread { toast(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message) }
        }

        if (csql.isNotEmpty()) {
            sendUpdateCoord(csql,close)
        } else {
            if (close) updateEstadoEnvio()
        }
    }

    fun sendCommit(usql:String) {
        try {
            //val cmd=usql!!.replace("´","'")
            val cmd=usql!!
            val jupd=clsClasses.clsUpdate(cmd)
            val pbody = gson.toJson(jupd)
            val body: RequestBody = pbody.toRequestBody(gl?.mediaType)

            http?.url=gl?.urlbase+"api/Orden/Commit"

            val request: Request = Request.Builder()
                .url(http?.url!!)
                .post(body)
                .addHeader("accept", "*/*")
                .build()

            http!!.processRequest(request, { cbCommit() })
        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message);
        }
    }

    fun cbCommit() {
        try {
            var retcode=http?.retcode!!
            var retmsg=http?.data.toString()
            if (retcode>-1) {
                try {
                    //if (retcode==0) addToSyntaxLog(selcmd.cmd)
                    //UpdcmdObj?.delete(selcmd)

                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed( { updateEstadoEnvio() }, 200)

                } catch (e: Exception) { }
            }
        } catch (e: java.lang.Exception) {
            runOnUiThread { toast(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message) }
        }
    }

    fun updateEstadoEnvio() {
        try {
            cap.activa = 2
            cap.recibido = 1
            OrdenenccapObj?.update(cap)

            actualizaImagenes()
            finish()
        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun sendUpdateCoord(csql:String,close: Boolean) {
        try {
            sqlsave=csql

            val jupd=clsClasses.clsUpdate(csql)
            val pbody = gson.toJson(jupd)
            val body: RequestBody = pbody.toRequestBody(gl?.mediaType)

            http?.url=gl?.urlbase+"api/Orden/Update"

            val request: Request = Request.Builder()
                .url(http?.url!!)
                .post(body)
                .addHeader("accept", "*/*")
                .build()

            http!!.processRequest(request, { cbUpdateCoord(close) })
        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message);
        }
    }

    fun cbUpdateCoord(close: Boolean) {
        try {
            if (http?.retcode == -1) {
                var newid=UpdsaveObj?.newID("SELECT MAX(id) FROM Updsave")
                sqlsave=sqlsave.replace("'","´")
                var uitem= clsClasses.clsUpdsave(newid!!, sqlsave)
                UpdsaveObj?.add(uitem)
            }
        } catch (e: java.lang.Exception) {
            runOnUiThread { toast(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message) }
        }

        if (close)  updateEstadoEnvio()
    }

    fun sendUpdateAnul(usql:String) {
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

            http!!.processRequest(request, { cbUpdateAnul() })
        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message);
        }
    }

    fun cbUpdateAnul() { }

    fun actualizaImagenes() {
        try {
            var EnvioimagenObj= clsEnvioimagenObj(this,Con!!,db!!)
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

    //endregion

    //region Dialogs

    fun dialogswitch() {
        try {
            when (gl?.dialogid) {
                0 -> { iniciarOrden() }
                1 -> { completarOrden() }
                2 -> { msgask(3,"Está seguro?") }
                3 -> { borrarOrden() }
                4 -> { startActivity(Intent(this, Firma::class.java)) }
                5 -> { msgask(6,"Está seguro?") }
                6 -> { completarOrden() }
                7 -> { iniciaEstadoDetalle() }
                8 -> { msgask(9,"Está seguro?") }
                9 -> { cerrarOrden() }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun processItemMenu(menuidx:Int) {
        try {
            when (menuidx) {
                1 -> {
                    callback=1
                    startActivity(Intent(this,Seriales::class.java))
                }
                2 -> { resetEstadoDetalle() }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun showDelMenu() {
        try {
            val listdlg = extListDlg();

            listdlg.buildDialog(this@Orden, "Opciones")
            listdlg.setLines(2)
            listdlg.setWidth(-1)
            listdlg.setCenterScreenPosition()

            listdlg.addData(2,"Cerrar orden")
            listdlg.addData(1,"Borrar orden")

            listdlg.clickListener= Runnable {
                //processItemMenu(listdlg.selcodint)
                when (listdlg.selcodint) {
                    1 -> { msgask(2,"Borrar servicio?") }
                    2 -> { msgask(8,"Cerrar servicio?") }
                }
            }

            listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
            listdlg.show()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

    //region Aux

    fun nombreEstado(codigo:Int):String {
        try {
            if (codigo==3) return "Pendiente"
            for (itm in EstadoordenObj?.items!!) {
                if (itm.id==codigo) return itm.nombre
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
        return "-"
    }

    fun mostrarEstado() {
        try {
            lbl5?.text=nombreEstado(idestado)
            var eres=R.drawable.color_gray_grad
            if (idestado==4) {
                eres=R.drawable.color_ocra_grad
            } else if (idestado==5) {
                eres=R.drawable.color_green_grad
            }
            lbl5?.setBackgroundResource(eres)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
        aplicaEstado()
    }

    fun nombreTipo(codigo:Int):String {
        try {
            for (itm in TiposervicioObj?.items!!) {
                if (itm.codigo_tipo_orden_servicio==codigo) return itm.nombre
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
        return "-"
    }

    fun aplicaEstado() {
        val estlist=listOf(2,3,4)
        var vis=false

        if (idestado in estlist) vis=true
        vis=true
        imgnext?.isVisible=vis


        if (idestado==4) {
            imgfoto?.setImageResource(R.drawable.btn_photo)
            imgsign?.setImageResource(R.drawable.btn_sign)
            imgnext?.setImageResource(R.drawable.btn_save_blue)
        } else {
            imgfoto?.setImageResource(R.drawable.blank)
            imgsign?.setImageResource(R.drawable.blank)
            imgnext?.setImageResource(R.drawable.arr_next)
        }
    }

    fun cbCoordenadas() {
        var jss: ClassesAPI.clsAPICoord? = null
        var coorx=0.0
        var coory=0.0

        try {
            Looper.prepare()

            if (http!!.retcode!=1) {
                toast("Error: "+http!!.data);return
            }

            val parsedList =http?.splitJsonArray()
            val RType = object : TypeToken<ClassesAPI.clsAPICoord>() {}.type

            for (pls in parsedList!!) {
                jss=gson.fromJson(pls, RType)
                coorx=jss?.COORX!!
                coory=jss?.COORY!!
            }

            if (coorx==0.0 || coory==0.0) {
                msgbox("¡El cliente no tiene coordenadas!");return
            }

            try {
                val wazeUri = Uri.parse("https://waze.com/ul?ll="+coory+","+coorx+"&navigate=yes")
                val intent = Intent(Intent.ACTION_VIEW, wazeUri)
                intent.setPackage("com.waze")

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    msgbox("Falta instalar Waze")
                }
            } catch (e: Exception) {
                msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            }

        } catch (e: java.lang.Exception) {
            var es=e.message
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message);
        }
    }

    fun Waze( latitude: Double, longitude: Double) {
        //14.617162 , -90,529535
        try {
            val uri = "https://waze.com/ul?ll=$latitude,$longitude&navigate=yes"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.waze")
            this.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            try {
                // Waze is not installed, open in a browser as fallback
                val fallbackUri = "https://waze.com/ul?ll=$latitude,$longitude&navigate=yes"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUri))
                this.startActivity(intent)
            } catch (e: Exception) {
                msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            }

        }
    }

    fun buildCoordUpdate(cap: clsClasses.clsOrdenenccap):String {
        if (cap.longit==0.0) return ""

        try {
            upd!!.init("P_CLIENTE")
            upd!!.add("COORX",cap.longit)
            upd!!.add("COORY",cap.latit)
            upd!!.Where("(CODIGO_CLIENTE=" + idcliente + ")")

            return upd!!.sql()
        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
        return ""
    }

    fun makePhoneCall(phoneNumber: String) {
        try {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(callIntent)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun tieneFirma():Boolean {
        try {
            OrdenenccapObj?.fill("WHERE (idOrden="+idorden+")")
            return (OrdenenccapObj?.first()?.firmacliente?.isNotEmpty()!!)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message);return false
        }
        return false
    }

    fun validaFirma() {
        try {
            if (idestado==4) {
                if (tieneFirma()) {
                    imgsign?.setImageResource(R.drawable.btn_sign_check)
                } else {
                    imgsign?.setImageResource(R.drawable.btn_sign)
                }
            } else {
                imgsign?.setImageResource(R.drawable.blank)
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun validaDetalle():Int {
        try {
            OrdendetObj?.fill("WHERE (idOrden="+idorden+") AND (realizado=0)")
            if (OrdendetObj?.count==0) return 1 else return 0
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message);return -1
        }

    }

    @SuppressLint("MissingPermission")
    fun capturaGPS() {

        try {
               fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    try {
                        location?.let {
                            gl?.gpslong= it.longitude
                            gl?.gpslat=it.latitude

                        } ?: run {
                            msgbox("No se pudo obtener ubicación")
                        }
                    } catch (e: Exception) {
                        msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
                    }
                }.addOnFailureListener { e ->
                    msgbox("Error al obtener ubicación: ${e.message}")
                }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun testUpdate() {

        try {
            updord =clsOrdenUpdate(this, gl?.urlbase!! ,Con!!, db!!)
            updord?.updateOrden(idorden, { receiveTestUpdate() })
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun receiveTestUpdate() {
        try {
            if (updord?.errflag!!) throw Exception(updord?.error!!)

            Thread {
                Handler(Looper.getMainLooper()).post {msgbox("OK") }
            }.start()
        } catch (e: Exception) {
            Thread { Handler(Looper.getMainLooper()).post {
                msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            } }.start()
        }
    }

    //endregion

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }

            EstadoordenObj!!.reconnect(Con!!, db!!)
            OrdenencObj!!.reconnect(Con!!, db!!)
            TiposervicioObj!!.reconnect(Con!!, db!!)
            ClienteObj!!.reconnect(Con!!, db!!)
            ClientedirObj!!.reconnect(Con!!, db!!)
            ClientecontactoObj!!.reconnect(Con!!, db!!)
            OrdenenccapObj!!.reconnect(Con!!, db!!)
            OrdendetObj!!.reconnect(Con!!, db!!)
            UpdsaveObj!!.reconnect(Con!!, db!!)
            ExistenciasObj!!.reconnect(Con!!, db!!)
            OrdenserialObj!!.reconnect(Con!!, db!!)

            cargaDetalle()

            validaFirma()

            if (callback==1) {
                callback=0
                //actualizaEstadoDetalle()
                return
            }

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}