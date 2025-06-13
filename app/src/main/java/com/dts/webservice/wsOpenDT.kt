package com.dts.webservice


import android.database.Cursor
import android.database.MatrixCursor
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

class wsOpenDT(Url: String?) : wsBase(Url) {

    var openDTCursor: Cursor? = null

    private val results = ArrayList<String>()
    private var command: String? = null
    private var odt_rows = 0
    private var odt_cols = 0

    fun execute(commandlist: String?, afterfinish: Runnable?) {
        command = commandlist
        super.execute(afterfinish)
    }

    override fun wsExecute() {
        openDT()
    }

    fun openDT() {
        var str: String
        val rc: Int
        val METHOD_NAME = "getDT"
        results.clear()
        try {
            val request = SoapObject(NAMESPACE, METHOD_NAME)
            val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
            envelope.dotNet = true
            val param = PropertyInfo()
            param.setType(String::class.java)
            param.setName("SQL")
            param.value = command
            request.addProperty(param)
            envelope.setOutputSoapObject(request)
            val transport = HttpTransportSE(URL)
            transport.call(NAMESPACE + METHOD_NAME, envelope)
            val resSoap = envelope.response as SoapObject
            val result = envelope.bodyIn as SoapObject
            rc = resSoap.propertyCount - 1
            for (i in 0 until rc) {
                str = (result.getProperty(0) as SoapObject).getPropertyAsString(i)
                if (i == 0) {
                    if (!str.equals("#", ignoreCase = true)) throw Exception(str)
                } else {
                    results.add(str)
                    if (i == 1) odt_rows = str.toInt()
                    if (i == 2) odt_cols = str.toInt()
                }
            }
            createCursor()
        } catch (e: Exception) {
            errflag = true
            error = e.message
            createVoidCursor()
        }
    }

    private fun createCursor() {
        val mRow = arrayOfNulls<String>(odt_cols)
        val cursor = MatrixCursor(mRow)
        var pos: Int
        var ss: String
        try {
            createVoidCursor()
            if (odt_rows == 0) return
            pos = 2
            for (i in 0 until odt_rows) {
                for (j in 0 until odt_cols) {
                    try {
                        ss = results[pos]
                        if (ss.equals("anyType{}", ignoreCase = true)) ss = ""
                        mRow[j] = ss
                    } catch (e: Exception) {
                        mRow[j] = ""
                    }
                    pos++
                }
                cursor.addRow(mRow)
            }
            val rc = cursor.count
            openDTCursor = cursor
        } catch (e: Exception) {
            errflag = true
            error = e.message
            createVoidCursor()
        }
    }

    private fun createVoidCursor() {
        val mRow = arrayOfNulls<String>(odt_cols)
        val cursor = MatrixCursor(mRow)
        try {
            openDTCursor = cursor
        } catch (e: Exception) {
            errflag = true
            error = e.message
        }
    }

}