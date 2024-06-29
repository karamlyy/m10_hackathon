package com.example.qrcodescanner

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/payment")
    fun sendPaymentRequest(@Body request: PaymentRequestDTO): Call<TransactionDTO>
}
