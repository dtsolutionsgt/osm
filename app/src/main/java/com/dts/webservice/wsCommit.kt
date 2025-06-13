package com.dts.webservice

import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapPrimitive
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

class wsCommit(Url: String?) : wsBase(Url) {

    private var command: String? = null

    fun execute(commandlist: String?, afterfinish: Runnable?) {
        command = commandlist
        super.execute(afterfinish)
    }

    override fun wsExecute() {
        commit()
    }

    fun commit() {
        val mNAME = "Commit"
        val sstr: String
        error = ""
        errflag = false

        try {
            val request = SoapObject(NAMESPACE, mNAME)
            val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
            envelope.dotNet = true

            val param = PropertyInfo()
            param.setType(String::class.java)
            param.setName("SQL")
            param.value = command
            request.addProperty(param)

            envelope.setOutputSoapObject(request)

            val transport = HttpTransportSE(URL)
            transport.call(NAMESPACE + mNAME, envelope)

            val response = envelope.response as SoapPrimitive
            sstr = response.toString()

            if (!sstr.equals("#", ignoreCase = true)) {
                error = sstr
                errflag = true
            }
        } catch (e: Exception) {
            error = e.message
            errflag = true
        }
    }
}