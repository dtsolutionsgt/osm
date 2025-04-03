package com.dts.osm

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.base.clsClasses.clsOrdenserial
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.clsOrdenserialObj
import com.dts.ladapt.LA_ProductoAdapter
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage


class Seriales : PBase() {

    var recview: RecyclerView? = null
    var txtcant: EditText? = null
    var lbltit: TextView? = null

    var OrdenserialObj: clsOrdenserialObj? = null

    var adapter: LA_ProductoAdapter? = null

    var items = ArrayList<clsClasses.clsProducto>()
    var item  = clsClasses.clsProducto()

    var scant=0
    var cant=0
    var idorden=0
    var idordendet=0
    var selindex=0
    var selserial=""

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_seriales)

            super.initbase(savedInstanceState)

            recview = findViewById<View>(R.id.recview) as RecyclerView
            recview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            lbltit = findViewById(R.id.textView15)
            txtcant = findViewById(R.id.editTextNumber);

            OrdenserialObj = clsOrdenserialObj(this, Con!!, db!!)

            loadItem()

            setHandlers()

        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }


    //region Events

    fun doAdd(view: View) {
        if (items.size>=cantProd()) {
            msgbox("No se permite cantidad de seriales mayor a cantidad de productos. ");return
        }

        callback=1
        gl?.barcode=""
        startActivity(Intent(this,Scan::class.java))
    }

    fun doInput(view: View) {
        if (items.size>=cantProd()) {
            msgbox("No se permite cantidad de seriales mayor a cantidad de productos. ");return
        }

        inputSerial()
    }

    fun doSave(view: View) {
        if (items.size>cantProd()) {
            msgbox("No se permite cantidad de seriales mayor a cantidad de productos. ");return
        }

        save()
    }

    fun doExit(view: View) {
        msgask(0,"Salir sin guardar?")
    }

    private fun setHandlers() {
        try {
            recview?.addOnItemTouchListener(
                RecyclerItemClickListener(this, recview!!,
                    object : RecyclerItemClickListener.OnItemClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            selindex = position
                            selserial = items?.get(selindex)?.desclarga!!
                            msgask(1,"Borrar "+selserial + " ?")
                        }
                        override fun onItemLongClick(view: View?, position: Int) {}
                    })
            )

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)

        }
    }

    //endregion

    //region Main

    fun loadItem() {
        try {
            cant=gl?.gint!!
            idorden=gl?.gint3!!
            idordendet=gl?.gint2!!

            lbltit?.text=gl?.gstr!!
            txtcant?.setText(""+cant)

            items.clear()
            OrdenserialObj?.fill("WHERE (idordendet="+idordendet+") ")
            for (itm in OrdenserialObj?.items!!) {
                item = clsClasses.clsProducto()

                item.desclarga=itm.serial
                item.codigo_tipo=""
                item.codigo_producto=itm.idordendet

                items.add(item)
            }

            listItems()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun listItems() {
        var scridx=-1
        var ii=0

        try {
            items.sortBy { it.desclarga }

            if (selserial.isNotEmpty()) {
                for (itm in items) {
                    if (itm.desclarga==selserial) scridx=ii
                    ii++
                }
            }

            adapter = LA_ProductoAdapter(items)
            recview?.adapter = adapter

            if (scridx>-1) {
                adapter?.setSelectedItem(scridx);
                recview?.smoothScrollToPosition(scridx);
            }

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun save() {
        var serial = clsOrdenserial()

        try {
            db!!.beginTransaction()

            db?.execSQL("DELETE FROM Ordenserial WHERE (idordendet="+idordendet+") ")

            for (itm in items) {

                serial = clsOrdenserial()

                serial.idorden = idorden
                serial.idordendet = idordendet
                serial.serial = itm.desclarga

                OrdenserialObj?.add(serial)

            }

            db!!.setTransactionSuccessful()
            db!!.endTransaction()

            gl?.gint=cantProd()
            gl?.gintval=1
            finish()
        } catch (e: java.lang.Exception) {
            db!!.endTransaction()
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun addItem(sn: String) {
        try {


            for (itm in items) {
                if (itm.desclarga==sn) {
                    selserial=sn
                    toast("Serial ya existe.")
                    return
                }
            }

            item = clsClasses.clsProducto()
            item.desclarga=sn
            item.codigo_producto=idordendet
            item.codigo_tipo = ""
            items.add(item)

            selserial=sn
            listItems()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun delItem() {
        try {
            items.removeAt(selindex)
            selserial=""
            listItems()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Dialogs

    fun dialogswitch() {
        try {
            when (gl?.dialogid) {
                0 -> {
                    gl?.gintval=0
                    finish()
                }
                1 -> { delItem() }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    private fun inputSerial() {
        val alert: AlertDialog.Builder = AlertDialog.Builder(this)
        alert.setTitle("Número serial")
        val input = EditText(this)

        alert.setView(input)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText("")
        input.requestFocus()

        alert.setPositiveButton("Aplicar",
            DialogInterface.OnClickListener { dialog, whichButton ->
                try {
                    val sn = input.text.toString()
                    if (sn.isEmpty()) throw Exception()
                    if (sn.length<5) throw Exception()

                    addItem(sn)
                } catch (e: java.lang.Exception) {
                    mu!!.msgbox("Valor incorrecto")
                    return@OnClickListener
                }
            })

        alert.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, whichButton -> })

        val dialog: AlertDialog = alert.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
    }

    //endregion

    //region Aux

    fun cantProd(): Int {
        try {
            var vcant= txtcant?.getText()?.toString()?.toInt()!!
            return vcant
        } catch (e: Exception) {
            msgbox(" Cantidad incorrecta ")
            return -1
        }
    }


    //endregion

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }

            OrdenserialObj!!.reconnect(Con!!, db!!)

            if (callback==1) {
                callback=0
                if (gl?.barcode?.isNotEmpty()!!) {
                    toast(gl?.barcode)
                    addItem(gl?.barcode!!)
                }
            }

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}