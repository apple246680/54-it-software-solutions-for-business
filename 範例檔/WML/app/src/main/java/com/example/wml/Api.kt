package com.example.wml

import com.example.wml.Api.result
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.ByteArrayInputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64
import kotlin.concurrent.thread

object Api {
    private fun connect(
        url: String,
        method: String,
        header: Map<String, String>
    ): HttpURLConnection =
        (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = method
            header.forEach { (y, u) -> setRequestProperty(y, u) }
        }

    private fun HttpURLConnection.result(body: JSONObject, successful: (String) -> Boolean) {
        thread {
            if (body.length() != 0) {
                BufferedWriter(OutputStreamWriter(outputStream)).use {
                    it.write(body.toString())
                    it.flush()
                }
            }
            if (responseCode == 200) {
                successful(inputStream.bufferedReader().readText())
            }
        }
    }
    private fun HttpURLConnection.QRresult(body: JSONObject, successful: (ByteArray) -> Boolean) {
        thread {
            if (body.length() != 0) {
                BufferedWriter(OutputStreamWriter(outputStream)).use {
                    it.write(body.toString())
                    it.flush()
                }
            }
            if (responseCode == 200) {
                val ay=ByteArrayInputStream(inputStream.readBytes()).readBytes()
                successful(ay)
            }
        }
    }
    fun postStatus(successful: (JSONObject) -> Boolean) {
        var json = JSONObject().apply {
            put("StationNumber", 20)
        }
        connect(
            "http://10.0.2.2:5000/api/ConnectionMonitor/RetrieveSiteStatus",
            "POST",
            mapOf("Content-Type" to "application/json")
        ).result(
            json
        ) {
            successful(JSONObject(it))
        }
    }
    fun postQRCODE(input:String,successful: (ByteArray ) -> Boolean) {
        var json = JSONObject().apply {
            put("Text", input)
            put("SizePixels", 360)
            put("CorrectionLevel", 0)
        }
        connect(
            "http://10.0.2.2:5000/Image/GenerateQRCode",
            "POST",
            mapOf("Content-Type" to "application/json")
        ).QRresult(
            json
        ) {
            successful(it)
        }
    }
}