package com.dts.osm

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.dts.base.clsClasses
import com.dts.classes.clsOrdendetObj
import com.dts.classes.clsProductoObj
import com.dts.classes.clsRazon_fallaObj
import com.dts.classes.clsRazon_no_atencionObj
import com.dts.classes.clsTiposervicioObj
import com.dts.classes.extListDlg


class OrdenDet : PBase() {

    var lbltit: TextView? = null
    var lblest: TextView? = null
    var lbl1: TextView? = null
    var lbl2: TextView? = null
    var lbl3: TextView? = null
    var lbl4: TextView? = null
    var lblser: TextView? = null
    var txtcant: EditText? = null
    var relser: RelativeLayout? = null
    var imgscan: ImageView? = null

    var OrdendetObj : clsOrdendetObj? = null

    var ditem = clsClasses.clsOrdendet()

    var id = 0
    var idorden = 0
    var ptipo = ""
    var serial = ""
    var cant = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_orden_det)

            super.initbase(savedInstanceState)

            lbltit = findViewById(R.id.textView15)
            lblest = findViewById(R.id.textView22)
            lbl1 = findViewById(R.id.textView18)
            lbl2 = findViewById(R.id.textView12)
            lbl3 = findViewById(R.id.textView17)
            lbl4 = findViewById(R.id.textView20)
            lblser = findViewById(R.id.textView51)
            txtcant = findViewById(R.id.editTextNumber2)
            relser = findViewById(R.id.relserial)
            imgscan = findViewById(R.id.imageView52)

            id=gl?.gint2!!

            OrdendetObj = clsOrdendetObj(this, Con!!, db!!)

            loadItem()
            setHandlers()
        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }

    //region Events

    fun doSave(view: View) {
       msgask(0,"¿Completar tarea?")
    }

    fun doExit(view: View) {
        exitItem()
    }

    fun doScan(view: View) {
        callback=1
        gl?.barcode=""
        startActivity(Intent(this,Scan::class.java))
    }

    fun doAnul(view: View) {
        msgask(1,"¿Está seguro de anular tarea?")
    }

    private fun setHandlers() {
        try {

            imgscan?.setOnLongClickListener {
                showScanMenu()
                true
            }

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }
    //endregion

    //region Main

    fun loadItem() {
        try {

            lblest?.text="";lbl1?.text="";lbl2?.text=""
            lbl3?.text="";lbl4?.text="";lblser?.text=""
            txtcant?.setText("0");relser?.isVisible=false

            idorden=gl?.gint3!!
            lbltit?.text =lbltit?.text?.toString()!!

            OrdendetObj?.fill("WHERE (ID="+id+")")
            ditem=OrdendetObj?.first()!!
            serial = ""+ditem?.serial!!
            lbl3?.text=ditem?.descripcion
            lblser?.text = serial
            txtcant?.setText(""+mu?.frmdecno2(ditem?.cant!!))
            lblest?.text=getStatName(ditem)
            lblest?.setBackgroundResource(getStatRes(ditem))

            var Razon_fallaObj = clsRazon_fallaObj(this, Con!!, db!!)
            Razon_fallaObj?.fill("WHERE (codigo_razon_falla="+ditem?.idrazonfalla+")")
            var ritem=Razon_fallaObj?.first()
            lbl2?.text=ritem?.descripcion!!

            var TiposervicioObj = clsTiposervicioObj(this, Con!!, db!!)
            TiposervicioObj?.fill("WHERE (codigo_tipo_orden_servicio="+ritem?.codigo_tipo_orden_servici+")")
            var titem=TiposervicioObj?.first()
            lbl1?.text=titem?.nombre!!

            var ProductoObj = clsProductoObj(this, Con!!, db!!)
            ProductoObj?.fill("WHERE (codigo_producto="+ditem?.idproducto+")")
            var pitem=ProductoObj?.first()
            lbl4?.text=pitem?.desclarga
            ptipo=pitem?.codigo_tipo!!

            relser?.isVisible=ptipo=="S"
            txtcant?.isEnabled=ptipo=="C"

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun exitItem() {
        try {
            if (!checkValues()) return

            ditem?.cant = cant
            ditem?.serial = serial

            OrdendetObj?.update(ditem)

            finish()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun saveItem() {
        try {
            if (!checkValues()) return

            ditem?.activo=1
            ditem?.realizado=1
            ditem?.horafin=du?.actDateTime!!
            ditem?.idnoaten=0
            ditem?.cant = cant
            ditem?.serial = serial

            OrdendetObj?.update(ditem)

            finish()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun resetItem() {
        try {
            ditem?.activo=0
            ditem?.realizado=0
            ditem?.horaini=0
            ditem?.horafin=0

            OrdendetObj?.update(ditem)

            finish()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun anulItem(cna : Int) {
        try {
            ditem?.activo=0
            ditem?.realizado=0
            ditem?.horaini=0
            ditem?.horafin=du?.actDateTime!!
            ditem?.idnoaten=cna

            OrdendetObj?.update(ditem)

            finish()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }


    //endregion

    //region Dialogs

    fun dialogswitch() {
        try {
            when (gl?.dialogid) {
                0 -> { saveItem() }
                1 -> { showAnulMenu() }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun showScanMenu() {
        try {

            val listdlg = extListDlg();

            listdlg.buildDialog(this@OrdenDet, "Número serial")
            if (serial.isNotBlank()) listdlg.setLines(3) else listdlg.setLines(3)
            listdlg.setWidth(-1)
            listdlg.setCenterScreenPosition()

            listdlg.addData(1,"Escanear")
            listdlg.addData(2,"Ingresar")
            if (serial.isNotBlank()) listdlg.addData(3,"Borrar serial")

            listdlg.clickListener= Runnable { processScanMenu(listdlg.selcodint) }

            listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
            listdlg.show()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun processScanMenu(menuidx:Int) {
        try {
            when (menuidx) {
                1 -> {
                    callback=1
                    gl?.barcode=""
                    startActivity(Intent(this,Scan::class.java))
                }
                2 -> { inputSerial() }
                3 -> { serial="";lblser?.text=serial}

            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun showAnulMenu() {
        try {

            val listdlg = extListDlg();

            listdlg.buildDialog(this@OrdenDet, "Razón de anulación")
            if (serial.isNotBlank()) listdlg.setLines(3) else listdlg.setLines(3)
            listdlg.setWidth(-1)
            listdlg.setCenterScreenPosition()

            var Razon_no_atencionObj = clsRazon_no_atencionObj(this, Con!!, db!!)
            Razon_no_atencionObj.fill("ORDER BY DESCRIPCION")

            for (itm in Razon_no_atencionObj?.items!!) {
                listdlg.addData(itm.codigo_razon_noatencion,itm.descripcion)
            }

            listdlg.clickListener= Runnable { anulItem(listdlg.selcodint) }

            listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
            listdlg.show()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    private fun inputSerial() {
        try {
            val alert: AlertDialog.Builder = AlertDialog.Builder(this)
            alert.setTitle("# SERIAL")
            val input = EditText(this)
            alert.setView(input)
            input.setText("")
            input.requestFocus()

            alert.setPositiveButton("Aplicar",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    try {
                        val s = input.text.toString()
                        if (s.length<5) throw Exception()

                        serial=s;lblser?.text=serial
                    } catch (e: java.lang.Exception) {
                        mu!!.msgbox("Serial incorrecto")
                        return@OnClickListener
                    }
                })

            alert.setNegativeButton("Cancelar",
                DialogInterface.OnClickListener { dialog, whichButton -> })

            val dialog: AlertDialog = alert.create()
            dialog.show()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Aux

    fun checkValues() : Boolean {
        try {
            cant=txtcant?.text?.toString()?.toDouble()!!
            if (cant<=0) throw Exception()

            return true
        } catch (e: Exception) {
            msgbox("Cantidad incorrecta. ")
            txtcant?.requestFocus();txtcant?.selectAll()
        }
        return false
    }

    private fun getStatName(item: clsClasses.clsOrdendet) : String {
        var ename="Pendiente"

        if (item?.activo==1) {
            if (item?.realizado==1) {
                ename="Completo"
            } else {
                ename="En progreso"
            }
        }

        return ename
    }

    private fun getStatRes(item: clsClasses.clsOrdendet) : Int {
        var eres=R.drawable.color_gray_grad

        if (item?.activo==1) {
            if (item?.realizado==1) {
                eres=R.drawable.color_green_grad
            } else {
                eres=R.drawable.color_ocra_grad
            }
        }

        return eres
    }


    //endregion

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }

            OrdendetObj?.reconnect(Con!!, db!!)

            if (callback==1) {
                callback=0
                if (gl?.barcode?.isNotEmpty()!!) {
                    serial = ""+gl?.barcode!!
                    lblser?.text = serial
                }
            }

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}