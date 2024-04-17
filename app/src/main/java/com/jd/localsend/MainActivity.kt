package com.jd.localsend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.net.toFile
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.textview)
        if (intent.clipData != null)
            lifecycleScope.launch {
                val count = intent.clipData!!.itemCount
                for (i in 0 until count) {
                    val item = intent.clipData!!.getItemAt(i)
                    val bookName = item.uri.lastPathSegment!!
                    if (contentResolver.openFileDescriptor(item.uri, "r").use { it!!.statSize > 1024*1024*64 })
                        Log.i("Activity", "File $bookName too big")
                    else if (!service.bookExists(bookName)) {
                        val stream = contentResolver.openInputStream(item.uri)
                        service.book(
                            bookName,
                            RequestBody.create(
                                MediaType.get("application/octet-stream"),
                                stream?.use { it.readBytes() })
                        )
                    }
                    textView.text = "${i + 1}/$count"
                }
            }
    }
}