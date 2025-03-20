package com.dts.osm


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.clsClienteObj
import com.dts.classes.clsClientecontactoObj
import com.dts.classes.clsClientedirObj
import com.dts.classes.extListDlg
import com.dts.ladapt.LA_ClienteAdapter
import com.dts.osm.R

class Clientes : PBase() {

    var recview: RecyclerView? = null
    var txtflt: EditText? = null
    var cbdir: TextView? = null
    var cbcont: TextView? = null

    var adapter: LA_ClienteAdapter? = null

    var ClienteObj: clsClienteObj? = null
    var ClientedirObj: clsClientedirObj? = null
    var ClientecontactoObj: clsClientecontactoObj? = null

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
            cbdir = findViewById<View>(R.id.textView12) as TextView
            cbcont = findViewById<View>(R.id.textView7) as TextView

            ClienteObj = clsClienteObj(this, Con!!, db!!)
            ClientedirObj = clsClientedirObj(this, Con!!, db!!)
            ClientecontactoObj = clsClientecontactoObj(this, Con!!, db!!)

            gl?.gint=0;gl?.gstr=""

            listItems()

            setHandlers()
        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }

    //region Events

    fun doSave(view: View) {
        try {
            if (idcli==0) {
                msgbox("Falta seleccionar un cliente.");return
            }

            /*
            if (iddir==0) {
                msgbox("Falta seleccionar una dirección.");return
            }

            if (idcont==0) {
                msgbox("Falta seleccionar un contacto.");return
            }
            */

            gl?.gint=idcli
            gl?.gstr=nomcli
            gl?.gint2=iddir
            gl?.gint3=idcont

            finish()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun doDir(view: View) {
        if (idcli>0) listaDir() else msgbox("Debe seleccionar un cliente.")
    }

    fun doContact(view: View) {
        if (idcli>0) listaCont() else msgbox("Debe seleccionar un cliente.")
    }

    fun doClear(view: View) {
        txtflt?.setText("")
    }

    fun doExit(view: View) {
        finish()
    }

    private fun setHandlers() {
        try {

            recview?.addOnItemTouchListener(
                RecyclerItemClickListener(this, recview!!,
                    object : RecyclerItemClickListener.OnItemClickListener {

                        override fun onItemClick(view: View, position: Int) {
                            idcli=items.get(position).codigo_cliente
                            nomcli=items.get(position).nombre
                            gl?.gintval=items.get(position).Nivel

                            initCliente()
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

        initCliente()
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

    fun listaDir() {
        try {

            ClientedirObj?.fill("WHERE (codigo_cliente="+idcli+") ORDER BY direccion")
            if (ClientedirObj?.count==0) {
                msgbox("No está definida ninguna dirección de clienmte");return;
            }

            val listdlg = extListDlg();

            listdlg.buildDialog(this,"Dirección")
            //listdlg.setLines(4);
            listdlg.setCenterScreenPosition()
            listdlg.setWidth(10000)

            for (itm in ClientedirObj?.items!!) {
                listdlg.addData(itm.codigo_direccion,itm.direccion)
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
            onResume()
            iddir=value
            cbdir?.text=text!!
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun listaCont() {
        try {
            try {

                ClientecontactoObj?.fill("WHERE (codigo_cliente="+idcli+") ORDER BY direccion")
                if (ClientecontactoObj?.count==0) {
                    msgbox("No está definida ninguna dirección de clienmte");return;
                }

                val listdlg = extListDlg();

                listdlg.buildDialog(this,"Contacto")
                //listdlg.setLines(4);
                listdlg.setCenterScreenPosition()
                listdlg.setWidth(10000)

                for (itm in ClientecontactoObj?.items!!) {
                    listdlg.addData(itm.codigo_cliente_contacto,itm.nombre)
                }


                listdlg.clickListener= Runnable { processContMenu(listdlg.selcodint,listdlg.selvalue) }

                listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
                listdlg.show()

            } catch (e: Exception) {
                msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun processContMenu(value:Int,text:String) {
        try {
            onResume()
            idcont=value
            cbcont?.text=text!!
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

    //region Aux

    fun initCliente() {
        idcont=0;iddir=0
        cbdir?.text="Dirección . . ."
        cbcont?.text="Contacto . . ."
    }

    //endregion

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }

            ClienteObj!!.reconnect(Con!!, db!!)
            ClientedirObj!!.reconnect(Con!!, db!!)
            ClientecontactoObj!!.reconnect(Con!!, db!!)

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion
}