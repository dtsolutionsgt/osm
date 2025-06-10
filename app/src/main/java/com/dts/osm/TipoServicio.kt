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
import com.dts.classes.clsTiposervicioObj
import com.dts.ladapt.LA_ProductoAdapter
import com.dts.ladapt.LA_Tiposervicio


class TipoServicio : PBase() {

    var recview: RecyclerView? = null
    var txtflt: EditText? = null

    var TiposervicioObj: clsTiposervicioObj? = null

    var adapter: LA_Tiposervicio? = null

    var items = ArrayList<clsClasses.clsTiposervicio>()

    var selitem = clsClasses.clsTiposervicio()

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_tipo_servicio)

            super.initbase(savedInstanceState)

            recview = findViewById<View>(R.id.recview) as RecyclerView
            recview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            txtflt = findViewById<View>(R.id.editTextText4) as EditText

            TiposervicioObj = clsTiposervicioObj(this, Con!!, db!!)

            setHandlers();

            listItems();

        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }


    //region Events

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
                            selitem = items[position]

                        }

                        override fun onItemLongClick(view: View?, position: Int) { }
                    })
            )

            txtflt?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val filtro = txtflt?.text.toString()
                    if (filtro.isNotEmpty()) browseItems(filtro) else listItems()
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

    private fun listItems() {
        try {
            TiposervicioObj?.fill("ORDER BY Nombre")

            items.clear()
            for (itm in TiposervicioObj?.items!!) {
                items.add(itm)
            }

            adapter = LA_Tiposervicio(items)
            recview?.adapter = adapter
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

    private fun browseItems(filtro: String) {
        try {
            TiposervicioObj?.fill("WHERE (Nombre LIKE '%"+filtro+"%') ORDER BY Nombre")

            items.clear()
            TiposervicioObj?.items?.let { items.addAll(it) }

            adapter = LA_Tiposervicio(items)
            recview?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }
    //endregion

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }

            TiposervicioObj?.reconnect(Con!!,db!!);

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}