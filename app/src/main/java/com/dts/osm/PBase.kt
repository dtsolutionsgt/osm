package com.dts.osm

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dts.base.AppMethods
import com.dts.base.BaseDatos
import com.dts.base.DateUtils
import com.dts.base.MiscUtils
import com.dts.base.appGlobals
import com.dts.base.clsClasses
import java.io.File

open class PBase : AppCompatActivity() {

    var active = 0
    var db: SQLiteDatabase? = null
    var Con: BaseDatos? = null
    var ins: BaseDatos.Insert? = null
    var upd: BaseDatos.Update? = null
    var sql: String? = null

    var DBPath: String? = null
    var DBName: String? = null
    var imgdir = ""

    var gl: appGlobals? = null
    var mu: MiscUtils? = null
    var du: DateUtils? = null
    var app: AppMethods? = null

    var clsCls = clsClasses()

    var callback = 0
    var mode = 0
    var selid = 0
    var selidx = 0
    var fecha: Long = 0
    var stamp: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_pbase)
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun initbase(savedInstanceState: Bundle?) {
        DBName = "osm.db"
        DBPath = storage + "/" + DBName
        //DBPath = Environment.getExternalStorageDirectory().path + "/" + DBName

        Con = BaseDatos(this, DBPath)
        opendb()
        ins = Con!!.Ins
        upd = Con!!.Upd

        gl = this.application as appGlobals
        gl!!.context = this

        gl!!.urlbase = "http://ec2-52-41-114-122.us-west-2.compute.amazonaws.com:8090/"

        mu = MiscUtils(this, gl)
        du = DateUtils()
        app = AppMethods(this, gl!!, Con!!, db!!)

        fecha = du!!.actDateTime
        stamp = du!!.actDate
        selid = -1
        selidx = -1
        callback = 0

        holdInstance(savedInstanceState)
    }

    //region Common

    var storage: String = ""
        get() {
            val sd = applicationContext.getExternalFilesDir("")!!.absolutePath
            val sp = sd+"/images/"
            //val sd = Environment.getExternalStorageDirectory().path
            //val sp = sd +"/osmimages/"

            try {
                val directory = File(sd)
                directory.mkdirs()
                val imgdirectory = File(sp)
                imgdirectory.mkdirs()
                imgdir=imgdirectory?.absolutePath!!
            } catch (e: Exception) {
                val ss = e.message
            }
            return sd
        }

    fun boolToInt(value: Boolean): Int {
        return if (value) 1 else 0
    }

    //endregion

    //region Database

    fun opendb() {
        try {
            db = Con!!.writableDatabase
            Con!!.vDatabase = db
            active = 1
        } catch (e: Exception) {
            mu!!.msgbox(e.message)
            active = 0
        }
    }

    fun exec() {
        db!!.execSQL(sql)
    }

    fun open(): Cursor {
        val dt: Cursor
        dt = Con!!.OpenDT(sql)
        try {
            dt.moveToFirst()
        } catch (e: Exception) {
        }
        return dt
    }

    //endregion

    //region Web service callback
    @Throws(Exception::class)
    fun wsCallBack(callmode: Int, throwing: Boolean, errmsg: String?) {
        if (throwing) throw Exception(errmsg)
    }

    //endregion

    //region WebAPI Common



    //endregion

    //region Messages

    fun toast(msg: String?) {
        if (mu!!.emptystr(msg)) return
        val toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun toastlong(msg: String?) {
        if (mu!!.emptystr(msg)) return
        val toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun toast(`val`: Double) {
        this.toast("" + `val`)
    }

    fun msgask(dialogid: Int, msg: String?) {
        gl!!.dialogid = dialogid
        mu!!.msgask(dialogid, msg)
    }

    fun msgask(dialogid: Int, msg: String?, tit: String?) {
        gl!!.dialogid = dialogid
        mu!!.msgask(dialogid, msg, tit)
    }

    fun msgbox(msg: String?) {
        mu!!.msgbox(msg)
    }

    fun msgbox(`val`: Int) {
        mu!!.msgbox("" + `val`)
    }

    fun msgbox(`val`: Double) {
        mu!!.msgbox("" + `val`)
    }

    //endregion

    //region Instance

    fun holdInstance(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) gl!!.restoreInstance(savedInstanceState)
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        gl!!.saveInstance(savedInstanceState)
        super.onSaveInstanceState(savedInstanceState)
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        gl!!.restoreInstance(savedInstanceState)
    }

    //endregion

    //region Activity Events

    public override fun onResume() {
        try {
            app!!.reconnect(Con!!, db!!)
        } catch (e: Exception) {
        }
        opendb()
        super.onResume()
    }

    public override fun onPause() {
        try {
            Con!!.close()
        } catch (e: Exception) {
        }
        active = 0
        super.onPause()
    }

    //endregion
}