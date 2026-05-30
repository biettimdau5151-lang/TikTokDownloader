package com.tiktokdownloader.app

import android.os.Environment
import android.widget.EditText
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var etUsers: EditText
    private lateinit var tvResult: TextView
    private lateinit var btnDownload: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUsers = findViewById(R.id.etUsers)
        tvResult = findViewById(R.id.tvResult)
        btnDownload = findViewById(R.id.btnDownload)

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        btnDownload.setOnClickListener {
            val users = etUsers.text.toString().trim()
            if (users.isEmpty()) {
                tvResult.text = "Nhap username!"
                return@setOnClickListener
            }
            btnDownload.isEnabled = false
            tvResult.text = "Dang tai..."
            CoroutineScope(Dispatchers.IO).launch {
                val py = Python.getInstance()
                val module = py.getModule("tiktok_dl")
                try {
                    var i = 0
                    val downloadDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
                    val lines = users.lines().filter { it.isNotBlank() }
                    for (line in lines) {
                        val result = module.callAttr("download", line.trim(), downloadDir).toString()
                        withContext(Dispatchers.Main) {
                            tvResult.append("\n[$i] $result")
                        }
                        i++
                    }
                    withContext(Dispatchers.Main) {
                        tvResult.append("\n\nHoan thanh!")
                        btnDownload.isEnabled = true
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        tvResult.append("\nLOI: ${e.message}")
                        btnDownload.isEnabled = true
                    }
                }
            }
        }
    }
}
