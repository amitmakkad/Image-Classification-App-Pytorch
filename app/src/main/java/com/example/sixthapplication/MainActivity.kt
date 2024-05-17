package com.example.sixthapplication

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.opencsv.CSVWriter
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.io.FileWriter
import java.io.IOException
import kotlin.math.exp


class MainActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1

    lateinit var selectButton: Button
    lateinit var predictButton: Button
    lateinit var imageView: ImageView
    lateinit var predictedClassText: TextView
    lateinit var module: Module
    var bitmap: Bitmap? = null
    var x=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        module = LiteModuleLoader.load(assetFilePath(this, "custom_vit.ptl"))
        selectButton = findViewById(R.id.selectButton)
        predictButton = findViewById(R.id.predictButton)
        imageView = findViewById(R.id.imageView)
        predictedClassText = findViewById(R.id.predictedClassText)

//        val selectFromAssets = intent.getBooleanExtra("selectFromAssets", false)
//        val imageFileName = intent.getStringExtra("imageFileName")
//        println("select from assests $selectFromAssets")
//        println("image file name $imageFileName")

        selectButton.setOnClickListener {
            openGallery()
        }

        predictButton.setOnClickListener {
            predictImageClass(this,bitmap!!)
        }
    }

    private fun openGallery() {
        val selectFromAssets = intent.getBooleanExtra("selectFromAssets", false)
        val imageFileName = intent.getStringExtra("imageFileName")
        x=imageFileName.toString()

        if (selectFromAssets && !imageFileName.isNullOrEmpty()) {
            val assetManager = assets
            try {
                val inputStream = assetManager.open(imageFileName)
                bitmap = BitmapFactory.decodeStream(inputStream)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

    }


    private fun getResizedBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {

        val resizedBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)

        val scaleX = newWidth / bitmap.width.toFloat()
        val scaleY = newHeight / bitmap.height.toFloat()
        val pivotX = 0f
        val pivotY = 0f

        var scaleMatrix = Matrix()
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY)

        var canvas = Canvas(resizedBitmap)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, Paint(Paint.FILTER_BITMAP_FLAG))

        return resizedBitmap
    }

    private fun predictImageClass(context: Context,bitmap: Bitmap) {
        val resizedBitmap = getResizedBitmap(bitmap, 224, 224)
        val inputTensor = preprocessImage(resizedBitmap)
        val inputIValue = IValue.from(inputTensor)
        val outputIValue = module.forward(inputIValue)




        if(outputIValue.isTensor){

            val outputs=outputIValue.toTensor()
            val out=outputs.dataAsFloatArray
            val predictedClassIdx = out.indexOfFirst { it==out.max()?.toFloat() }

            predictedClassText.text = "Predicted Class Index: $predictedClassIdx"
            println("$x Predicted Class Index: $predictedClassIdx")
        }

        if (outputIValue.isDictStringKey) {
            val dict = outputIValue.toDictStringKey()
            for ((key, value) in dict) {
//                println("$key: $value")
                if (value.isTensor) {
                    val tensorValue: Tensor = value.toTensor()
                    val predictedClassIdx = tensorValue.dataAsFloatArray.indexOfFirst { it==tensorValue.dataAsFloatArray.max()?.toFloat() }
//                    println("Predicted Class Index: $predictedClassIdx")

                    predictedClassText.text = "Predicted Class Index: $predictedClassIdx"

                    val data = tensorValue.dataAsFloatArray
                    for (index in data.indices) {
                        val value = data[index]
                    }
//                    calculating softmax probabilities
//                    val probabilitiesArray = calculateSoftmaxProbabilities(tensorValue)
//
//                    Write probabilities to a text file in the assets folder
//                    writeProbabilitiesToFile(probabilitiesArray)

                }
            }
        }


    }

    var means = floatArrayOf(0.5f, 0.5f, 0.5f)
    var stds = floatArrayOf(0.5f, 0.5f, 0.5f)

    private fun preprocessImage(bitmap: Bitmap): Tensor {

//        val inputBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false)
        val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(bitmap, means, stds)

//        Apply normalization
//        inputTensor.data = inputTensor.data.sub_(0.5f).div_(0.5f)
//        val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(resizedBitmap, means, stds)
        return inputTensor
    }

    private fun assetFilePath(context: Context, assetName: String): String {
        val file = context.getFileStreamPath(assetName)
        if (file.exists()) {
            return file.absolutePath
        }

        try {
            context.assets.open(assetName).use { inputStream ->
                context.openFileOutput(assetName, Context.MODE_PRIVATE).use { outputStream ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Int
                    while (inputStream.read(buffer, 0, 8192).also { bytesRead = it } >= 0) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }
            }
            return file.absolutePath
        } catch (e: Exception) {
            throw IOException("Error copying asset file: $assetName", e)
        }
    }

    private fun loadBitmapFromUri(uri: Uri): Bitmap? {
        val inputStream = contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            var selectedImageUri = data.data
            bitmap = loadBitmapFromUri(selectedImageUri!!)
            imageView.setImageBitmap(bitmap)
        }
    }

    private fun calculateSoftmaxProbabilities(tensorValue: Tensor): FloatArray {
        val logitsArray = tensorValue.dataAsFloatArray
        val maxLogit = logitsArray.maxOrNull() ?: 0.0f
        var sumExp = 0.0f
        for (i in logitsArray.indices) {
            logitsArray[i] = exp(logitsArray[i] - maxLogit)
            sumExp += logitsArray[i]
        }
        for (i in logitsArray.indices) {
            logitsArray[i] /= sumExp
        }
        return logitsArray
    }


    private fun writeProbabilitiesToFile(probabilities: FloatArray) {
        val fileName = "android prob elephant 05.txt"
        try {
            val externalDir = getExternalFilesDir(null) // Get the external storage directory
            val file = File(externalDir, fileName)

            val writer = FileWriter(file)
            for (index in probabilities.indices) {
                val probability = probabilities[index]
                val line = "$probability\n"
                writer.write(line)
            }
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
