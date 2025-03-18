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
import com.dts.classes.clsSaveposObj
import com.dts.classes.clsUsuarioObj
import com.dts.ladapt.LA_UsuarioAdapter

class Usuarios : PBase() {

    var recview: RecyclerView? = null

    var adapter: LA_UsuarioAdapter? = null

    var UsuarioObj: clsUsuarioObj? = null

    var items = ArrayList<clsClasses.clsUsuario>()

    var iduser=0
    var rol=""
    var pais=""

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_usuarios)

            super.initbase(savedInstanceState)

            recview = findViewById<View>(R.id.recview) as RecyclerView
            recview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

            UsuarioObj = clsUsuarioObj(this, Con!!, db!!)

            listItems()

            setHandlers()
        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }

    //region Events

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
                            var ss=items.get(position).rol
                            pais=ss.substring(0,2)
                            rol=ss.substring(2)
                            ingresoClave(items.get(position).pin)
                        }

                        override fun onItemLongClick(view: View?, position: Int) { }
                    })
            )

            /*
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            */

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

            adapter = LA_UsuarioAdapter(items!!)
            recview?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    private fun ingresoClave(sclave:Int) {

        val alert: AlertDialog.Builder = AlertDialog.Builder(this)
        alert.setTitle("Ingrese PIN")
        val input = EditText(this)
        alert.setView(input)

        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.setText("")
        input.requestFocus()

        alert.setPositiveButton("Aplicar",
            DialogInterface.OnClickListener { dialog, whichButton ->
                try {
                    val ide = Integer.parseInt(input.text.toString())
                    if (ide!=sclave) throw Exception()

                    saveItem()
                } catch (e: java.lang.Exception) {
                    mu!!.msgbox("Contraseña incorrecta")
                    return@OnClickListener
                }
            })
        alert.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, whichButton -> })

        val dialog: AlertDialog = alert.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
    }

    fun saveItem() {
        var item = clsClasses.clsSavepos()

        try {
            if (iduser==0) {
                msgbox("Usuario incorrecto");return
            }

            val SaveposObj = clsSaveposObj(this, Con!!, db!!)

            try {
                item.id=1
                item.valor=""+iduser
                SaveposObj.add(item)

                item.id=4
                item.valor=rol
                SaveposObj.add(item)

                item.id=5
                item.valor=pais
                SaveposObj.add(item)

                var idsuc=141
                var mon="Q"
                when (pais) {
                    "HN" -> {idsuc=161;mon="L"}
                    "PA" -> {idsuc=163;mon="$"}
                    "SV" -> {idsuc=164;mon="$"}
                    "NI" -> {idsuc=165;mon="C$"}
                    "CR" -> {idsuc=169;mon="₡"}
                }

                item.id=6
                item.valor=""+idsuc
                SaveposObj.add(item)

                item.id=7
                item.valor=mon
                SaveposObj.add(item)

            } catch (e: Exception) {
                SaveposObj.update(item)
            }

            finish()
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

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion


}