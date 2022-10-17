package com.example.uploadimage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmusicadmin.databinding.ActivityMainBinding
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class MainActivity:AppCompatActivity() ,UploadRequestBody.UploadCallback{
    lateinit var binding: ActivityMainBinding
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.textView.setOnClickListener(View.OnClickListener {
            openImageChooser()
        })
//        binding.buttonUpload.setOnClickListener {
//            uploadImage()
//        }
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data

                }
            }
        }
    }

    private fun uploadImage() {
        if (selectedImageUri == null) {
            binding.root.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

//        progress_bar.progress = 0
        val body = UploadRequestBody(file, "proof", this)
        MyAPI().uploadImage(
            MultipartBody.Part.createFormData(
                "proof",
                file.name,
                body
            ),
        ).enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                binding.root.snackbar(t.message!!)
//                progress_bar.progress = 0
            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    binding.root.snackbar(it.message)
//                    progress_bar.progress = 100
                }
            }
        })

    }

    override fun onProgressUpdate(percentage: Int) {

    }
}
