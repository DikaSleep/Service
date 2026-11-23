
package com.example.fundamentalapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object ApiService {

    private const val BASE_URL =
        "https://jsonplaceholder.typicode.com"

// HTTP GET
    suspend fun get(
        endpoint: String,
        headers: Map<String, String> = emptyMap()
    ): String {
        return request(endpoint, "GET", headers = headers)
    }

    // HTTP POST
    suspend fun post(
        endpoint: String,
        body: String,
        headers: Map<String, String> = emptyMap()
    ): String {
        return request(endpoint, "POST", body, headers)
    }

    // HTTP PUT
    suspend fun put(
        endpoint: String,
        body: String,
        headers: Map<String, String> = emptyMap()
    ): String {
        return request(endpoint, "PUT", body, headers)
    }

    // HTTP DELETE
    suspend fun delete(
        endpoint: String,
        headers: Map<String, String> = emptyMap()
    ): String {
        return request(endpoint, "DELETE", headers = headers)
    }
    // Fungsi utama untuk mengirim HTTP Request
    suspend fun request(
        endpoint: String,
        method: String,
        body: String? = null,
        headers: Map<String, String> = emptyMap()
    ): String = withContext(Dispatchers.IO) {

        val url = URL("$BASE_URL$endpoint")
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = method
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

            // Menambahkan Headers
            connection.setRequestProperty(
                "Accept", "application/json"
            )

            //authentication lewat header
            headers.forEach { (key, value) ->
                connection.setRequestProperty(key, value)
            }

            // Mengirim Body jika tersedia
            if (body != null) {
                connection.doOutput = true

                connection.setRequestProperty(
                    "Content-Type", "application/json"
                )

                connection.outputStream.use { output ->
                    output.write(body.toByteArray(Charsets.UTF_8))
                }
            }

            // Mengambil response dari server
            val responseCode = connection.responseCode

            val stream = if (responseCode in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }

            val response = stream?.bufferedReader()?.use {
                it.readText()
            } ?: ""

            "HTTP $responseCode\n$response"

        } finally {
            connection.disconnect()
        }
    }
}
