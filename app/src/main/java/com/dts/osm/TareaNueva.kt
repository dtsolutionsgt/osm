package com.dts.osm


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.clsProdprecioObj
import com.dts.classes.clsTiposerviciosObj
import com.dts.classes.extListDlg
import com.dts.ladapt.LA_MaterialAdapter


class TareaNueva : PBase() {

    var recview: RecyclerView? = null
    var lbltipo: TextView? = null
    var lblcli: TextView? = null
    var lbldesc: TextView? = null

    var ProdprecioObj: clsProdprecioObj? = null

    var adapter: LA_MaterialAdapter? = null

    var items = ArrayList<clsClasses.clsMaterial>()
    lateinit var item : clsClasses.clsMaterial
    lateinit var selitem : clsClasses.clsMaterial


    var idcli=0
    var nomcli=""
    var idtipo=0
    var nomtipo=""
    var iddir=0
    var idcont=0
    var descrip=""
    var nivel=0
    var selindex=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_tarea_nueva)

            super.initbase(savedInstanceState)

            recview = findViewById<View>(R.id.recview) as RecyclerView
            recview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            lbltipo = findViewById<View>(R.id.editTextText2) as TextView
            lblcli = findViewById<View>(R.id.editTextText) as TextView
            lbldesc = findViewById<View>(R.id.editTextText3) as TextView

            ProdprecioObj = clsProdprecioObj(this, Con!!, db!!)

            idtipo=gl?.gint!!
            nomtipo=gl?.gstr!!;lbltipo?.text=nomtipo

            gl?.gnota="";gl?.gint=0;gl?.gstr=""
            items.clear()

            setHandlers();

            listItems();

        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }


    //region Events

    fun doAdd(view: View) {
        if (idcli==0) {
            msgbox("Falta seleccionar cliente.");return
        }

        gl?.gint=0
        callback=2
        startActivity(Intent(this,Material::class.java))
    }

    fun doCliente(view: View) {
        gl?.gint=0
        callback=1
        startActivity(Intent(this,Clientes::class.java))
    }

    fun doSave(view: View) {
        if (checkValues()) save()
    }

    fun doNote(view: View) {
        showDescDialog() { text ->
            descrip=text
            lbldesc?.text=descrip
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
                            selitem = items[position]
                            selindex=position
                            showItemMenu()
                        }

                        override fun onItemLongClick(view: View?, position: Int) {}
                    })
            )

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }


    //endregion

    //region Main

    fun listItems() {
        try {
            adapter = LA_MaterialAdapter(items)
            recview?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun save() {
        try {

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun AddItem() {
        var prec=0.0
        var idmat=gl?.gint!!

        try {
            ProdprecioObj?.fill("WHERE (codigo_producto="+gl?.gint!!+") AND (nivel="+nivel+")");
            if (ProdprecioObj?.count==0) {
                msgbox("Para este cliente el material no tiene definido precio.")
            } else {
                prec=ProdprecioObj?.first()?.precio!!
                if (prec!!<0) msgbox("Para este cliente el material no tiene definido precio.");
            }

            if (items.size>0) {
                for (itm in items) {
                    if (itm?.codigo_producto==idmat) {
                        itm?.cant=gl?.gcant!!
                        listItems()
                        return
                    }
                }
            }

            item= clsClasses.clsMaterial(idmat, gl?.gstr!!, gl?.gcant!!,prec)
            items.add(item)

            listItems()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun EditItem(nc: Double) {
        try {
            items.get(selindex).cant=nc!!
            listItems()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun DelItem() {
        try {
            items?.removeAt(selindex)
            listItems()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Dialogs

    fun dialogswitch() {
        try {
            when (gl?.dialogid) {
                1 -> {  DelItem() }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun showItemMenu() {
        try {
            val listdlg = extListDlg();

            listdlg.buildDialog(this@TareaNueva,"Opciónes")
            listdlg.setLines(2)
            listdlg.setWidth(-1)
            listdlg.setCenterScreenPosition()

            listdlg.addData(1,"Cambiar cantidad")
            listdlg.addData(2,"Borrar")

            listdlg.clickListener= Runnable { processItemMenu(listdlg.selcodint) }

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
                2 -> { msgask(1,"Borrar registro?") }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun showDescDialog( onTextEntered: (String) -> Unit) {
        var editText = EditText(this).apply {
            setText(descrip)
            requestFocus()
            setSelection(descrip.length)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            minLines = 4
            maxLines = 6
        }

        editText.filters = arrayOf(InputFilter.LengthFilter(490))
        editText.isVerticalScrollBarEnabled = true
        editText.isHorizontalScrollBarEnabled = false

        AlertDialog.Builder(this)
            .setTitle("Descripción")
            .setView(editText)
            .setPositiveButton("Guardar") { _, _ ->
                onTextEntered(editText.text.toString())
            }
            .setNegativeButton("Salir", null)
            .show()
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
                    if (ide==0.0) DelItem() else EditItem(ide)
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

    //region Aux

    fun checkValues() : Boolean {
        if (idcli==0) {
            msgbox("Falta seleccionar cliente.");return false
        }
        if (idtipo==0) {
            msgbox("Falta seleccionar tipo de servicio.");return false
        }

        /*
        if (iddir==0) {
            msgbox("Falta seleccionar una dirección.");return false
        }

        if (idcont==0) {
            msgbox("Falta seleccionar un contacto.");return false
        }
        */

        return true
    }

    //endregion

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }

            ProdprecioObj?.reconnect(Con!!, db!!)

            if (callback==1) {
                callback=0
                if (gl?.gint!=0) {

                    idcli=gl?.gint!!
                    nomcli=gl?.gstr!!
                    iddir=gl?.gint2!!
                    idcont=gl?.gint3!!
                    nivel=gl?.gintval!!

                    lblcli?.text=nomcli
                }
            }

            if (callback==2) {
                callback=0
                if (gl?.gint!=0) AddItem()
            }


        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}