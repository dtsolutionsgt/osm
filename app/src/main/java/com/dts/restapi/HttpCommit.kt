package com.dts.restapi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class HttpCommit(var URL: String?) {

    var errflag = true
    var error: String? = null

    private val url=URL
    private val client: OkHttpClient = OkHttpClient()
    private var rnCallback: Runnable? = null

    fun commit(sql: String? , callback: Runnable?) {

        rnCallback = callback

        CoroutineScope(Dispatchers.IO).launch {
            processCommit(sql!!)
        }
    }


    private fun processCommit(sql: String) {

        val json = """{"sql":"$sql"}"""
        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url!!)
            .post(requestBody)
            .build()

        client.newCall(request!!).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                error = e.message
                errflag = true
                rnCallback!!.run()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    error = response.body!!.string()
                    errflag = false
                } else {
                    error = "" + response.code + " " + response.message
                    errflag = true
                }
                rnCallback!!.run()
            }
        })

    }

}