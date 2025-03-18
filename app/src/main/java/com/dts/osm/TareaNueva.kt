package com.dts.osm

import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_tarea_nueva)

            super.initbase(savedInstanceState)

            lbltipo = findViewById<View>(R.id.editTextText2) as TextView
            lblcli = findViewById<View>(R.id.editTextText) as TextView


        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }


    //region Events

    fun doCliente(view: View) {
        callback=1
        startActivity(Intent(this,Clientes::class.java))
    }

    fun doSave(view: View) {
        if (idcli==0) {
            msgbox("Falta seleccionar cliente.");return
        }
        if (idtipo==0) {
            msgbox("Falta seleccionar tipo de servicio.");return
        }

        save()
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

    //endregion

    //region Aux


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
                    lblcli?.text=nomcli
                }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}