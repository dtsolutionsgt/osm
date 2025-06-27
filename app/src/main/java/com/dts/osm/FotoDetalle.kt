package com.dts.osm

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.*
import android.media.MediaScannerConnection
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.dts.base.clsClasses
import com.dts.classes.clsEnvioimagenObj
import com.dts.classes.clsOrdenfotoObj
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FotoDetalle : PBase() {

    var img1: ImageView? = null
    var lbl1: TextView? = null
    var reltop: RelativeLayout? = null
    var relbot: RelativeLayout? = null

    var EnvioimagenObj: clsEnvioimagenObj? = null
    var OrdenfotoObj: clsOrdenfotoObj? = null

    var item = clsClasses.clsOrdenfoto()

    var idordfoto = 0
    var horiz = false
    var bitmap: Bitmap? = null
    var rotationAngle = 0f

    val REQUEST_DIBUJO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_foto_detalle)

            super.initbase(savedInstanceState)

            img1 = findViewById(R.id.imageView23)
            lbl1 = findViewById(R.id.textView29)
            reltop = findViewById(R.id.reltop)
            relbot = findViewById(R.id.relbot)

            idordfoto = gl?.idordfoto!!

            OrdenfotoObj = clsOrdenfotoObj(this, Con!!, db!!)
            EnvioimagenObj = clsEnvioimagenObj(this, Con!!, db!!)

            val sharedPref = getSharedPreferences("FotoPrefs", Context.MODE_PRIVATE)

            // Obtener el ángulo de rotación guardado en las preferencias
            rotationAngle = sharedPref.getFloat("rotation_$idordfoto", 0f)

            // Obtener la imagen y aplicar la rotación si es necesario
            loadItem()
            horiz = when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> true
                Configuration.ORIENTATION_PORTRAIT -> false
                else -> true
            }
            if (horiz) {
                reltop?.visibility = View.GONE; relbot?.visibility = View.GONE
            } else {
                reltop?.visibility = View.VISIBLE; relbot?.visibility = View.VISIBLE
            }

            loadItem()

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //region Events

    fun doText(view: View) {
        showLargeTextInputDialog(this, "Observacion", item.nota) { text ->
            item.nota = text
            item.statcom = 0
            OrdenfotoObj?.update(item)
            lbl1?.text = text
        }
    }

    fun doObserv(view: View) {
        if (item.nota.isNotBlank()) msgbox(item.nota)
    }

    fun doExit(view: View) {
        finish()
    }

    fun doRotate(view: View) {
        rotateImage()
    }

    fun doDraw(view: View) {
        try {
            val intent = Intent(this, DibujoActivity::class.java)
            val file = File(gl?.picdir, item.nombre)
            intent.putExtra("imagePath", file.absolutePath)
            intent.putExtra("rotationAngle", rotationAngle)
            startActivityForResult(intent,1)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }


    fun doSave(view: View) {
        saveImageAndSendToDB()
    }

    //endregion

    //region Main

    fun loadItem() {
        try {
            OrdenfotoObj?.fill("WHERE (id=$idordfoto)")
            item = OrdenfotoObj?.first()!!

            lbl1?.text = item.nota

            val fbm = File(gl?.picdir, item.nombre)
            if (fbm.exists()) {
                val originalBitmap = BitmapFactory.decodeFile(fbm.absolutePath)

                val sharedPref = getSharedPreferences("FotoPrefs", Context.MODE_PRIVATE)
                rotationAngle = sharedPref.getFloat("rotation_$idordfoto", 0f)


                if (rotationAngle != 0f) {
                    val matrix = Matrix()
                    matrix.postRotate(rotationAngle)
                    bitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, true)
                } else {
                    bitmap = originalBitmap
                }

                img1?.setImageBitmap(bitmap)
            }
        } catch (e: Exception) {
            msgbox("loadItem . ${e.message}")
        }
    }


    fun rotateImage() {
        try {
            if (bitmap == null) return

            rotationAngle = (rotationAngle + 90f) % 360

            val matrix = Matrix()
            matrix.postRotate(90f)
            bitmap = Bitmap.createBitmap(bitmap!!, 0, 0, bitmap!!.width, bitmap!!.height, matrix, true)

            img1?.setImageBitmap(bitmap)
            img1?.invalidate()
            img1?.requestLayout()

            var eiitem = clsClasses.clsEnvioimagen(item?.nombre!!, 0)
            try {
                //EnvioimagenObj?.add(eiitem)
            } catch (e: Exception) {
                //EnvioimagenObj?.update(eiitem)
            }

            val sharedPref = getSharedPreferences("FotoPrefs", Context.MODE_PRIVATE)
            sharedPref.edit().putFloat("rotation_$idordfoto", rotationAngle).apply()

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }





    private fun saveImageAndSendToDB() {
        try {
            if (bitmap == null) {
                Toast.makeText(this, "No hay imagen para guardar", Toast.LENGTH_SHORT).show()
                return
            }

            val finalBitmap = bitmap!!
            val file = File(gl?.picdir, item.nombre)

            FileOutputStream(file).use { out ->
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }

            item.statcom = 0
            OrdenfotoObj?.update(item)

            scanMediaFile(file)

            Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show()
            finish()

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //region Dialogs

    fun dialogswitch() {
        try {
            when (gl?.dialogid) {
                0 -> {}
                else -> msgbox("Diálogo no definido: ${gl?.dialogid}")
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

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }

            OrdenfotoObj?.reconnect(Con!!, db!!)
            EnvioimagenObj?.reconnect(Con!!,db!!)


        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val updatedImagePath = data?.getStringExtra("imagePath")
            if (updatedImagePath != null) {
                val file = File(updatedImagePath)
                if (file.exists()) {
                    img1?.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
                } else {
                    Toast.makeText(this, "No se encontró la imagen guardada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }




    //endregion

    private fun scanMediaFile(file: File) {
        MediaScannerConnection.scanFile(
            this,
            arrayOf(file.toString()),
            null,
            null
        )
    }
}