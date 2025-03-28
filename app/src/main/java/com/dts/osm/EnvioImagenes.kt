package com.dts.osm

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.dts.base.clsClasses
import com.dts.classes.clsEnvioimagenObj
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File


class EnvioImagenes : PBase() {

    var lblreg: TextView? = null

    var fbstorage: FirebaseStorage? = null
    var storageReference: StorageReference? = null

    var EnvioimagenObj: clsEnvioimagenObj? = null

    var reglim=0
    var regpos=0
    var idfoto=""
    var ftipo=0
    var idle=true

    var item=clsClasses.clsEnvioimagen()

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_envio_imagenes)

            super.initbase(savedInstanceState)

            lblreg = findViewById(R.id.textView30)

            onBackPressedDispatcher.addCallback(this,backPress)

            fbstorage = FirebaseStorage.getInstance()
            storageReference = fbstorage?.getReference()

            EnvioimagenObj = clsEnvioimagenObj(this, Con!!, db!!)
            EnvioimagenObj?.fill()
            reglim=EnvioimagenObj?.count!!-1
            regpos=0

            if (app?.sinInternet() == true) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({finish()}, 200)
            } else {
                idle=false
                processItem()
            }

        } catch (e: Exception) {
            var se=e.message
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //region Events

    //endregion

    //region Main

    fun processItem() {
        try {
            if (regpos<=reglim) {

                runOnUiThread {lblreg?.text = "  Actualizando: "+(regpos+1)+" / "+(reglim+1)+"   "}

                item=EnvioimagenObj?.items?.get(regpos)!!
                idfoto=item?.id!!

                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    uploadFile()
                }, 200)
            } else {
                finishSession()
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun uploadFile() {
        try {
            regpos++

            var devpath="/storage/emulated/0/Pictures/" + item.id
            var storpath="fotos/" + gl?.idemp +"/" + item.id

            if (item.tipo==1) {

                var ifile = File(devpath)
                var ufile = Uri.fromFile(ifile)
                val fstorRef = storageReference?.child(storpath)
                var uploadTask = fstorRef?.putFile(ufile)

                uploadTask?.addOnFailureListener { exception ->
                    processItem() //exception.message
                }?.addOnSuccessListener { taskSnapshot ->
                    db?.execSQL("DELETE FROM Envioimagen WHERE (id='" + item.id + "')");
                    processItem()
                }
            } else if (item.tipo==0) {

                val storageReference: StorageReference = FirebaseStorage.getInstance().getReference(storpath)

                storageReference.delete()
                    .addOnSuccessListener {
                        db?.execSQL("DELETE FROM Envioimagen WHERE (id='" + item.id + "')");
                        processItem()
                    }
                    .addOnFailureListener { exception ->
                        processItem()
                    }

            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun finishSession() {
        idle=true
        finish()
    }

    //endregion

    //region Dialogs


    //endregion

    //region Aux


    //endregion

    //region Activity Events

    override fun onResume() {
        super.onResume()
        try {
            EnvioimagenObj!!.reconnect(Con!!, db!!)
        } catch (e: java.lang.Exception) {
            msgbox(e.message)
        }
    }

    val backPress = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (idle) onBackPressedDispatcher?.onBackPressed() else toast("Espere, por favor . . . ")
        }
    }

    //endregion

}