package com.dts.osm

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.dts.classes.clsTiposerviciosObj
import com.dts.classes.extListDlg

class Lista : PBase() {



    var TiposervicioObj: clsTiposerviciosObj? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_lista)

            super.initbase(savedInstanceState)

            TiposervicioObj = clsTiposerviciosObj(this, Con!!, db!!)


        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }

    //region Events

    fun doInventario(view: View) {
        try {
             startActivity(Intent(this,InvLista::class.java))
            //startActivity(Intent(this,UsuariosPrueba::class.java))
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun doCom(view: View) {
        try {
            startActivity(Intent(this,Com::class.java))
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun doCliente(view: View) {
        try {
            //startActivity(Intent(this,CliNuevo::class.java))
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun doNuevo(view: View) {
        try {
            listaTipos()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Main


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

    fun listaTipos() {
        try {

            TiposervicioObj?.fill("ORDER BY nombre")
            if (TiposervicioObj?.count==0) {
                msgbox("No está definido ninguna tipo de servicio, no se puede continuar");return;
            }

            val listdlg = extListDlg();

            listdlg.buildDialog(this,"Tipo de servicio")
            //listdlg.setLines(4);
            listdlg.setCenterScreenPosition()
            listdlg.setWidth(10000)

            for (itm in TiposervicioObj?.items!!) {
                listdlg.addData(itm.codigo_tipo_departamento,itm.nombre)
            }

            listdlg.clickListener= Runnable { processDirMenu(listdlg.selcodint,listdlg.selvalue) }

            listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
            listdlg.show()

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun processDirMenu(value:Int,text:String) {
        try {

            gl?.gint=value
            gl?.gstr=text!!
            startActivity(Intent(this,TareaNueva::class.java))

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

            TiposervicioObj?.reconnect(Con!!, db!!)

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion




}