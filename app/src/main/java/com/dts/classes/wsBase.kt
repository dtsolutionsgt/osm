package com.dts.classes

import android.os.AsyncTask
import android.os.Handler

open class wsBase(var URL: String?) {

    var callBack: Runnable? = null
    var error: String? = ""
    var errflag = false
    var level = 0
    var items = ArrayList<String>()
    var NAMESPACE = "http://tempuri.org/"
    private var idle = true

    open fun execute(afterfinish: Runnable?) {
        if (idle) {
            errflag = false
            error = ""
            callBack = afterfinish
            execute()
        }
    }

    fun pause() {
        idle = false
    }

    fun resume() {
        idle = true
    }

    protected open fun wsExecute() {}

    protected open fun wsFinished() {
        runCallBack()
    }

    private fun execute() {
        try {
            val wstask = AsyncCallwsBase()
            wstask.execute()
        } catch (e: Exception) {
            error = e.message
            errflag = true
            try {
                runCallBack()
            } catch (ee: Exception) {
            }
        }
    }

    private fun runCallBack() {
        if (callBack == null) return
        val cbhandler = Handler()
        cbhandler.postDelayed({ callBack!!.run() }, 50)
    }

    private inner class AsyncCallwsBase : AsyncTask<String?, Void?, Void?>() {

        protected override fun doInBackground(vararg p0: String?): Void? {
            try {
                wsExecute()
            } catch (e: Exception) {
                error = e.message
                errflag = true
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            try {
                wsFinished()
            } catch (e: Exception) { }
        }

        override fun onPreExecute() {}

        protected override fun onProgressUpdate(vararg values: Void?) {}

    }

}