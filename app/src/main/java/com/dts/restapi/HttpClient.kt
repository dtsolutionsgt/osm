package com.dts.restapi

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

class HttpClient {

    var url=""
    var params=""

    var retcode = 0
    var data: String? = null

    private val client: OkHttpClient
    private var rnCallback: Runnable? = null

    init {
        client = OkHttpClient()
    }

    fun processRequest(request: Request?, callback: Runnable?) {

        rnCallback = callback

        client.newCall(request!!).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                data = e.message
                retcode = -1
                rnCallback!!.run()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    data = response.body!!.string()
                    retcode = 1
                } else {
                    data = "" + response.code + " " + response.message
                    retcode = 0
                }
                rnCallback!!.run()
            }
        })
    }

    fun splitJsonArray(): List<String> {
        var jos=""

        var jss:String= data!!
        jss=jss.replace("$"+"type","type")

        val jsonArray = JSONArray(jss)
        val list = mutableListOf<String>()

        for (i in 0 until jsonArray.length()) {
            jos=jsonArray.getString(i).trim()
            list.add(jos)
        }

        return list
    }

}
