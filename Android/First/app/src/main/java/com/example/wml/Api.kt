package com.example.wml

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object Api {
    fun connect(url:String,method:String):HttpURLConnection=(URL(url).openConnection() as HttpURLConnection).apply {
        requestMethod=method
        setRequestProperty("Content-Type","application/json")
    }
    suspend fun postAsync(url: String, body: String): ByteArray = withContext(Dispatchers.IO) {
        val connection = URL(url).openConnection() as HttpURLConnection

        connection.run {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json")
            connectTimeout = 100
            doOutput = true
            outputStream.use { os ->
                val inputBytes = body.toByteArray()
                os.write(inputBytes, 0, inputBytes.size)
            }
            return@withContext inputStream.readBytes()
        }
    }
}