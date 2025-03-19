package com.dts.osm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.classes.clsExistenciaObj
import com.dts.classes.clsProductoObj
import com.dts.ladapt.LA_ProductoAdapter

class InvLista : PBase() {


    private var ExistenciasObj: clsExistenciaObj? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_inv_lista)

            super.initbase(savedInstanceState)
            ExistenciasObj = clsExistenciaObj(this, Con!!, db!!)

            ExistenciasObj = clsExistenciaObj(this, Con!!, db!!)


        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }

    //region Events

    fun doEgreso(view: View) {
        try {
            //startActivity(Intent(this,InvEgreso::class.java))
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun doAjuste(view: View) {
        try {
            //startActivity(Intent(this,InvAjuste::class.java))
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun doIngreso(view: View) {
        try {

            callback=1
            startActivity(Intent(this,Productos::class.java))
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun doExit(view: View) {
        finish()
    }

    //endregion

    //region Main
    fun agregar(){
        try {
            var item = clsClasses.clsExistencia()
                item.codigo = gl?.gint!!
                item.nombre = gl?.gstr!!
                item.cant = gl?.gcant!!

            try {
                //no existe en la tabla aún
              ExistenciasObj?.add(item)
            } catch (e: Exception){
                ExistenciasObj?.fill("WHERE codigo="+item.codigo)
                var existant=ExistenciasObj?.first()?.cant!!
                item.cant=item.cant+existant
                ExistenciasObj?.update(item)
            }


        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
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

            ExistenciasObj!!.reconnect(Con!!, db!!)

            if(callback==1){
                callback=0
                if(gl?.gint!=0){
                    agregar()
                }
            }

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}