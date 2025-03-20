package com.dts.osm

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.clsExistenciaObj
import com.dts.ladapt.LA_ExistenciasAdapter
import com.dts.ladapt.LA_ProductoAdapter

class InvLista : PBase() {

    private var recyclerView: RecyclerView? = null
    private var ExistenciasObj: clsExistenciaObj? = null
    private var adapter: LA_ExistenciasAdapter? = null
    private var items = ArrayList<clsClasses.clsExistencia>()
    private var editText: EditText? = null


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
                            val existenciaSeleccionada = items[position]
                            //msgbox("Producto seleccionado: ${productoSeleccionado.desclarga}")()

                            gl?.gcant=existenciaSeleccionada.cant
                            gl?.gstr=existenciaSeleccionada.nombre

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