package com.example.firstlinearmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val interPreter = Interpreter(loadModelFile(), null)
        val edt = findViewById<EditText>(R.id.editText)
        val btn = findViewById<AppCompatButton>(R.id.button)
        val text = findViewById<TextView>(R.id.textView)

        btn.setOnClickListener {
            val amount = predictValue(edt.text.toString(), interPreter)
            text.text = "Result: $amount"

        }
    }

    fun loadModelFile(): MappedByteBuffer {
       val model = assets.openFd("linear.tflite")
        val fileInputSystem = FileInputStream(model.fileDescriptor)
        val channel = fileInputSystem.channel
        val startOffset = model.startOffset
        val lenght = model.length
        return channel.map(FileChannel.MapMode.READ_ONLY, startOffset, lenght)
    }

    fun predictValue(editText:String, interpreter: Interpreter):Float {

        val input = FloatArray(1)
        input[0] = editText.toFloat()
        val output = Array(1){
            FloatArray(1)
        }
        interpreter.run(input, output)
        return output[0][0]
    }
}