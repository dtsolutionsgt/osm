package com.dts.osm

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.clsProductoObj
import com.dts.ladapt.LA_ProductoAdapter

class Productos : PBase() {

    private var recyclerView: RecyclerView? = null
    private var editText: EditText? = null

    private var productoObj: clsProductoObj? = null
    private var adapter: LA_ProductoAdapter? = null
    private var items = ArrayList<clsClasses.clsProducto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_productos)

            super.initbase(savedInstanceState)

            recyclerView = findViewById(R.id.recview)
            editText = findViewById(R.id.editTextText)

            recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            productoObj = clsProductoObj(this, Con!!, db!!)

            listItems()
            setHandlers()
        } catch (e: Exception) {
            msgbox("Error en onCreate: " + e.message)
        }
    }

    //region Eventos

    fun doExit(view: View) {
        finish()
    }

    private fun setHandlers() {
        try {
            recyclerView?.addOnItemTouchListener(
                RecyclerItemClickListener(this, recyclerView!!,
                    object : RecyclerItemClickListener.OnItemClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            val productoSeleccionado = items[position]
                            //msgbox("Producto seleccionado: ${productoSeleccionado.desclarga}")()

                            gl?.gint=productoSeleccionado.codigo_producto
                            gl?.gstr=productoSeleccionado.desclarga
                            ingresoCantidad()
                        }

                        override fun onItemLongClick(view: View?, position: Int) {}
                    })
            )

           editText?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val filtro = editText?.text.toString()
                    if (filtro.isNotEmpty()) buscarProducto(filtro) else listItems()
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
            productoObj?.fill(" WHERE CODIGO_TIPO = 'P' ORDER BY desclarga")

            items.clear()
            for (item in productoObj?.items!!) {
                items.add(item)
            }

            adapter = LA_ProductoAdapter(items)
            recyclerView?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    private fun buscarProducto(filtro: String) {
        try {
            productoObj?.fill("WHERE (desclarga LIKE '%$filtro%') AND CODIGO_TIPO = 'P' ORDER BY desclarga")

            items.clear()
            productoObj?.items?.let { items.addAll(it) }

            adapter = LA_ProductoAdapter(items)
            recyclerView?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    private fun ingresoCantidad() {
        val alert: AlertDialog.Builder = AlertDialog.Builder(this)
        alert.setTitle("Ingrese cantidad")
        val input = EditText(this)
        alert.setView(input)

        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.setText("")
        input.requestFocus()

        alert.setPositiveButton("Aplicar",
            DialogInterface.OnClickListener { dialog, whichButton ->
                try {
                    val ide = input.text.toString().toDouble()

                    gl?.gcant = ide
                    callback = 1
                    finish()

                } catch (e: java.lang.Exception) {
                    mu!!.msgbox("Cantidad inválida")
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
}
