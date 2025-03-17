package com.dts.base

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class appGlobals : Application() {

   	var context: Context? = null
    var dialogr: Runnable? = null
    var dialogid = 0

    var gstr=""
    var gint=0
    var gbool=false
    var fbkey=""

    var idemp=0
    var iduser=0
    var idrol=""
    var idorden=0
    var idfoto=""
    var idordfoto=0
    var modoapp=0

    var urlbase=""
    var nuser=""
    var changed=false
    var com_pend=false

    //Params
    var pegps=false;var peHini=-1;var peHfin=-1;
    var peSab=false;var peHSini=-1;var peHSfin=-1;
    var pePassAdm="";
    var peDiasCoord=7;

    val picdir="/storage/emulated/0/Pictures/"
    val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()


    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("location", "location",
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                setSound(null, null)
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun saveInstance(savedInstanceState: Bundle) {
        try {

            savedInstanceState.putInt("idemp", idemp)
            savedInstanceState.putInt("gint", gint)
            savedInstanceState.putInt("iduser", iduser)
            savedInstanceState.putInt("idorden", idorden)
            savedInstanceState.putInt("idordfoto", idordfoto)
            savedInstanceState.putInt("modoapp", modoapp)

            savedInstanceState.putString("idrol", idrol)
            savedInstanceState.putString("gstr", gstr)
            savedInstanceState.putString("fbkey", fbkey)
            savedInstanceState.putString("urlbase", urlbase)
            savedInstanceState.putString("nuser", nuser)
            savedInstanceState.putString("idphoto", idfoto)

            savedInstanceState.putBoolean("gbool", gbool)
            savedInstanceState.putBoolean("changed", changed)
            savedInstanceState.putBoolean("com_pend", com_pend)

            //Params

            savedInstanceState.putInt("peHini", peHini)
            savedInstanceState.putInt("peHfin", peHfin)
            savedInstanceState.putInt("peHSini", peHSini)
            savedInstanceState.putInt("peHSfin", peHSfin)

            savedInstanceState.putBoolean("pegps", pegps)
            savedInstanceState.putBoolean("peSab", peSab)

            savedInstanceState.putString("pePassAdm", pePassAdm)

        } catch (e: Exception) {
        }
    }

    fun restoreInstance(savedInstanceState: Bundle) {
        try {
            idemp = savedInstanceState.getInt("idemp")
            gint = savedInstanceState.getInt("gint")
            iduser = savedInstanceState.getInt("iduser")
            idorden = savedInstanceState.getInt("idorden")
            idordfoto = savedInstanceState.getInt("idordfoto")
            modoapp = savedInstanceState.getInt("modoapp")

            idrol = savedInstanceState.getString("idrol").toString()
            gstr = savedInstanceState.getString("gstr").toString()
            fbkey = savedInstanceState.getString("fbkey").toString()
            urlbase = savedInstanceState.getString("urlbase").toString()
            nuser = savedInstanceState.getString("nuser").toString()
            idfoto = savedInstanceState.getString("idphoto").toString()

            gbool = savedInstanceState.getBoolean("gbool")
            changed = savedInstanceState.getBoolean("changed")
            com_pend = savedInstanceState.getBoolean("com_pend")

            //Params

            peHini = savedInstanceState.getInt("peHini")
            peHfin = savedInstanceState.getInt("peHfin")
            peHSini = savedInstanceState.getInt("peHSini")
            peHSfin = savedInstanceState.getInt("peHSfin")

            pegps = savedInstanceState.getBoolean("pegps")
            peSab = savedInstanceState.getBoolean("peSab")

            pePassAdm = savedInstanceState.getString("pePassAdm").toString()

        } catch (e: Exception) {
        }
    }

    private fun toastlong(msg: String) {
        val toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

}