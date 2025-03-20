package com.dts.osm

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
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
import com.dts.classes.clsExistenciaObj
import com.dts.classes.extListDlg
import com.dts.ladapt.LA_ExistenciasAdapter
import com.dts.ladapt.LA_ProductoAdapter

class InvLista : PBase() {

    private var recyclerView: RecyclerView? = null
    private var ExistenciasObj: clsExistenciaObj? = null
    private var adapter: LA_ExistenciasAdapter? = null
    private var items = ArrayList<clsClasses.clsExistencia>()
    private var editText: EditText? = null
    var selindex=-1


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_inv_lista)

            super.initbase(savedInstanceState)

            recyclerView = findViewById(R.id.recview)
            editText = findViewById(R.id.editTextText)
            recyclerView?.layoutManager = LinearLayoutManager(this)

            ExistenciasObj = clsExistenciaObj(this, Con!!, db!!)

            adapter = LA_ExistenciasAdapter(items)
            recyclerView?.adapter = adapter


            listItems()
            setHandlers()


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

    //region
    private fun setHandlers() {
        try {
            recyclerView?.addOnItemTouchListener(
                RecyclerItemClickListener(this, recyclerView!!,
                    object : RecyclerItemClickListener.OnItemClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            selindex = position
                            val existenciaSeleccionada = items[position]
                            //msgbox("Producto seleccionado: ${productoSeleccionado.desclarga}")()

                            gl?.gcant=existenciaSeleccionada.cant
                            gl?.gstr=existenciaSeleccionada.nombre

                            showItemMenu()

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

    //region
    private fun listItems() {
        try {
            ExistenciasObj?.fill("ORDER BY nombre")

            items.clear()
            ExistenciasObj?.items?.let { items.addAll(it) }
            adapter?.notifyDataSetChanged()

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun EditItem(nc: Double) {
        try {
            if (selindex in items.indices) {
                val existencia = items[selindex]
                existencia.cant = nc  // Actualizar en la lista

                // Actualizar en la base de datos
                ExistenciasObj?.update(existencia)

                adapter?.notifyDataSetChanged()
            } else {
                msgbox("Índice fuera de rango: $selindex")
            }
        } catch (e: Exception) {
            msgbox("EditItem: " + e.message)
        }
    }

    fun DelItem() {
        val alert: AlertDialog.Builder = AlertDialog.Builder(this)
        alert.setTitle("Ingrese unidades a devolver")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.setText("")
        input.requestFocus()
        alert.setView(input)

        alert.setPositiveButton("Devolver") { _, _ ->
            try {
                val unidades = input.text.toString().toDoubleOrNull()
                if (unidades == null || unidades <= 0) {
                    msgbox("Cantidad inválida")
                    return@setPositiveButton
                }
                if (selindex in items.indices) {
                    val existencia = items[selindex]
                    if (unidades > existencia.cant) {
                        msgbox("No puedes devolver más de lo que existe")
                        return@setPositiveButton
                    }

                    existencia.cant -= unidades

                    if (existencia.cant == 0.0) {
                        ExistenciasObj?.delete(existencia.codigo)
                        items.removeAt(selindex)
                    } else {
                        ExistenciasObj?.update(existencia)
                    }

                    adapter?.notifyDataSetChanged()
                } else {
                    msgbox("Ítem no válido")
                }
            } catch (e: Exception) {
                msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            }
        }

        alert.setNegativeButton("Cancelar", null)

        val dialog: AlertDialog = alert.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
    }

    private fun buscarProducto(filtro: String) {
        try {
            ExistenciasObj?.fill("WHERE nombre LIKE '%$filtro%' ORDER BY nombre")

            items.clear()
            ExistenciasObj?.items?.let { items.addAll(it) }
            adapter?.notifyDataSetChanged()


        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
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

            sql = "DELETE FROM Existencia WHERE cant=0"
            db!!.execSQL(sql)


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
    fun showItemMenu() {
        try {
            val listdlg = extListDlg();

            listdlg.buildDialog(this@InvLista,"Opciones")
            listdlg.setLines(2)
            listdlg.setWidth(-1)
            listdlg.setCenterScreenPosition()

            listdlg.addData(1,"Ajustar cantidad")
            listdlg.addData(2,"Devolver unidades")

            listdlg.clickListener = Runnable { processItemMenu(listdlg.selcodint) }

            listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
            listdlg.show()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun processItemMenu(menuidx:Int) {
        try {
            when (menuidx) {
                1 -> { ingresoCantidad() }
                2 -> { DelItem() }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    private fun ingresoCantidad() {
        val alert: AlertDialog.Builder = AlertDialog.Builder(this)
        alert.setTitle("Ingrese cantidad")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.setText("")
        input.requestFocus()
        alert.setView(input)

        alert.setPositiveButton("Aplicar") { _, _ ->
            try {
                val ide = input.text.toString().toDoubleOrNull()
                if (ide == null || ide < 0) {
                    msgbox("Cantidad inválida")
                    return@setPositiveButton
                }
                if (selindex in items.indices) {
                    EditItem(ide)
                } else {

                    msgbox("No se seleccionó un ítem válido")
                }
            } catch (e: Exception) {
                msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            }
        }

        alert.setNegativeButton("Cancelar", null)

        val dialog: AlertDialog = alert.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
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

            listItems()

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}