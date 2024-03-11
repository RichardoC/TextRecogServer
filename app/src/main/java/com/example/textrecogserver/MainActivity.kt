package com.example.textrecogserver


import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import fi.iki.elonen.NanoHTTPD
import java.io.File
import java.io.IOException

class MainActivity : Activity() {

    private lateinit var server: MyHTTPD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            server = MyHTTPD()
            server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false)
            Toast.makeText(this, "Server started", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error starting server", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        server.stop()
    }

    inner class MyHTTPD : NanoHTTPD(8080) {
        private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        override fun serve(session: IHTTPSession): Response {
            if (session.method == Method.POST) {
                try {
                    val files = HashMap<String, String>()
                    session.parseBody(files)

                    val location = files["image"]

                    val tempFile =  File(location.toString())

                    val bitmap = BitmapFactory.decodeFile(tempFile.absolutePath)

                    val image = InputImage.fromBitmap(bitmap, 0)
                    val result = recognizer.process(image)
                    val textResult = Tasks.await(result)


                    val text = textResult.text

                    return newFixedLengthResponse(text)

                } catch (e: Exception) {
                    return newFixedLengthResponse("Error: $e")
                }
            }



            return newFixedLengthResponse("POST your image as 'image' to get back the recognised text. Must be upright")
        }
    }
}
