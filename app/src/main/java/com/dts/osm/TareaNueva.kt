package com.dts.osm

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView

class TareaNueva : PBase() {


    var lbltipo: TextView? = null
    var lblcli: TextView? = null

    var idcli=0
    var nomcli=""
    var idtipo=0
    var nomtipo=""
    var iddir=0
    var idcont=0
    var descrip=""

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_tarea_nueva)

            super.initbase(savedInstanceState)

            lbltipo = findViewById<View>(R.id.editTextText2) as TextView
            lblcli = findViewById<View>(R.id.editTextText) as TextView

            gl?.gnota=""
        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }


    //region Events

    fun doCliente(view: View) {
        gl?.gint=0
        callback=1
        startActivity(Intent(this,Clientes::class.java))
    }

    fun doSave(view: View) {
        if (checkValues()) save()
    }

    fun doNote(view: View) {
        showDescDialog() { text ->
            descrip=text
        }
    }

    fun doExit(view: View) {
        finish()
    }

    //endregion

    //region Main

    fun save() {
        try {

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
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

    fun showDescDialog( onTextEntered: (String) -> Unit) {
        var editText = EditText(this).apply {
            setText(descrip)
            requestFocus()
            setSelection(descrip.length)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            minLines = 4
            maxLines = 6
        }

        editText.filters = arrayOf(InputFilter.LengthFilter(490))
        editText.isVerticalScrollBarEnabled = true
        editText.isHorizontalScrollBarEnabled = false

        AlertDialog.Builder(this)
            .setTitle("Descripción")
            .setView(editText)
            .setPositiveButton("Guardar") { _, _ ->
                onTextEntered(editText.text.toString())
            }
            .setNegativeButton("Salir", null)
            .show()
    }


    //endregion

    //region Aux

    fun checkValues() : Boolean {
        if (idcli==0) {
            msgbox("Falta seleccionar cliente.");return false
        }
        if (idtipo==0) {
            msgbox("Falta seleccionar tipo de servicio.");return false
        }

        if (iddir==0) {
            msgbox("Falta seleccionar una dirección.");return false
        }

        if (idcont==0) {
            msgbox("Falta seleccionar un contacto.");return false
        }

        return true
    }

    //endregion

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }

            if (callback==1) {
                callback=0
                if (gl?.gint!=0) {

                    idcli=gl?.gint!!
                    nomcli=gl?.gstr!!
                    iddir=gl?.gint2!!
                    idcont=gl?.gint3!!

                    lblcli?.text=nomcli
                }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}