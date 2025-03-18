package com.dts.osm

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.clsClienteObj
import com.dts.ladapt.LA_ClienteAdapter


class Clientes : PBase() {

    var recview: RecyclerView? = null
    var txtflt: EditText? = null


    var adapter: LA_ClienteAdapter? = null

    var ClienteObj: clsClienteObj? = null

    var items = ArrayList<clsClasses.clsCliente>()

    var idcli=0
    var nomcli=""
    var idcont=0
    var iddir=0

    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_clientes)

            super.initbase(savedInstanceState)

            recview = findViewById<View>(R.id.recview) as RecyclerView
            recview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            txtflt = findViewById<View>(R.id.editTextText) as EditText

            ClienteObj = clsClienteObj(this, Con!!, db!!)

            gl?.gint=0;gl?.gstr=""

            listItems()

            setHandlers()
        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }

    //region Events

    fun doSave(view: View) {

        gl?.gint=idcli
        gl?.gstr=nomcli

        finish()
    }

    fun doClear(view: View) {
        txtflt?.setText("")
    }

    fun doExit(view: View) {
        gl?.gint=0;gl?.gstr=""
        finish()
    }

    private fun setHandlers() {
        try {

            recview?.addOnItemTouchListener(
                RecyclerItemClickListener(this, recview!!,
                    object : RecyclerItemClickListener.OnItemClickListener {

                        override fun onItemClick(view: View, position: Int) {
                            gl?.gint=items.get(position).codigo_cliente
                            gl?.gstr=items.get(position).nombre

                            var idcli=0
                            var nomcli=0

                            //finish()
                        }

                        override fun onItemLongClick(view: View?, position: Int) { }
                    })
            )

            txtflt?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    listItems()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Main

    private fun listItems() {
        try {
            var flt=txtflt?.text?.toString()
            if (flt?.isNotEmpty()!!) {
                ClienteObj!!.fill("WHERE (Nombre LIKE '%"+flt+"%') ORDER BY Nombre")
            } else {
                ClienteObj!!.fill("ORDER BY Nombre")
            }

            items.clear()
            for (itm in ClienteObj?.items!!) {
                if (itm.nombre.isNotEmpty()) {
                    if (itm.nombre.length>2) items.add(itm)
                }
            }

            adapter = LA_ClienteAdapter(items)
            recview?.setAdapter(adapter)
        } catch (e: java.lang.Exception) {
            mu!!.msgbox(e.message)
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

            ClienteObj!!.reconnect(Con!!, db!!)

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion
}