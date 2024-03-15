package com.example.wml

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object Api{
    suspend fun postAsync(url: String, body: String): ByteArray = withContext(Dispatchers.IO) {
        val connection = URL(url).openConnection() as HttpURLConnection

        connection.run {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json-patch+json")
            connectTimeout = 10000
            doOutput = true
            outputStream.use { os ->
                val inputBytes = body.toByteArray()
                os.write(inputBytes, 0, inputBytes.size)
            }
            return@withContext inputStream.readBytes()
        }
    }
}