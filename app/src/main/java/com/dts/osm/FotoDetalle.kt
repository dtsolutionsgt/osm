package com.dts.osm


import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.dts.base.clsClasses
import com.dts.classes.clsOrdenfotoObj
import java.io.File

class FotoDetalle : PBase() {

    var img1: ImageView? = null
    var lbl1: TextView? = null
    var reltop: RelativeLayout? = null
    var relbot: RelativeLayout? = null

    var OrdenfotoObj: clsOrdenfotoObj? = null

    var item= clsClasses.clsOrdenfoto()

    var idordfoto=0
    var horiz=false

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_foto_detalle)

            super.initbase(savedInstanceState)

            img1 = findViewById(R.id.imageView23)
            lbl1 = findViewById(R.id.textView29)
            reltop = findViewById(R.id.reltop)
            relbot = findViewById(R.id.relbot)

            idordfoto=gl?.idordfoto!!

            OrdenfotoObj = clsOrdenfotoObj(this, Con!!, db!!)

            horiz = when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> true
                Configuration.ORIENTATION_PORTRAIT -> false
                else -> true
            }
            if (horiz) {
                reltop?.visibility= View.GONE;relbot?.visibility= View.GONE
            } else {
                reltop?.visibility= View.VISIBLE;relbot?.visibility= View.VISIBLE
            }

            loadItem()

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //region Events

    fun doText(view : View) {
        showLargeTextInputDialog(this, "Observacion",item.nota) { text ->
            item.nota=text
            item.statcom=0
            OrdenfotoObj?.update(item)
            lbl1?.text=text
        }
    }

    fun doObserv(view : View) {
        if (item.nota.isNotBlank()) msgbox(item.nota)
    }

    fun doExit(view : View) {
        finish()
    }

    //endregion

    //region Main

    fun loadItem() {
        try {
            OrdenfotoObj?.fill("WHERE (id="+idordfoto+")")
            item=OrdenfotoObj?.first()!!

            lbl1?.text=item.nota

            try {
                var fbm= File(gl?.picdir,item.nombre)
                if (fbm.exists()) {
                    val fbmp = BitmapFactory.decodeFile(fbm.absolutePath)
                    img1?.setImageBitmap(fbmp)
                }
            } catch (e: Exception) {}

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Dialogs

    fun dialogswitch() {
        try {
            when (gl?.dialogid) {
                0 -> {}
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun showLargeTextInputDialog(context: Context, title: String, stext: String, onTextSubmitted: (String) -> Unit) {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
        }

        val inputEditText = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            minLines = 5
            maxLines = 10
            isSingleLine = false
            setText(stext)
            setSelection(stext.length)
        }

        layout.addView(inputEditText)

        AlertDialog.Builder(context)
            .setTitle(title)
            .setView(layout)
            .setPositiveButton("Aplicar") { dialog, _ ->
                onTextSubmitted(inputEditText.text.toString())
                dialog.dismiss()
            }
            .setNegativeButton("Salir") { dialog, _ -> dialog.cancel() }
            .show()

        inputEditText.requestFocus()

    }

    //endregion

    //region Aux


    //endregion

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }

            OrdenfotoObj?.reconnect(Con!!,db!!)

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}