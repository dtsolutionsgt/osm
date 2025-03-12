package com.dts.base

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Patterns
import android.view.Gravity
import android.widget.Toast
import com.dts.base.BaseDatos.Update
import com.dts.classes.clsParamObj
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Currency
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream


class AppMethods( private val cont: Context, private val gl: appGlobals,
                  private var Con: BaseDatos, private var db: SQLiteDatabase) {
    var flicsup: Long = 0
    var flicbck: Long = 0
    private var ins: BaseDatos.Insert
    private var upd: Update
    var clsCls = clsClasses()

    init {
        ins = Con.Ins
        upd = Con.Upd
    }

    fun reconnect(dbconnection: BaseDatos, database: SQLiteDatabase) {
        Con = dbconnection
        db = database
        ins = Con.Ins
        upd = Con.Upd
    }

    //region Public

    fun params() {
        var idparam=0

        try {
            var ParamObj = clsParamObj(cont, Con, db)

            try {
                idparam=1
                ParamObj.fill("WHERE (id="+idparam+") AND (userid="+gl?.iduser!!+")")
                if (ParamObj.count==0) ParamObj.fill("WHERE (id="+idparam+")")

                gl?.pegps=ParamObj?.first()?.valor?.toUpperCase(Locale.getDefault())=="S"
            } catch (e: Exception) {
                gl?.pegps=false
            }

            try {
                idparam=2
                ParamObj.fill("WHERE (id="+idparam+") AND (userid="+gl?.iduser!!+")")
                if (ParamObj.count==0) ParamObj.fill("WHERE (id="+idparam+")")

                gl?.peHini= ParamObj?.first()?.valor?.toInt()!!
            } catch (e: Exception) {
                gl?.peHini=8
            }

            try {
                idparam=3
                ParamObj.fill("WHERE (id="+idparam+") AND (userid="+gl?.iduser!!+")")
                if (ParamObj.count==0) ParamObj.fill("WHERE (id="+idparam+")")

                gl?.peHfin= ParamObj?.first()?.valor?.toInt()!!
            } catch (e: Exception) {
                gl?.peHfin=17
            }

            try {
                idparam=4
                ParamObj.fill("WHERE (id="+idparam+") AND (userid="+gl?.iduser!!+")")
                if (ParamObj.count==0) ParamObj.fill("WHERE (id="+idparam+")")

                gl?.peSab=ParamObj?.first()?.valor?.toUpperCase(Locale.getDefault())=="S"
            } catch (e: Exception) {
                gl?.peSab=false
            }

            try {
                idparam=5
                ParamObj.fill("WHERE (id="+idparam+") AND (userid="+gl?.iduser!!+")")
                if (ParamObj.count==0) ParamObj.fill("WHERE (id="+idparam+")")

                gl?.peHSini= ParamObj?.first()?.valor?.toInt()!!
            } catch (e: Exception) {
                gl?.peHSini=8
            }

            try {
                idparam=6
                ParamObj.fill("WHERE (id="+idparam+") AND (userid="+gl?.iduser!!+")")
                if (ParamObj.count==0) ParamObj.fill("WHERE (id="+idparam+")")

                gl?.peHSfin= ParamObj?.first()?.valor?.toInt()!!
            } catch (e: Exception) {
                gl?.peHSfin=17
            }

            try {
                idparam=7
                ParamObj.fill("WHERE (id="+idparam+") AND (userid="+gl?.iduser!!+")")
                if (ParamObj.count==0) ParamObj.fill("WHERE (id="+idparam+")")

                gl?.pePassAdm= ParamObj?.first()?.valor!!
            } catch (e: Exception) {
                gl?.pePassAdm="1234"
            }

            try {
                idparam=8
                ParamObj.fill("WHERE (id="+idparam+") AND (userid="+gl?.iduser!!+")")
                if (ParamObj.count==0) ParamObj.fill("WHERE (id="+idparam+")")

                gl?.peDiasCoord= ParamObj?.first()?.valor!!.toInt()
            } catch (e: Exception) {
                gl?.peDiasCoord=7
            }

        } catch (ee: Exception) {
            throw Exception("Error en carga parametros: "+ee.message)
        }
    }

    @Throws(IOException::class)
    fun buildEncUpdate(cap: clsClasses.clsOrdenenccap, idest:Int, fs: String):String {

        upd!!.init("D_ORDEN_SERVICIO_ENC")

        upd!!.add("CODIGO_ESTADO_ORDEN_SERVICIO",idest)

        when (idest) {
            0 -> {
                upd!!.add("ANULADA",1)
                upd!!.add("ACTIVA",0)
                upd!!.add("CERRADA",1)
            }
            4 -> {
                upd!!.add("ANULADA",0)
                upd!!.add("ACTIVA",1)
                upd!!.add("CERRADA",0)
                upd!!.add("COORDENADA_X",cap.longit)
                upd!!.add("COORDENADA_Y",cap.latit)
                upd!!.add("HORA_INICIO_HH",fs)
            }
            5 -> {
                upd!!.add("ANULADA",0)
                upd!!.add("ACTIVA",1)
                upd!!.add("CERRADA",1)
                upd!!.add("HORA_FIN_HH",fs)
            }
        }

        var nota=cap.nota;nota=nota.replace("´","")
        upd!!.add("OBSERVACION",nota)

        upd!!.Where("(CODIGO_ORDEN_SERVICIO=" + cap.idorden + ")")

        return upd!!.sql()
    }

    //endregion

    //region Common

    fun getLocalCurrencySymbol(): String {
        val locale = Locale.getDefault()
        val currency = Currency.getInstance(locale)
        return currency.symbol as String
    }

    public fun sinInternet(): Boolean {
        if (checkInternet()) {
            return false
        } else {
            toast("Sin conexión al internet.");return true;
        }
    }

    fun copyToClipboard(text: String?) {
        try {
            val clipboard = cont.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(text, text)
            clipboard.setPrimaryClip(clip)
        } catch (e: Exception) {
            toast(e.message)
        }
    }

    @Throws(IOException::class)
    fun zip(file: String?, zipFile: String?) {
        var origin: BufferedInputStream? = null
        val out = ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile)))
        val BUFFER_SIZE = 6 * 1024
        try {
            val files = arrayOfNulls<String>(1)
            files[0] = file
            val data = ByteArray(BUFFER_SIZE)
            for (i in files.indices) {
                val fi = FileInputStream(files[i])
                origin = BufferedInputStream(fi, BUFFER_SIZE)
                try {
                    val entry = ZipEntry(
                        files[i]!!.substring(files[i]!!.lastIndexOf("/") + 1)
                    )
                    out.putNextEntry(entry)
                    var count: Int
                    while (origin.read(data, 0, BUFFER_SIZE).also { count = it } != -1) {
                        out.write(data, 0, count)
                    }
                } finally {
                    origin.close()
                }
            }
        } finally {
            out.close()
        }
    }

    fun unzip(zipFile: File?, targetDirectory: File?, dname: String?) {
        val DefaultBufferSize = 65536
        try {
            FileInputStream(zipFile).use { fis ->
                BufferedInputStream(fis).use { bis ->
                    ZipInputStream(bis).use { zis ->
                        var ze: ZipEntry
                        var count: Int
                        val buffer = ByteArray(DefaultBufferSize)
                        while (zis.nextEntry.also { ze = it } != null) {
                            //File file = new File(targetDirectory, ze.getName());
                            val file = File(targetDirectory, dname)
                            val dir = if (ze.isDirectory) file else file.parentFile
                            if (!dir.isDirectory && !dir.mkdirs()) throw FileNotFoundException("Failed to ensure directory: " + dir.absolutePath)
                            if (ze.isDirectory) continue
                            FileOutputStream(file).use { fout ->
                                while (zis.read(buffer)
                                        .also { count = it } != -1
                                ) fout.write(buffer, 0, count)
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            //handle exception
        }
    }

    fun invalidEmail(email: CharSequence?): Boolean {
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //endregion

    //region Private

    protected fun toast(msg: String?) {
        val toast = Toast.makeText(cont, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    protected fun toastlong(msg: String?) {
        val toast = Toast.makeText(cont, msg, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    private fun checkInternet(): Boolean {
        val connectivityManager = cont.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo?.isConnectedOrConnecting ?: false
        }
    }

    //endregion

}