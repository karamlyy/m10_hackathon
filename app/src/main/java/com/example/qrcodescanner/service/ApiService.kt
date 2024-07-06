package com.example.qrcodescanner.service

import com.example.qrcodescanner.model.PaymentRequestDTO
import com.example.qrcodescanner.model.TransactionDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/payment")
    fun sendPaymentRequest(@Body request: PaymentRequestDTO): Call<TransactionDTO>
}
