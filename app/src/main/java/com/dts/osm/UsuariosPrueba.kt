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
import com.dts.classes.clsUsuarioObj
import com.dts.ladapt.LA_UsuarioAdapter
import com.dts.ladapt.LA_UsuarioPruebaAdapter

class UsuariosPrueba : PBase() {

    var recview: RecyclerView? = null
    var txtflt: EditText? = null

    var adapter: LA_UsuarioPruebaAdapter? = null

    var UsuarioObj: clsUsuarioObj? = null

    var items = ArrayList<clsClasses.clsUsuario>()

    var iduser=0
    var nombre=""

    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_usuarios_prueba)

            super.initbase(savedInstanceState)

            recview = findViewById<View>(R.id.recview) as RecyclerView
            recview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            txtflt = findViewById<View>(R.id.editTextText) as EditText

            UsuarioObj = clsUsuarioObj(this, Con!!, db!!)

            listItems()

            setHandlers()
        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }

    //region Events

    fun doBuscar(view: View) {
        try {
            var flt=txtflt?.text.toString()
            if (flt.isNotEmpty()) buscarUsuario(flt) else listItems()

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
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
                            iduser=items.get(position).id
                            nombre=items.get(position).nombre

                            msgbox(nombre)
                        }

                        override fun onItemLongClick(view: View?, position: Int) { }
                    })
            )

            txtflt?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    var flt=txtflt?.text.toString()
                    if (flt.isNotEmpty()) buscarUsuario(flt) else listItems()
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

    fun listItems() {

        try {
            UsuarioObj?.fill("ORDER BY Nombre")

            items.clear()
            for (item in UsuarioObj?.items!!) {
                items.add(item)
            }

            adapter = LA_UsuarioPruebaAdapter(items!!)
            recview?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun buscarUsuario(flt:String) {
        try {
            UsuarioObj?.fill("WHERE (Nombre LIKE '%"+flt+"%') ORDER BY Nombre")

            items.clear()
            for (item in UsuarioObj?.items!!) {
                items.add(item)
            }

            adapter = LA_UsuarioPruebaAdapter(items!!)
            recview?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }


    //endregion

}