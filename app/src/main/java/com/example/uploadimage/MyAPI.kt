package com.example.uploadimage

import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MyAPI {
    @Multipart
    @POST("upload")

    fun uploadImage(
        @Part proof: MultipartBody.Part
    ): Call<UploadResponse>

    companion object {
        operator fun invoke(): MyAPI {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            return Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(MyAPI::class.java)
        }
    }
}