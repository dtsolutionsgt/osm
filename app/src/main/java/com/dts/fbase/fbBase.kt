package com.dts.fbase

import android.os.Handler
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


open class fbBase(troot: String?, forced_callback : Boolean ) {

    var fdb: FirebaseDatabase

    var fdt: DatabaseReference

    var callBack: Runnable?
    var value: String? = null

    var root: String
    var rootpath: String = ""
    var forcedCallBack = false
    var errflag = false

    open var path: String=""

    constructor(troot: String?) : this(troot, false) { }

    constructor(troot: String?,trootpath : String) : this(troot, false) {
        rootpath=trootpath
    }


    init {
        fdb = FirebaseDatabase.getInstance()
        //fdb.setPersistenceEnabled(true);
        root = troot!!
        forcedCallBack = forced_callback

        fdt = fdb.getReference(root)
        fdt.keepSynced(true)

        callBack = null
        forcedCallBack = false
    }

    fun runCallBack() {
        var flag=true

        if (callBack == null) return
        if (errflag) flag=false
        if (forcedCallBack) flag=true

        flag=true

        if (flag) {
            val cbhandler = Handler()
            cbhandler.postDelayed({ callBack!!.run() }, 50)
        }
    }

    fun removeValue(key: String) {
        fdb.getReference(root+"/"+key).removeValue()
    }

    val key: String? get() = fdt.push().key


}